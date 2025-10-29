package com.workouttracker.main.dtos.Users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,20}$", message = "Username must be 3-20 characters long and contain only letters, numbers, underscores, and hyphens")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z\\s'-]{1,50}$", message = "First name can only contain letters, spaces, hyphens, and apostrophes")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z\\s'-]{1,50}$", message = "Last name can only contain letters, spaces, hyphens, and apostrophes")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;
}
