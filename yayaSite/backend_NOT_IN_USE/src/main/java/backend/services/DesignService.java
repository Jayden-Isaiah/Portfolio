package backend.services;

import backend.dtos.DesignDto;
import backend.models.Design;
import backend.repositories.DesignRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DesignService {

    private final DesignRepository repository;

    public DesignService(DesignRepository repository) {
        this.repository = repository;
    }

    /** Retourne tous les designs triés par displayOrder */
    public List<DesignDto.Response> findAll() {
        return repository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /** Retourne les designs filtrés par catégorie */
    public List<DesignDto.Response> findByCategory(String category) {
        return repository.findByCategoryOrderByDisplayOrderAsc(category)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /** Crée un nouveau design */
    public DesignDto.Response save(DesignDto.Request request) {
        Design design = new Design();
        design.setTitle(request.title());
        design.setCategory(request.category());
        design.setImageUrl(request.imageUrl());
        design.setDescription(request.description());
        design.setDisplayOrder(request.displayOrder());

        return toResponse(repository.save(design));
    }

    /** Met à jour un design existant */
    public DesignDto.Response update(Long id, DesignDto.Request request) {
        Design design = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Design " + id + " introuvable"));

        design.setTitle(request.title());
        design.setCategory(request.category());
        design.setImageUrl(request.imageUrl());
        design.setDescription(request.description());
        design.setDisplayOrder(request.displayOrder());

        return toResponse(repository.save(design));
    }

    /** Supprime un design */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Design " + id + " introuvable");
        }
        repository.deleteById(id);
    }

    private DesignDto.Response toResponse(Design design) {
        return new DesignDto.Response(
            design.getId(),
            design.getTitle(),
            design.getCategory(),
            design.getImageUrl(),
            design.getDescription(),
            design.getDisplayOrder()
        );
    }
}
