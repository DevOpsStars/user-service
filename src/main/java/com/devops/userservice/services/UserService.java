package com.devops.userservice.services;

import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.model.User;
import com.devops.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(UserDTO dto, String passwordHash){
        User newUser = new User(dto);
        newUser.setPassword(passwordHash);
        return this.userRepository.save(newUser);
    }

    public User findUser(String username){
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAllUsers(){
        return this.userRepository.findAll();
    }
}
