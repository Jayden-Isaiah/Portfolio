package backend.controllers;

import backend.dtos.DesignDto;
import backend.services.DesignService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/designs")
@CrossOrigin(origins = "*")
public class DesignController {

    private final DesignService designService;

    public DesignController(DesignService designService) {
        this.designService = designService;
    }

    /**
     * GET /api/designs
     * Tous les designs, triés par displayOrder.
     * Appelé par le frontend au chargement de la page.
     */
    @GetMapping
    public ResponseEntity<List<DesignDto.Response>> getAllDesigns() {
        return ResponseEntity.ok(designService.findAll());
    }

    /**
     * GET /api/designs?category=Fashion
     * Filtrer par catégorie (optionnel).
     */
    @GetMapping(params = "category")
    public ResponseEntity<List<DesignDto.Response>> getByCategory(
            @RequestParam String category) {
        return ResponseEntity.ok(designService.findByCategory(category));
    }

    /**
     * POST /api/designs
     * Ajouter un design (admin).
     */
    @PostMapping
    public ResponseEntity<DesignDto.Response> createDesign(
            @Valid @RequestBody DesignDto.Request request) {

        DesignDto.Response response = designService.save(request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * PUT /api/designs/{id}
     * Modifier un design existant (admin).
     */
    @PutMapping("/{id}")
    public ResponseEntity<DesignDto.Response> updateDesign(
            @PathVariable Long id,
            @Valid @RequestBody DesignDto.Request request) {

        return ResponseEntity.ok(designService.update(id, request));
    }

    /**
     * DELETE /api/designs/{id}
     * Supprimer un design (admin).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesign(@PathVariable Long id) {
        designService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
