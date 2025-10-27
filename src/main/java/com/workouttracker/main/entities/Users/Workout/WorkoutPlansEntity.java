package com.workouttracker.main.entities.Users.Workout;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.workouttracker.main.entities.Excercises.WorkoutPlanExercisesEntity;
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
@Table(name = "workout_plans")
public class WorkoutPlansEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "plan_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private UsersEntity user;

    @Column(name = "plan_name", nullable = false, length = 255)
    private String planName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal", length = 50)
    private WorkoutGoal goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 20)
    private DifficultyLevel difficultyLevel;

    @Column(name = "duration_weeks")
    private Integer durationWeeks; // How long the plan should last

    @Column(name = "sessions_per_week")
    private Integer sessionsPerWeek;

    @Column(name = "is_template", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isTemplate = false; // Is this a reusable template?

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    // Relationship: One workout plan has many exercises (through bridge table)
    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlanExercisesEntity> planExercises;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
        if (isTemplate == null)
            isTemplate = false;
        if (isActive == null)
            isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Enums
    public enum WorkoutGoal {
        MUSCLE_GAIN,
        FAT_LOSS,
        STRENGTH,
        ENDURANCE,
        GENERAL_FITNESS,
        ATHLETIC_PERFORMANCE,
        REHABILITATION
    }

    public enum DifficultyLevel {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED,
        EXPERT
    }
}
