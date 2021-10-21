package dev.zndev.reviewlogger.repositories;

import dev.zndev.reviewlogger.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<User,Integer> {

    Optional<User> findByUsernameAndPassword(String username, String password);

    List<User> findAllByUsernameContainsOrFullnameContains(String username, String fullname, Pageable pageable);

}
