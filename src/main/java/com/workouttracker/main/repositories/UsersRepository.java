package com.workouttracker.main.repositories;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workouttracker.main.entities.Users.UsersEntity;

@Repository
public interface UsersRepository extends JpaRepository<UsersEntity, UUID> {
    Optional<UsersEntity> findByUsername(String username);

    Optional<UsersEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}