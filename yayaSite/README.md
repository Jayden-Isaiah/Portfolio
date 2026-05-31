# YayaMadeThat — Portfolio Site

Site portfolio pour **Yaya** — artiste, graphic designer, directrice artistique — de l'Est de Montréal.

---

## Stack

| Couche | Technologie |
|---|---|
| Frontend | HTML · CSS · JavaScript vanilla |
| Backend | Java 21 · Spring Boot 3 |
| DB (dev) | H2 (fichier local) |
| DB (prod) | PostgreSQL |
| Hébergement frontend | Netlify (gratuit) |
| Hébergement backend | Railway (gratuit au démarrage) |

---

## Structure

```
yaya/
├── frontend/
│   ├── index.html      ← Page principale (toutes sections)
│   ├── style.css       ← Design éditorial brutaliste (noir/jaune)
│   └── app.js          ← Interactions + appels API Java
└── backend/
    ├── pom.xml
    └── src/main/java/com/yaya/portfolio/
        ├── PortfolioApplication.java
        ├── model/
        │   ├── ContactMessage.java
        │   ├── Design.java
        │   └── NotifyEmail.java
        └── controller/
            └── Controllers.java    ← /api/contact · /api/designs · /api/notify
```

---

## Lancer en local

### Frontend
Ouvre simplement `frontend/index.html` dans ton navigateur.
Ou utilise Live Server dans VS Code pour le rechargement automatique.

### Backend Java
```bash
cd backend
./mvnw spring-boot:run
```
L'API tourne sur `http://localhost:8080`
Console H2 (DB) : `http://localhost:8080/h2-console`

---

## Routes API

| Méthode | Route | Description |
|---|---|---|
| `GET` | `/api/designs` | Liste des designs du portfolio |
| `POST` | `/api/designs` | Ajouter un design (admin) |
| `DELETE` | `/api/designs/{id}` | Supprimer un design (admin) |
| `POST` | `/api/contact` | Envoyer un message de contact |
| `GET` | `/api/contact` | Lire les messages reçus (admin) |
| `POST` | `/api/notify` | S'inscrire pour la notif merch |
| `GET` | `/api/notify` | Liste des emails inscrits (admin) |

---

## Mise en ligne (gratuit)

### Frontend → Netlify
1. Crée un compte sur [netlify.com](https://netlify.com)
2. Drag & drop le dossier `frontend/` sur le dashboard Netlify
3. Ton site est en ligne en 30 secondes avec une URL gratuite
4. Pour un domaine custom : Netlify Settings → Domain Management

### Backend → Railway
1. Crée un compte sur [railway.app](https://railway.app)
2. New Project → Deploy from GitHub → sélectionne ce repo
3. Railway détecte automatiquement le `pom.xml` et déploie
4. Ajoute un plugin PostgreSQL dans Railway → copie `DATABASE_URL`
5. Dans `application.properties`, décommenter la section PostgreSQL
6. Dans `app.js`, changer `API_BASE` pour l'URL Railway

---

## Ajouter des designs
Une fois le backend lancé, utilise curl ou Postman :
```bash
curl -X POST http://localhost:8080/api/designs \
  -H "Content-Type: application/json" \
  -d '{"title":"Lookbook Été","category":"Fashion","imageUrl":"https://...","displayOrder":1}'
```

---

*Projet portfolio — Jayden-Isaiah × Yaya*
