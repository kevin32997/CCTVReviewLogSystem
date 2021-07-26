package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.others.SystemData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemDataRepo extends JpaRepository<SystemData,String> {
}
