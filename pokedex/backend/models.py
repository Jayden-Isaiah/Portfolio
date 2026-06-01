from sqlalchemy import Column, Integer, String, ForeignKey, JSON
from sqlalchemy.orm import relationship
from db import Base

# ---------- TEAMS ----------

class Team(Base):
    
    #Équipe mock créée par l'utilisateur.
    #Contient jusqu'à 6 Pokémon avec stats personnalisées.
    
    __tablename__ = "teams"

    id         = Column(Integer, primary_key=True, index=True)
    name       = Column(String, nullable=False)          # Ex: "Mon équipe Unova"
    description = Column(String, nullable=True)

    # Relation vers les membres de l'équipe
    members    = relationship("TeamMember", back_populates="team", cascade="all, delete-orphan")


class TeamMember(Base):
    
    #Un Pokémon dans une équipe, avec ses stats personnalisées.
    #On garde l'ID PokéAPI (pokemon_id) pour fetcher le sprite/nom côté frontend.
    
    __tablename__ = "team_members"

    id          = Column(Integer, primary_key=True, index=True)
    team_id     = Column(Integer, ForeignKey("teams.id"), nullable=False)
    pokemon_id  = Column(Integer, nullable=False)    # ID PokéAPI (ex: 643 = Reshiram)
    pokemon_name = Column(String, nullable=False)    # Nom (ex: "reshiram") - redondant mais pratique
    slot        = Column(Integer, nullable=False)    # Position dans l'équipe : 1 à 6

    # Stats personnalisées stockées en JSON
    # Format : {"hp": 100, "attack": 120, "defense": 95, ...}
    # Si null → on utilise les stats de base de PokéAPI
    custom_stats = Column(JSON, nullable=True)

    nickname    = Column(String, nullable=True)      # Surnom optionnel

    team        = relationship("Team", back_populates="members")