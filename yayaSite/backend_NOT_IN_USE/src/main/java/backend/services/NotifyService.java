package backend.services;

import backend.dtos.NotifyDto;
import backend.models.NotifyEmail;
import backend.repositories.NotifyEmailRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotifyService {

    private final NotifyEmailRepository repository;

    public NotifyService(NotifyEmailRepository repository) {
        this.repository = repository;
    }

    /**
     * Inscrit un email pour la notification merch.
     * Idempotent : si l'email existe déjà, on ne lève pas d'erreur.
     */
    public NotifyDto.Response subscribe(NotifyDto.Request request) {
        if (repository.existsByEmail(request.email())) {
            return new NotifyDto.Response("ok", "Déjà inscrit !");
        }

        NotifyEmail entry = new NotifyEmail();
        entry.setEmail(request.email());
        repository.save(entry);

        return new NotifyDto.Response("ok", "Inscription confirmée !");
    }

    /** Retourne tous les emails inscrits (usage admin) */
    public List<String> findAllEmails() {
        return repository.findAll()
                .stream()
                .map(NotifyEmail::getEmail)
                .toList();
    }

    /** Nombre total d'inscrits */
    public long count() {
        return repository.count();
    }
}
