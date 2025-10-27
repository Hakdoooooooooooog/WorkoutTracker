package com.workouttracker.main.entities.Users;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

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
@Table(name = "progress_records")
public class ProgressRecordsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "record_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false, length = 50)
    private RecordType recordType;

    @Column(name = "measurement_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal measurementValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false, length = 20)
    private MeasurementUnit unit;

    @Column(name = "measurement_date", nullable = false)
    private Timestamp measurementDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "photo_url", length = 500)
    private String photoUrl; // Progress photo URL

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Enums
    public enum RecordType {
        BODY_WEIGHT,
        BODY_FAT_PERCENTAGE,
        MUSCLE_MASS,
        CHEST_CIRCUMFERENCE,
        WAIST_CIRCUMFERENCE,
        HIP_CIRCUMFERENCE,
        ARM_CIRCUMFERENCE,
        THIGH_CIRCUMFERENCE,
        CALF_CIRCUMFERENCE,
        NECK_CIRCUMFERENCE,
        HEIGHT,
        BMI,
        BLOOD_PRESSURE_SYSTOLIC,
        BLOOD_PRESSURE_DIASTOLIC,
        RESTING_HEART_RATE,
        VO2_MAX
    }

    public enum MeasurementUnit {
        KG,
        LBS,
        CM,
        INCHES,
        PERCENTAGE,
        BPM, // Beats per minute
        MMHG, // Millimeters of mercury (blood pressure)
        ML_KG_MIN // VO2 max unit
    }
}
