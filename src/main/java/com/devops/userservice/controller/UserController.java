package com.devops.userservice.controller;

import com.devops.userservice.dto.UpdateDTO;
import com.devops.userservice.dto.UserDTO;
import com.devops.userservice.model.User;
import com.devops.userservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(this.userService.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?>  updateUser(@Valid @RequestBody UpdateDTO dto, @RequestParam("password") String passwordHash){
        User existingUser = this.userService.findUser(dto.getOldUsername());
        if(passwordHash.equals("")) passwordHash = existingUser.getPassword();
        if(existingUser == null || existingUser.isDeleted()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        this.userService.updateUser(dto, passwordHash, existingUser);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?>  deleteUser(@RequestParam("username") String username){
        User existingUser = this.userService.findUser(username);
        if(existingUser == null || existingUser.isDeleted()) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        this.userService.deleteUser(username);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
