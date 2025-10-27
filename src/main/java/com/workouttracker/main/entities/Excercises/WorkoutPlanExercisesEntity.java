package com.workouttracker.main.entities.Excercises;

import java.util.UUID;

import com.workouttracker.main.entities.Users.Workout.WorkoutPlansEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "workout_plan_exercises")
public class WorkoutPlanExercisesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "plan_exercise_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlansEntity workoutPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExercisesEntity exercise;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex; // Order of exercises in the workout plan

    @Column(name = "target_sets", nullable = false)
    private Integer targetSets;

    @Column(name = "target_reps", nullable = false)
    private Integer targetReps;

    @Column(name = "target_weight")
    private Double targetWeight; // Optional target weight

    @Column(name = "rest_seconds")
    private Integer restSeconds; // Rest time between sets

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Exercise-specific notes (e.g., "Focus on form", "Add drop sets")

    @Column(name = "is_superset", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSuperset = false;

    @Column(name = "superset_group")
    private Integer supersetGroup; // Group exercises that should be performed as a superset
}
