from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from db import engine
import models
from routes import pokemon, teams

# Crée toutes les tables au démarrage si elles n'existent pas
models.Base.metadata.create_all(bind=engine)

app = FastAPI(
    title="Pokédex API",
    description="Backend pour le Pokédex Gen 5→9 avec Team Builder",
    version="1.0.0",
)

# CORS — permet au frontend (HTML/JS) de contacter l'API en local
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],   # En prod : remplacer par l'URL du frontend
    allow_methods=["*"],
    allow_headers=["*"],
)

# Enregistrement des routes
app.include_router(pokemon.router)
app.include_router(teams.router)


@app.get("/")
def root():
    return {
        "message": "Pokédex API opérationnelle",
        "docs":    "/docs",
        "routes": {
            "pokemon":     "/pokemon/{id ou nom}",
            "generation":  "/pokemon/generation/{5-9}",
            "type":        "/pokemon/type/{nom}",
            "teams":       "/teams/",
        }
    }