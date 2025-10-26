package com.workouttracker.main.repositories.Users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workouttracker.main.entities.Users.PersonalRecordsEntity;

@Repository
public interface PersonalRecordRepository extends JpaRepository<PersonalRecordsEntity, UUID> {

}
