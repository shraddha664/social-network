package com.saru.userservice.controller;

import com.saru.userservice.dto.FriendListResponse;
import com.saru.userservice.dto.UserRegisterDto;
import com.saru.userservice.entity.User;
import com.saru.userservice.exception.UserServiceNotFoundException;
import com.saru.userservice.service.UserService;
import com.saru.userservice.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
// todo :   private PasswordEncoder encoder;

    private final PasswordUtil passwordUtil;

    @PostMapping("/registration")
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDto userRegisterDto){
        User user= User.builder()
                .userName(userRegisterDto.getUserName())
                .firstName(userRegisterDto.getFirstName())
                .lastName(userRegisterDto.getLastName())
                .password(passwordUtil.hashPassword(userRegisterDto.getPassword()))
                .build();

        userService.registerUser(user);
        return new ResponseEntity<>("User saved succesfully", HttpStatus.CREATED);
    }

    @GetMapping
    public Boolean findUserById(@RequestParam Long userId){
       return userService.findUserById(userId);
    }


    @PostMapping("/{userId}/friend/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable("userId") Long userId,@PathVariable("friendId") Long friendId) throws UserServiceNotFoundException {

        if ((userService.addFriend(userId, friendId))!=null) {
            return new ResponseEntity<>("Both are already friends.U can either remove or unfriend if you want ", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Friend added succesfully", HttpStatus.CREATED);
        }

    }


    @GetMapping("/{userId}/friends")
    public ResponseEntity<List<FriendListResponse>> getAllFriendsByUserId(@PathVariable("userId") Long userId) throws UserServiceNotFoundException {
        return new ResponseEntity<>(userService.getAllFriendsByUserId(userId),HttpStatus.OK);
    }

}
