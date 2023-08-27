package com.devops.userservice.services;


import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.model.User;
import com.devops.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;


    @Test
    void givenValidNewUserData_whenDataSentForRegistration_thenUserHasPassedData(){
        //given
        UserDTO dto = new UserDTO();
        dto.setRole("host");
        dto.setUsername("newhost1");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("johndoe@email.com");

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User user = this.userService.registerUser(dto, "passwordHash");

        //then
        assertThat(user.getPassword(), is("passwordHash"));
        assertThat(user.getUsername(), is(dto.getUsername()));
        assertThat(user.getRole().toString(), is(dto.getRole()));
    }

    @Test
    void givenNewUserMissingRole_whenDataSentForRegistration_thenNullPointerExceptionThrown(){
        //given
        UserDTO dto = new UserDTO();
        dto.setUsername("newhost1");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("johndoe@email.com");

        assertThrows(NullPointerException.class, () -> {this.userService.registerUser(dto, "passwordHash");});
    }
}
