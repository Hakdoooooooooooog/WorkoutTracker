package com.workouttracker.main.repositories.Exercises.Plan;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workouttracker.main.entities.Excercises.Plan.WorkoutPlanExercisesEntity;

@Repository
public interface WorkoutPlanExercisesRepository extends JpaRepository<WorkoutPlanExercisesEntity, UUID> {

}
