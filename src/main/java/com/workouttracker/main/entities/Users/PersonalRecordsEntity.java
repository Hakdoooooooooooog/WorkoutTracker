package com.workouttracker.main.entities.Users;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.workouttracker.main.entities.Excercises.ExercisesEntity;

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
import jakarta.persistence.PreUpdate;
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
@Table(name = "personal_records")
public class PersonalRecordsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pr_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExercisesEntity exercise;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false, length = 20)
    private PRType recordType;

    @Column(name = "weight_value", precision = 10, scale = 2)
    private BigDecimal weightValue; // For strength PRs

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_unit", length = 10)
    private WeightUnit weightUnit;

    @Column(name = "reps")
    private Integer reps; // Number of reps for the PR

    @Column(name = "time_seconds")
    private Integer timeSeconds; // For time-based PRs (e.g., best plank time)

    @Column(name = "distance_meters")
    private Double distanceMeters; // For distance PRs (e.g., longest run)

    @Column(name = "achieved_date", nullable = false)
    private Timestamp achievedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "video_url", length = 500)
    private String videoUrl; // URL to video proof of PR

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Enums
    public enum PRType {
        ONE_REP_MAX, // 1RM
        THREE_REP_MAX, // 3RM
        FIVE_REP_MAX, // 5RM
        TEN_REP_MAX, // 10RM
        MAX_REPS, // Maximum reps at bodyweight
        MAX_TIME, // Longest time held (planks, wall sits)
        MAX_DISTANCE, // Longest distance (running, rowing)
        FASTEST_TIME, // Fastest time for a distance
        HEAVIEST_VOLUME // Highest total volume (weight x reps x sets)
    }

    public enum WeightUnit {
        KG,
        LBS,
        BODYWEIGHT
    }
}
