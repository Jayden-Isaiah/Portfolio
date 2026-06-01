package backend.services;

import backend.dtos.ContactDto;
import backend.models.ContactMessage;
import backend.repositories.ContactMessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service = couche de logique métier.
 *
 * Le controller reçoit la requête HTTP et appelle le service.
 * Le service fait le travail réel : valider, transformer, appeler le repository.
 * Le repository parle à la base de données.
 *
 * Avantage : si on veut changer la logique (ex: envoyer un email à Yaya
 * quand un message arrive), on le fait ici — pas dans le controller.
 */
@Service
public class ContactService {

    // Injection de dépendance — Spring fournit automatiquement le repository
    private final ContactMessageRepository repository;

    public ContactService(ContactMessageRepository repository) {
        this.repository = repository;
    }

    /** Sauvegarde un nouveau message et retourne le DTO de réponse */
    public ContactDto.Response save(ContactDto.Request request) {
        // 1. Créer l'entité à partir du DTO
        ContactMessage message = new ContactMessage();
        message.setName(request.name());
        message.setEmail(request.email());
        message.setSubject(request.subject());
        message.setMessage(request.message());

        // 2. Sauvegarder en base
        ContactMessage saved = repository.save(message);

        // 3. Convertir en DTO et retourner (on n'expose pas l'entité directement)
        return toResponse(saved);
    }

    /** Retourne tous les messages triés par date décroissante */
    public List<ContactDto.Response> findAll() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /** Retourne uniquement les messages non lus */
    public List<ContactDto.Response> findUnread() {
        return repository.findByReadFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /** Marque un message comme lu */
    public ContactDto.Response markAsRead(Long id) {
        ContactMessage message = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Message " + id + " introuvable"));

        message.setRead(true);
        return toResponse(repository.save(message));
    }

    /** Convertit une entité JPA en DTO de réponse */
    private ContactDto.Response toResponse(ContactMessage msg) {
        return new ContactDto.Response(
            msg.getId(),
            msg.getName(),
            msg.getEmail(),
            msg.getSubject(),
            msg.getMessage(),
            msg.getCreatedAt(),
            msg.isRead()
        );
    }
}
