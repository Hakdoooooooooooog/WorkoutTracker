package com.workouttracker.main.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.workouttracker.main.dtos.Users.LoginRequest;
import com.workouttracker.main.dtos.Users.UsersDto;
import com.workouttracker.main.entities.Users.UsersEntity;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    // Maps entity to DTO - only maps fields that exist in both
    UsersDto toDto(UsersEntity user);

    LoginRequest toLoginRequest(UsersEntity user);

    // Maps DTO to entity - ignores fields that should not be set from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "customExercises", ignore = true)
    @Mapping(target = "personalRecords", ignore = true)
    @Mapping(target = "progressRecords", ignore = true)
    @Mapping(target = "scheduledWorkouts", ignore = true)
    @Mapping(target = "workoutLogs", ignore = true)
    @Mapping(target = "workoutPlans", ignore = true)
    @Mapping(target = "permission", ignore = true)
    UsersEntity toEntity(UsersDto dto);

    // Updates existing entity from another entity - ignores immutable fields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UsersEntity source, @MappingTarget UsersEntity target);
}
