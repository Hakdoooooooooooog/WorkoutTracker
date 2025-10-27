package com.workouttracker.main.entities.Excercises;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.workouttracker.main.entities.Excercises.Plan.WorkoutPlanExercisesEntity;
import com.workouttracker.main.entities.Users.UsersEntity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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
@Table(name = "exercises")
public class ExercisesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "exercise_id")
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private ExerciseCategory category;

    @Column(name = "primary_muscle_group", nullable = false, length = 100)
    private String primaryMuscleGroup;

    @Column(name = "secondary_muscle_groups", length = 255)
    private String secondaryMuscleGroups; // Comma-separated

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment", length = 50)
    private EquipmentType equipment;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 20)
    private DifficultyLevel difficultyLevel;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_custom", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isCustom = false;

    // If custom exercise, link to the user who created it
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private UsersEntity createdBy;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    // Relationship: One exercise can be in many workout plan exercises
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanExercisesEntity> workoutPlanExercises;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
        if (isCustom == null) {
            isCustom = false;
        }
    }

    // Enums for better type safety
    public enum ExerciseCategory {
        STRENGTH,
        CARDIO,
        FLEXIBILITY,
        BALANCE,
        PLYOMETRICS,
        POWERLIFTING,
        OLYMPIC_LIFTING,
        STRETCHING,
        MOBILITY
    }

    public enum EquipmentType {
        BARBELL,
        DUMBBELL,
        KETTLEBELL,
        MACHINE,
        CABLE,
        BODYWEIGHT,
        RESISTANCE_BAND,
        MEDICINE_BALL,
        TRX,
        NONE
    }

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }
}
