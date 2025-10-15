package com.workouttracker.main.entities;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "workout_plan_exercises", uniqueConstraints = @UniqueConstraint(columnNames = { "plan_id",
        "exercise_id" }))
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

    @Column(name = "sets", nullable = false)
    private Integer sets;

    @Column(name = "reps", nullable = false, length = 50)
    private String reps;

    @Column(name = "weight_unit", length = 20)
    private String weightUnit;

    @Column(name = "initial_weight", precision = 10, scale = 2)
    private BigDecimal initialWeight;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
