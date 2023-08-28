package com.devops.userservice.services;

import com.devops.userservice.dto.UpdateDTO;
import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.model.Role;
import com.devops.userservice.model.User;
import com.devops.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService;
    @Test
    void givenValidNewUserData_whenDataSentForUpdate_thenUserGetsUpdated(){

        User user = new User();
        user.setUsername("username1");
        user.setPassword("password");
        user.setAddress("adress1");
        user.setName("name");
        user.setSurname("surname");
        user.setRole(new Role("ROLE_GUEST"));

        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDTO dto = new UserDTO();
        dto.setRole("ROLE_GUEST");
        dto.setUsername("newusername1");
        dto.setName("John");
        dto.setSurname("Doe");
        dto.setEmail("johndoe@email.com");

        UpdateDTO updateDTO = new UpdateDTO(dto, "username1");

        this.userService.updateUser(updateDTO, "newpassword", user);

        assertThat(user.getUsername(), is(dto.getUsername()));
        assertThat(user.getName(), is(dto.getName()));
    }
}
