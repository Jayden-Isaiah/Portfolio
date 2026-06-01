package backend.repositories;

import backend.models.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository = couche d'accès à la base de données.
 *
 * JpaRepository<ContactMessage, Long> nous donne GRATUITEMENT :
 *   - save(entity)       → INSERT ou UPDATE
 *   - findById(id)       → SELECT WHERE id = ?
 *   - findAll()          → SELECT *
 *   - delete(entity)     → DELETE
 *   - count()            → SELECT COUNT(*)
 *   ... et plus encore
 *
 * On peut aussi déclarer des méthodes custom — Spring génère
 * automatiquement le SQL à partir du nom de la méthode.
 */
@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    // Spring traduit "findByOrderByCreatedAtDesc" en :
    // SELECT * FROM contact_messages WHERE read = false ORDER BY created_at DESC
    List<ContactMessage> findByReadFalseOrderByCreatedAtDesc();

    // SELECT * FROM contact_messages ORDER BY created_at DESC
    List<ContactMessage> findAllByOrderByCreatedAtDesc();
}
