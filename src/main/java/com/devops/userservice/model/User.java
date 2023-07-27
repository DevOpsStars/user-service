package com.devops.userservice.model;

import com.devops.userservice.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;
    @Column
    private String surname;
    @Column
    private String email;
    @Column
    private String address;

    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean deleted;

    public User(UserDTO dto){
        this.username = dto.getUsername();
        this.surname = dto.getSurname();
        this.address = dto.getAddress();
        this.email = dto.getEmail();
        this.name = dto.getName();

        switch (dto.getRole()){
            case "host":
                this.role = Role.HOST;
                break;
            case "guest":
                this.role = Role.GUEST;
                break;
            default:
                this.role = Role.NO_ROLE;
        }

        this.deleted = false;
    }
}
