package com.devops.userservice.services;

import com.devops.userservice.dto.UpdateDTO;
import com.devops.userservice.model.User;
import com.devops.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String username){
        return this.userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAllUsers(){
        return this.userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.findUser(username);
    }

    public void updateUser(UpdateDTO dto, String newPasswordHashed, User existingUser){
        existingUser.setPassword(newPasswordHashed);
        existingUser.setUsername(dto.getUserData().getUsername());
        existingUser.setName(dto.getUserData().getName());
        existingUser.setSurname(dto.getUserData().getSurname());
        existingUser.setAddress(dto.getUserData().getAddress());
        existingUser.setEmail(dto.getUserData().getEmail());

        this.userRepository.save(existingUser);
    }
}
