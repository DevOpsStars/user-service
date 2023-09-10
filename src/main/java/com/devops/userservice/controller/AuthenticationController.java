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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.devops.userservice.dto.LoginRequestDTO;
import com.devops.userservice.services.AuthenticationService;

import javax.xml.transform.OutputKeys;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

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
    public ResponseEntity<?> authorizeUser(HttpServletRequest request, Authentication authentication){

        String requestURI = request.getHeader("originalURI");

        String role = authentication.getAuthorities().iterator().next().toString().substring(5);

        List<List<String>> expectedRoles = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList("rating/api/host-ratings/new-rating/[0-9]+/[0-9]+/[1-5]", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("rating/api/host-ratings/update/[0-9]+/[1-5]", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("rating/api/host-ratings/delete/[0-9]+", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("rating/api/lodge-ratings/new-rating/[0-9]+/[0-9]+/[1-5]", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("rating/api/lodge-ratings/update/[0-9]+/[1-5]", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("rating/api/lodge-ratings/delete/[0-9]+", "ROLE_GUEST")),

                new ArrayList<>(Arrays.asList("booking/api/reservations/[0-9]+/cancel", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/[0-9]+/all-reservations", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/requests/send-request", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/requests/send-request/automatic-accept", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/[0-9]+", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/[0-9]+/cancel-count", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/lodges/active", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("booking/api/[0-9]+/accept", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("booking/api/[0-9]+/decline", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("booking/api/[0-9]+/pending", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/[0-9]+/active", "ROLE_HOST", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/logde/[0-9]+/active", "ROLE_HOST", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/lodge/[0-9]+/period/count", "ROLE_HOST", "ROLE_GUEST")),
                new ArrayList<>(Arrays.asList("booking/api/reservations/lodge/[0-9]+/period", "ROLE_HOST", "ROLE_GUEST")),

                new ArrayList<>(Arrays.asList("lodging/api/availability/update/[0-9]+", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("lodging/api/lodge/update/[0-9]+", "ROLE_HOST")),
                new ArrayList<>(Arrays.asList("lodging/api/price/update/[0-9]+", "ROLE_HOST"))
        ));

        for(List<String> path : expectedRoles){
            Pattern pattern = Pattern.compile(path.get(0), Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(requestURI);
            if(matcher.find()){
                for(int i = 1;i < path.size();++i){
                    if(role.equals(path.get(i))) return new ResponseEntity<>(null, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
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
