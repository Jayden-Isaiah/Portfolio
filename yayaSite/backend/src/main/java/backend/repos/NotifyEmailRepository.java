package backend.repositories;

import backend.models.NotifyEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyEmailRepository extends JpaRepository<NotifyEmail, Long> {

    // Vérifie si un email existe déjà — utile pour éviter les doublons
    boolean existsByEmail(String email);
}
