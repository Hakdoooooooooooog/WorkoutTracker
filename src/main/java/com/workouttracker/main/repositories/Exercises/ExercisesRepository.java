package com.workouttracker.main.repositories.Exercises;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workouttracker.main.entities.Excercises.ExercisesEntity;

public interface ExercisesRepository extends JpaRepository<ExercisesEntity, UUID> {
}