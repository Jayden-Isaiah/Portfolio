from sqlalchemy import create_engine
from sqlalchemy.orm import DeclarativeBase, sessionmaker

# SQLite local - facile à remplacer par PostgreSQL plus tard
# Pour PostgreSQL : "postgresql://user:password@localhost/pokedex"
DATABASE_URL = "sqlite:///./pokedex.db"

engine = create_engine(
    DATABASE_URL,
    connect_args={"check_same_thread": False}  # Requis pour SQLite avec FastAPI
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

class Base(DeclarativeBase):
    pass

# Dependency - injecté dans chaque route qui a besoin de la DB
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()