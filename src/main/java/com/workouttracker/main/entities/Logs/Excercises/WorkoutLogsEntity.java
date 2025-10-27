package com.workouttracker.main.entities.Logs.Excercises;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.workouttracker.main.entities.Excercises.ExercisesEntity;
import com.workouttracker.main.entities.Users.UsersEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_schedule_id")
    private ScheduledWorkoutsEntity scheduledWorkout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_exercise_id", nullable = false)
    private ExercisesEntity exercise;

    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "reps_performed", nullable = false)
    private Integer repsPerformed;

    @Column(name = "weight_used", precision = 10, scale = 2)
    private BigDecimal weightUsed;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_unit", length = 10)
    private WeightUnit weightUnit;

    @Column(name = "duration_seconds")
    private Integer durationSeconds; // For time-based exercises (planks, running, etc.)

    @Column(name = "distance_meters")
    private Double distanceMeters; // For distance-based exercises (running, cycling)

    @Column(name = "rpe")
    private Integer rpe; // Rate of Perceived Exertion (1-10)

    @Column(name = "rest_seconds")
    private Integer restSeconds; // Rest taken after this set

    @Column(name = "is_warmup", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isWarmup = false;

    @Column(name = "is_drop_set", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDropSet = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "logged_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp loggedAt;

    @PrePersist
    protected void onCreate() {
        loggedAt = new Timestamp(System.currentTimeMillis());
        if (isWarmup == null)
            isWarmup = false;
        if (isDropSet == null)
            isDropSet = false;
    }

    // Enum for weight units
    public enum WeightUnit {
        KG,
        LBS,
        BODYWEIGHT
    }
}
