package com.devops.userservice.services;


import com.devops.userservice.dto.LoginDTO;
import com.devops.userservice.dto.LoginRequestDTO;
import com.devops.userservice.dto.UpdateDTO;
import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.model.Role;
import com.devops.userservice.model.User;
import com.devops.userservice.repository.RoleRepository;
import com.devops.userservice.repository.UserRepository;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    TokenService tokenService;
    @InjectMocks
    AuthenticationService authService;


    @Test
    void givenValidNewUserData_whenDataSentForRegistration_thenUserHasPassedData(){
        UserDTO dto = new UserDTO();
        dto.setRole("ROLE_GUEST");
        dto.setUsername("newhost1");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("johndoe@email.com");

        Role role = new Role(1, "ROLE_GUEST");

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(roleRepository.findByAuthority("ROLE_GUEST")).thenReturn(Optional.of(role));

        User user = this.authService.registerUser(dto, "passwordHash");

        //then
        assertThat(user.getPassword(), is("passwordHash"));
        assertThat(user.getUsername(), is(dto.getUsername()));
        assertThat(user.getRole().getAuthority(), is(dto.getRole()));
    }

    @Test
    void givenNewUserMissingRole_whenDataSentForRegistration_thenNullPointerExceptionThrown(){
        UserDTO dto = new UserDTO();
        dto.setUsername("newhost1");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("johndoe@email.com");

        assertThrows(NoSuchElementException.class, () -> {this.authService.registerUser(dto, "passwordHash");});
    }

    @Test
    void givenExistingUserCredentials_whenDataSentForLogin_thenReturnsUserDTOWithJWT(){
        String username = "newhost1";
        String password = "passwordHash";

        User user = new User();
        user.setRole(new Role(3, "ROLE_GUEST"));
        user.setUsername(username);
        user.setEmail("someemail@email.com");
        user.setDeleted(false);
        user.setSurname("surname");
        user.setName("name");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        LoginDTO response = this.authService.loginUser(username, password);

        assertThat(response.getUser().getUsername(), is(user.getUsername()));
        assertThat(response.getUser().getRole(), is(user.getRole().getAuthority()));
    }

    @Test
    void givenBadUserCredentials_whenDataSentForLogin_thenReturnsNull(){
        User user = new User();
        user.setRole(new Role(3, "ROLE_GUEST"));
        user.setUsername("existinguser");
        user.setEmail("someemail@email.com");
        user.setDeleted(false);
        user.setSurname("surname");
        user.setName("name");
        user.setPassword("hashedpassword");

        lenient().when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(user));

        assertThrows(NoSuchElementException.class, () -> {this.authService.loginUser("nonExistingUser", "1234123");});
    }
}
