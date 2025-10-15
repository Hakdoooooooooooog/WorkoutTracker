package com.workouttracker.main.dtos.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersDto {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
