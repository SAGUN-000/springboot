package io.virinchi.hibernatetest.Repository;

import io.virinchi.hibernatetest.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
}
