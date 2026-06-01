package backend.repositories;

import backend.models.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {

    // SELECT * FROM designs ORDER BY display_order ASC
    List<Design> findAllByOrderByDisplayOrderAsc();

    // SELECT * FROM designs WHERE category = ?
    List<Design> findByCategoryOrderByDisplayOrderAsc(String category);
}
