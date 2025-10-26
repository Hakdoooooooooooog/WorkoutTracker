package com.workouttracker.main.repositories.Logs.Users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workouttracker.main.entities.Logs.Users.UserLogsEntity;

@Repository
public interface UserLogsRepository extends JpaRepository<UserLogsEntity, UUID> {

}
