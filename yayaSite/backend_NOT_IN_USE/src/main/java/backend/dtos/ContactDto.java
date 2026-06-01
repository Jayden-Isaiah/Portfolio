package backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;


public class ContactDto {

    /**
     * Données reçues du formulaire frontend (entrée).
     * @NotBlank = ne peut pas être null ou vide
     * @Email = doit être un format email valide
     */
    public record Request(
        @NotBlank(message = "Le nom est requis")
        String name,

        @Email(message = "Format email invalide")
        @NotBlank(message = "L'email est requis")
        String email,

        @NotBlank(message = "Le sujet est requis")
        String subject,

        @NotBlank(message = "Le message est requis")
        String message
    ) {}

    /**
     * Données retournées au frontend (sortie).
     * On expose uniquement ce que le client a besoin de voir.
     */
    public record Response(
        Long id,
        String name,
        String email,
        String subject,
        String message,
        LocalDateTime createdAt,
        boolean read
    ) {}
}
