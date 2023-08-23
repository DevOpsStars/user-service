package com.devops.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginRequestDTO {
    @NotEmpty(message = "username is required")
    private String username;
    @NotEmpty(message = "password is required")
    private String password;
}
