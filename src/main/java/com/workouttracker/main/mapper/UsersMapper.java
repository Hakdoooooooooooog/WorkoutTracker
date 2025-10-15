package com.workouttracker.main.mapper;

import org.mapstruct.Mapper;

import com.workouttracker.main.dtos.UsersDto;
import com.workouttracker.main.entities.UsersEntity;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersDto toDto(UsersEntity user);

    UsersEntity toEntity(UsersDto dto);

    void updateEntityFromDto(UsersEntity user, UsersEntity existingUser);
}
