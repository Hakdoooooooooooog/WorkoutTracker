package com.workouttracker.main.entities.Logs.Excercises;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.workouttracker.main.entities.Users.UsersEntity;
import com.workouttracker.main.entities.Users.Workout.WorkoutPlansEntity;

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
@Table(name = "scheduled_workouts")
public class ScheduledWorkoutsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "schedule_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private WorkoutPlansEntity workoutPlan;

    @Column(name = "scheduled_date", nullable = false)
    private Timestamp scheduledDate;

    @Column(name = "start_time")
    private Timestamp startTime; // When workout actually started

    @Column(name = "end_time")
    private Timestamp endTime; // When workout actually ended

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private WorkoutStatus status = WorkoutStatus.SCHEDULED;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Pre-workout or post-workout notes

    @Column(name = "rating")
    private Integer rating; // 1-5 stars for how the workout went

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    // Relationship: One scheduled workout can have many workout logs
    @OneToMany(mappedBy = "scheduledWorkout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutLogsEntity> workoutLogs;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
        updatedAt = now;
        if (status == null) {
            status = WorkoutStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // Enum for workout status
    public enum WorkoutStatus {
        SCHEDULED, // Planned but not started
        IN_PROGRESS, // Currently working out
        COMPLETED, // Finished
        SKIPPED, // Intentionally skipped
        MISSED // Missed without rescheduling
    }
}
