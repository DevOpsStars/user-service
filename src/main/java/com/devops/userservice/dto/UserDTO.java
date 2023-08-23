package com.devops.userservice.dto;

import com.devops.userservice.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NotEmpty(message = "username is required")
    private String username;
    private String name, surname, email, address;
    @NotNull(message = "role is required")
    private String role;

    public UserDTO(User user){
        this.username = user.getUsername();
        this.surname = user.getSurname();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole().getAuthority();
    }
}
