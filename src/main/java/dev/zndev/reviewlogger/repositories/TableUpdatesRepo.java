package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.others.TableUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableUpdatesRepo extends JpaRepository<TableUpdate,String> {

}
