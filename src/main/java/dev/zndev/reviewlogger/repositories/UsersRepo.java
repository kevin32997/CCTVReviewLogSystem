package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<User,Integer> {

    Optional<User> findByUsernameAndPassword(String username, String password);

}
