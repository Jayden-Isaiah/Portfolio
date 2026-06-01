from pydantic import BaseModel, Field
from typing import Optional

# ---------- STATS ----------

class CustomStats(BaseModel):
    hp:              Optional[int] = Field(None, ge=1, le=999)
    attack:          Optional[int] = Field(None, ge=1, le=999)
    defense:         Optional[int] = Field(None, ge=1, le=999)
    special_attack:  Optional[int] = Field(None, ge=1, le=999)
    special_defense: Optional[int] = Field(None, ge=1, le=999)
    speed:           Optional[int] = Field(None, ge=1, le=999)


# ---------- TEAM MEMBER ----------

class TeamMemberCreate(BaseModel):
    pokemon_id:   int          = Field(..., ge=1)
    pokemon_name: str
    slot:         int          = Field(..., ge=1, le=6)
    custom_stats: Optional[CustomStats] = None
    nickname:     Optional[str] = None

class TeamMemberResponse(TeamMemberCreate):
    id: int

    class Config:
        from_attributes = True


# ---------- TEAM ----------

class TeamCreate(BaseModel):
    name:        str
    description: Optional[str] = None
    members:     list[TeamMemberCreate] = []

class TeamUpdate(BaseModel):
    name:        Optional[str] = None
    description: Optional[str] = None

class TeamResponse(BaseModel):
    id:          int
    name:        str
    description: Optional[str]
    members:     list[TeamMemberResponse]

    class Config:
        from_attributes = True