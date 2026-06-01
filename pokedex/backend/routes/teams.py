from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from db import get_db
from models import Team, TeamMember
from schemas import TeamCreate, TeamUpdate, TeamResponse, TeamMemberCreate

router = APIRouter(prefix="/teams", tags=["Teams"])


# ---------- CREATE ----------

@router.post("/", response_model=TeamResponse, status_code=201)
def create_team(payload: TeamCreate, db: Session = Depends(get_db)):
     
    #Crée une nouvelle équipe avec ses membres.
    #Les membres sont optionnels à la création — on peut les ajouter après.
     
    if len(payload.members) > 6:
        raise HTTPException(400, "Une équipe ne peut pas avoir plus de 6 Pokémon")

    # Vérifier que les slots sont uniques
    slots = [m.slot for m in payload.members]
    if len(slots) != len(set(slots)):
        raise HTTPException(400, "Deux Pokémon ne peuvent pas occuper le même slot")

    team = Team(name=payload.name, description=payload.description)
    db.add(team)
    db.flush()  # On flush pour obtenir l'ID de l'équipe avant d'ajouter les membres

    for m in payload.members:
        member = TeamMember(
            team_id      = team.id,
            pokemon_id   = m.pokemon_id,
            pokemon_name = m.pokemon_name,
            slot         = m.slot,
            custom_stats = m.custom_stats.model_dump() if m.custom_stats else None,
            nickname     = m.nickname,
        )
        db.add(member)

    db.commit()
    db.refresh(team)
    return team


# ---------- READ ----------

@router.get("/", response_model=list[TeamResponse])
def get_all_teams(db: Session = Depends(get_db)):
     #Retourne toutes les équipes sauvegardées. 
    return db.query(Team).all()


@router.get("/{team_id}", response_model=TeamResponse)
def get_team(team_id: int, db: Session = Depends(get_db)):
     #Retourne une équipe par son ID. 
    team = db.query(Team).filter(Team.id == team_id).first()
    if not team:
        raise HTTPException(404, f"Équipe {team_id} introuvable")
    return team


# ---------- UPDATE ----------

@router.patch("/{team_id}", response_model=TeamResponse)
def update_team(team_id: int, payload: TeamUpdate, db: Session = Depends(get_db)):
     #Met à jour le nom ou la description d'une équipe. 
    team = db.query(Team).filter(Team.id == team_id).first()
    if not team:
        raise HTTPException(404, f"Équipe {team_id} introuvable")

    if payload.name is not None:
        team.name = payload.name
    if payload.description is not None:
        team.description = payload.description

    db.commit()
    db.refresh(team)
    return team


@router.put("/{team_id}/members/{slot}", response_model=TeamResponse)
def upsert_member(
    team_id: int,
    slot: int,
    payload: TeamMemberCreate,
    db: Session = Depends(get_db)
):
     
    #Ajoute ou remplace le Pokémon dans un slot donné.
    #Si le slot est occupé, il est écrasé.
     
    team = db.query(Team).filter(Team.id == team_id).first()
    if not team:
        raise HTTPException(404, f"Équipe {team_id} introuvable")
    if slot < 1 or slot > 6:
        raise HTTPException(400, "Le slot doit être entre 1 et 6")

    existing = db.query(TeamMember).filter(
        TeamMember.team_id == team_id,
        TeamMember.slot == slot
    ).first()

    if existing:
        existing.pokemon_id   = payload.pokemon_id
        existing.pokemon_name = payload.pokemon_name
        existing.custom_stats = payload.custom_stats.model_dump() if payload.custom_stats else None
        existing.nickname     = payload.nickname
    else:
        member = TeamMember(
            team_id      = team_id,
            pokemon_id   = payload.pokemon_id,
            pokemon_name = payload.pokemon_name,
            slot         = slot,
            custom_stats = payload.custom_stats.model_dump() if payload.custom_stats else None,
            nickname     = payload.nickname,
        )
        db.add(member)

    db.commit()
    db.refresh(team)
    return team


# ---------- DELETE ----------

@router.delete("/{team_id}", status_code=204)
def delete_team(team_id: int, db: Session = Depends(get_db)):
     #Supprime une équipe et tous ses membres (cascade). 
    team = db.query(Team).filter(Team.id == team_id).first()
    if not team:
        raise HTTPException(404, f"Équipe {team_id} introuvable")
    db.delete(team)
    db.commit()


@router.delete("/{team_id}/members/{slot}", status_code=204)
def remove_member(team_id: int, slot: int, db: Session = Depends(get_db)):
     #Retire un Pokémon d'un slot spécifique. 
    member = db.query(TeamMember).filter(
        TeamMember.team_id == team_id,
        TeamMember.slot == slot
    ).first()
    if not member:
        raise HTTPException(404, f"Aucun Pokémon au slot {slot} dans l'équipe {team_id}")
    db.delete(member)
    db.commit()