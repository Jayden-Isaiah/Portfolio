package backend.controllers;

import backend.dtos.ContactDto;
import backend.services.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller = couche HTTP uniquement.
 * Son seul rôle : recevoir la requête, appeler le service, retourner la réponse.
 * Aucune logique métier ici — tout ça est dans ContactService.
 */
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // En prod : remplacer par le domaine exact du frontend
public class ContactController {

    private final ContactService contactService;

    // Injection par constructeur — meilleure pratique vs @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * POST /api/contact
     * Reçoit un message du formulaire frontend.
     * @Valid déclenche la validation des @NotBlank, @Email du DTO
     */
    @PostMapping
    public ResponseEntity<ContactDto.Response> receiveMessage(
            @Valid @RequestBody ContactDto.Request request) {

        ContactDto.Response response = contactService.save(request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * GET /api/contact
     * Tous les messages (admin).
     */
    @GetMapping
    public ResponseEntity<List<ContactDto.Response>> getAllMessages() {
        return ResponseEntity.ok(contactService.findAll());
    }

    /**
     * GET /api/contact/unread
     * Seulement les messages non lus (admin).
     */
    @GetMapping("/unread")
    public ResponseEntity<List<ContactDto.Response>> getUnreadMessages() {
        return ResponseEntity.ok(contactService.findUnread());
    }

    /**
     * PATCH /api/contact/{id}/read
     * Marque un message comme lu (admin).
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<ContactDto.Response> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.markAsRead(id));
    }
}
