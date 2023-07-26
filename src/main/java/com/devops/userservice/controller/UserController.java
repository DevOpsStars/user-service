package com.devops.userservice.controller;

import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.services.UserService;
import jakarta.validation.Constraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    /* this method expects user information inside dto, and it also expects an already hashed password */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO dto, @RequestParam("password") String passwordHash){
        if(this.userService.findUser(dto.getUsername()) != null) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        this.userService.registerUser(dto, passwordHash);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(this.userService.findAllUsers(), HttpStatus.OK);
    }
}
