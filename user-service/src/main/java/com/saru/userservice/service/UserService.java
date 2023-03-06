package com.saru.userservice.service;

import com.saru.userservice.dto.FriendListResponse;
import com.saru.userservice.entity.User;
import com.saru.userservice.exception.UserServiceNotFoundException;
import com.saru.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void registerUser(User user) {
        userRepository.save(user);
    }


    public Boolean findUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.isPresent();
    }

    public String addFriend(Long userId, Long friendId) throws UserServiceNotFoundException {
        String message="user is already friend with the requested id";
        User user = userRepository.findById(userId).orElseThrow(() -> new UserServiceNotFoundException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserServiceNotFoundException("Friend not found"));

        User user1 = userRepository.findByUserIdAndFriendId(userId,friendId);
        if (user1 != null) {
            return message;
        } else {
            user.getFriends().add(friend);
            userRepository.save(user);

            friend.getFriends().add(user);
            userRepository.save(friend);

            return null;
        }
    }

    public List<FriendListResponse> getAllFriendsByUserId(Long userId) throws UserServiceNotFoundException {
        User user= userRepository.findByIdWithFriends(userId).orElseThrow(() -> new UserServiceNotFoundException("User not found or has no friends"));

        return user.getFriends().stream()
                .map(friend -> FriendListResponse.builder()
                        .id(friend.getId())
                        .userName(friend.getUserName())
                        .firstName(friend.getFirstName())
                        .lastName(friend.getLastName())
                        .build())
                .collect(Collectors.toList());
    }



}
