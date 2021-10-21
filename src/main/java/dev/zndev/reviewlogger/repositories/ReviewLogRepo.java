package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.ReviewLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewLogRepo extends JpaRepository<ReviewLog, Integer> {

    List<ReviewLog>
    findAllByPersonnel_firstNameContainsOrPersonnel_lastNameContainsOrPersonnel_middleNameContainsOrPersonnel_officeContains(
            String firstName,
            String lastName,
            String middleName,
            String office,
            Pageable pageable);


    List<ReviewLog> findAllById(int id, Pageable pageable);

    List<ReviewLog> findAllByReviewerId(int id, Pageable pageable);

    List<ReviewLog> findAllByPersonnelId(int id, Pageable pageable);

    long countByReviewerId(int id);

    long countByPersonnelId(int id);

    @Query(value = "SELECT * FROM review_logs WHERE review_date >= ?1 AND review_date <= ?2", nativeQuery = true)
    List<ReviewLog> findAllBetweenDates(String dateFrom, String dateUntil);

    @Query(value = "SELECT * FROM review_logs WHERE DATE_FORMAT(review_date, \"%m-%Y\") = ?1", nativeQuery = true)
    List<ReviewLog> findAllByMonthAndYear(String monthYear);

}
