package com.workouttracker.main.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import com.workouttracker.main.entities.User.UsersEntity;

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
@Table(name = "workout_logs")
public class WorkoutLogsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduledWorkoutsEntity scheduledWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExercisesEntity exercise;

    @Column(name = "workout_date", nullable = false)
    private Date workoutDate;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "reps_performed", nullable = false)
    private Integer repsPerformed;

    @Column(name = "weight_used", nullable = false, precision = 10, scale = 2)
    private BigDecimal weightUsed;

    @Column(name = "weight_unit", nullable = false, length = 20)
    private String weightUnit;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "logged_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp loggedAt;
}
