package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.Personnel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonnelRepo extends JpaRepository<Personnel, Integer> {

    // Search
    List<Personnel>
    findByfirstNameContainsOrMiddleNameContainsOrLastNameContainsOrOfficeContains
    (String firstName, String middleName, String lastName,String office, Pageable pageable);
}
