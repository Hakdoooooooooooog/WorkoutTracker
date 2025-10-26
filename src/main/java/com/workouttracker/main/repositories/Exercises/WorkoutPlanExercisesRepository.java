package com.workouttracker.main.repositories.Exercises;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workouttracker.main.entities.Excercises.WorkoutPlanExercisesEntity;

@Repository
public interface WorkoutPlanExercisesRepository extends JpaRepository<WorkoutPlanExercisesEntity, UUID> {

}
