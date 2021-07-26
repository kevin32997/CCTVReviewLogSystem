package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentRepo extends JpaRepository<Incident,Integer> {

    List<Incident> findByReviewLogId(int id);

    long countByReviewLogId(int id);
}
