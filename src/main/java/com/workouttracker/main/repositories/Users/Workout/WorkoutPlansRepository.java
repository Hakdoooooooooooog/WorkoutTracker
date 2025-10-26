package com.workouttracker.main.repositories.Users.Workout;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workouttracker.main.entities.Users.Workout.WorkoutPlansEntity;

public interface WorkoutPlansRepository extends JpaRepository<WorkoutPlansEntity, UUID> {

}
