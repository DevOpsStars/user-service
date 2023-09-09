package com.devops.userservice.controller;

import com.devops.userservice.dto.LoginDTO;
import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.model.User;
import com.devops.userservice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.devops.userservice.dto.LoginRequestDTO;
import com.devops.userservice.services.AuthenticationService;

import javax.xml.transform.OutputKeys;

@Controller
@Validated
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthenticationController {


    private AuthenticationService authenticationService;
    private UserService userService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> authenticateUser(HttpServletRequest request){
        System.out.println(request);
        System.out.println(request.getHeaderNames());
        System.out.println(request.getPathInfo());
        System.out.println(request.getPathTranslated());
        System.out.println(request.getPathTranslated());

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /* this method expects user information inside dto, and it also expects an already hashed password */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO dto, @RequestParam("password") String passwordHash){
        if(passwordHash.equals("")) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        User existingUser = this.userService.findUser(dto.getUsername());
        if(existingUser != null) return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

        this.authenticationService.registerUser(dto, passwordHash);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?>  loginUser(@Valid @RequestBody LoginRequestDTO body){
        return new ResponseEntity<>(authenticationService.loginUser(body.getUsername(), body.getPassword()), HttpStatus.OK);
    }
}
