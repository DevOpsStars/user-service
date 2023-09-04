package com.devops.userservice.services;


import com.devops.userservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devops.userservice.model.User;
import com.devops.userservice.dto.LoginDTO;
import com.devops.userservice.model.Role;
import com.devops.userservice.repository.RoleRepository;
import com.devops.userservice.repository.UserRepository;

@Service
@Transactional
public class AuthenticationService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public User registerUser(UserDTO dto, String passwordHash){
        User user = new User(dto);
        user.setPassword(passwordHash);
        Role userRole = roleRepository.findByAuthority(user.getRole().getAuthority()).get();
        user.setRole(userRole);
        return userRepository.save(user);
    }

    public LoginDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            User user = userRepository.findByUsername(username).get();
            if(user.isDeleted()) return new LoginDTO(null, "");

            return new LoginDTO(new UserDTO(user), token);

        } catch(AuthenticationException e){
            return new LoginDTO(null, "");
        }
    }

}
