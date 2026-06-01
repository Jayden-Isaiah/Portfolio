package backend.dtos;

import jakarta.validation.constraints.NotBlank;

public class DesignDto {

    /** Données pour créer ou modifier un design (entrée admin) */
    public record Request(
        @NotBlank(message = "Le titre est requis")
        String title,

        @NotBlank(message = "La catégorie est requise")
        String category,

        @NotBlank(message = "L'URL de l'image est requise")
        String imageUrl,

        String description,   // Optionnel

        int displayOrder
    ) {}

    /** Données retournées au frontend */
    public record Response(
        Long id,
        String title,
        String category,
        String imageUrl,
        String description,
        int displayOrder
    ) {}
}
