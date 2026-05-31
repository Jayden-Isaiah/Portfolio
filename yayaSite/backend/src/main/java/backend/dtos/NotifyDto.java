package backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotifyDto {

    /** Email entrant pour inscription merch */
    public record Request(
        @Email(message = "Format email invalide")
        @NotBlank(message = "L'email est requis")
        String email
    ) {}

    /** Réponse simple */
    public record Response(
        String status,
        String message
    ) {}
}
