package com.devops.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateDTO {
    private UserDTO userData;
    @NotEmpty(message = "old username is required")
    private String oldUsername;
}
