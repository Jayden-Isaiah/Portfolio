import httpx
from fastapi import APIRouter, HTTPException

router = APIRouter(prefix="/pokemon", tags=["Pokémon"])

POKEAPI = "https://pokeapi.co/api/v2"

# Plages d'ID par génération
GENERATIONS = {
    5: range(494, 650),   # Victini → Genesect
    6: range(650, 722),
    7: range(722, 810),
    8: range(810, 906),
    9: range(906, 1026),
}

# Formes spéciales à inclure (Mega, Gigantamax, formes régionales)
SPECIAL_FORMS = {
    5: [],  # Gen 5 n'a pas de formes régionales rétroactives
    6: [    # Méga-évolutions gen 6
        "charizard-mega-x", "charizard-mega-y", "mewtwo-mega-x", "mewtwo-mega-y",
        "venusaur-mega", "blastoise-mega",
    ],
    7: [    # Formes d'Alola
        "rattata-alola", "raticate-alola", "raichu-alola", "sandshrew-alola",
        "sandslash-alola", "vulpix-alola", "ninetales-alola", "diglett-alola",
        "dugtrio-alola", "meowth-alola", "persian-alola", "geodude-alola",
        "graveler-alola", "golem-alola", "grimer-alola", "muk-alola",
        "exeggutor-alola", "marowak-alola",
    ],
    8: [    # Formes de Galar + Gigantamax
        "meowth-galar", "ponyta-galar", "rapidash-galar", "farfetchd-galar",
        "weezing-galar", "mr-mime-galar", "corsola-galar", "zigzagoon-galar",
        "linoone-galar", "darumaka-galar", "darmanitan-galar-standard",
        "yamask-galar", "stunfisk-galar",
    ],
    9: [    # Formes de Paldea
        "tauros-paldea-combat-breed", "tauros-paldea-blaze-breed",
        "tauros-paldea-aqua-breed", "wooper-paldea",
    ],
}


async def fetch_pokemon(identifier: str | int) -> dict:
    #Fetch un Pokémon depuis PokéAPI et retourne les données essentielles.
    async with httpx.AsyncClient() as client:
        r = await client.get(f"{POKEAPI}/pokemon/{identifier}", timeout=10)
        if r.status_code == 404:
            raise HTTPException(404, f"Pokémon '{identifier}' introuvable")
        r.raise_for_status()
        data = r.json()

    # Stats sous forme de dict lisible
    stats = {s["stat"]["name"]: s["base_stat"] for s in data["stats"]}

    return {
        "id":      data["id"],
        "name":    data["name"],
        "types":   [t["type"]["name"] for t in data["types"]],
        "sprite":  data["sprites"]["other"]["official-artwork"]["front_default"],
        "sprite_shiny": data["sprites"]["other"]["official-artwork"]["front_shiny"],
        "stats":   stats,
        "height":  data["height"],   # en décimètres
        "weight":  data["weight"],   # en hectogrammes
    }


@router.get("/{identifier}")
async def get_pokemon(identifier: str):
     
    #Récupère un Pokémon par ID ou nom.
    #Ex: /pokemon/646  ou  /pokemon/kyurem
     
    return await fetch_pokemon(identifier)


@router.get("/generation/{gen_number}")
async def get_generation(gen_number: int):
     
    #Retourne la liste sommaire de tous les Pokémon d'une génération.
    #Inclut les formes spéciales (Méga, régionales, Gigantamax).
    #Ex: /pokemon/generation/5
     
    if gen_number not in GENERATIONS:
        raise HTTPException(400, f"Génération {gen_number} non supportée. Valeurs valides : 5-9")

    id_range = GENERATIONS[gen_number]
    forms    = SPECIAL_FORMS.get(gen_number, [])

    async with httpx.AsyncClient() as client:
        # Liste de base depuis PokéAPI (noms + URLs)
        r = await client.get(
            f"{POKEAPI}/generation/{gen_number}",
            timeout=10
        )
        r.raise_for_status()
        data = r.json()

    pokemon_list = [
        {"name": p["name"], "url": p["url"]}
        for p in data["pokemon_species"]
    ]

    # Ajouter les formes spéciales
    for form_name in forms:
        pokemon_list.append({"name": form_name, "url": None, "is_special_form": True})

    return {
        "generation": gen_number,
        "count":      len(pokemon_list),
        "pokemon":    sorted(pokemon_list, key=lambda x: x["name"]),
    }


@router.get("/type/{type_name}")
async def get_by_type(type_name: str):
     
    #Retourne tous les Pokémon d'un type donné.
    #Ex: /pokemon/type/dragon
     
    async with httpx.AsyncClient() as client:
        r = await client.get(f"{POKEAPI}/type/{type_name}", timeout=10)
        if r.status_code == 404:
            raise HTTPException(404, f"Type '{type_name}' introuvable")
        r.raise_for_status()
        data = r.json()

    pokemon = [
        {"name": p["pokemon"]["name"], "url": p["pokemon"]["url"]}
        for p in data["pokemon"]
    ]

    return {
        "type":    type_name,
        "count":   len(pokemon),
        "pokemon": pokemon,
    }