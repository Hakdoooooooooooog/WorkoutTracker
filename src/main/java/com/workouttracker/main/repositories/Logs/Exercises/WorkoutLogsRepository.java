package com.workouttracker.main.repositories.Logs.Exercises;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workouttracker.main.entities.Logs.Excercises.WorkoutLogsEntity;

@Repository
public interface WorkoutLogsRepository extends JpaRepository<WorkoutLogsEntity, UUID> {

}
