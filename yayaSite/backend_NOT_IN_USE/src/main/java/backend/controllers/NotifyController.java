package backend.controllers;

import backend.dtos.NotifyDto;
import backend.services.NotifyService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notify")
@CrossOrigin(origins = "*")
public class NotifyController {

    private final NotifyService notifyService;

    public NotifyController(NotifyService notifyService) {
        this.notifyService = notifyService;
    }

    /**
     * POST /api/notify
     * Inscrire un email pour la notification du merch.
     */
    @PostMapping
    public ResponseEntity<NotifyDto.Response> subscribe(
            @Valid @RequestBody NotifyDto.Request request) {

        return ResponseEntity.ok(notifyService.subscribe(request));
    }

    /**
     * GET /api/notify
     * Liste de tous les emails inscrits (admin).
     */
    @GetMapping
    public ResponseEntity<List<String>> getAllSubscribers() {
        return ResponseEntity.ok(notifyService.findAllEmails());
    }

    /**
     * GET /api/notify/count
     * Nombre total d'inscrits.
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCount() {
        return ResponseEntity.ok(Map.of("count", notifyService.count()));
    }
}
