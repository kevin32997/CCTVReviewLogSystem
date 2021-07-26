package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.ReviewLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewLogRepo extends JpaRepository<ReviewLog,Integer> {


    List<ReviewLog> findByIdOrPersonnel_firstNameContainsOrPersonnel_lastNameContainsOrPersonnel_middleNameContains(
            String id,
            String firstName,
            String lastName,
            String middleName,
            Pageable pageable);

    List<ReviewLog> findAllByReviewerId(int id, Pageable pageable);
    List<ReviewLog> findAllByPersonnelId(int id, Pageable pageable);

    long countByReviewerId(int id);
    long countByPersonnelId(int id);
}
