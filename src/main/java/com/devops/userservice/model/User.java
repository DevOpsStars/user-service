package com.devops.userservice.model;

import com.devops.userservice.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

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

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinTable(
            name="user_role_junction",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private Role role;

    @Column(nullable = false)
    private boolean deleted;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(this.role);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(UserDTO dto){
        this.username = dto.getUsername();
        this.name = dto.getName();
        this.surname = dto.getSurname();
        this.address = dto.getAddress();
        this.email = dto.getEmail();
        this.role = new Role(dto.getRole());
    }
}
