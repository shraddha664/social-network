package com.saru.postservice.controller;

import com.saru.postservice.dto.PostDto;
import com.saru.postservice.exception.PostApplicationException;
import com.saru.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final WebClient.Builder webClientBuilder;
    private final PostService postService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> createPost(@PathVariable("userId") Long userId, @RequestBody PostDto postRequestDto) {
        if (postService.createPost(userId, postRequestDto).equals(Boolean.TRUE)) {
            return new ResponseEntity<>("Post created succesfully ", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> fetchAllPostsById(@PathVariable("userId") Long userId) throws PostApplicationException {
        Boolean userExist = webClientBuilder.build().get()
                .uri("http://user-service/api/v1/users", uriBuilder -> uriBuilder.queryParam("userId", userId).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (userExist.equals(Boolean.TRUE)) {
            return new ResponseEntity<>(postService.fetchAllPostsById(userId), HttpStatus.ACCEPTED);
        }else {
            throw new PostApplicationException("User not found with id:"+userId);
        }
    }

    @DeleteMapping("/{userId}/post/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable("userId") Long userId,@PathVariable("postId") Long postId) throws PostApplicationException {
        Boolean userExist=  webClientBuilder.build().get()
                .uri("http://user-service/api/v1/users",uriBuilder -> uriBuilder.queryParam("userId",userId).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (userExist.equals(Boolean.TRUE)) {
            log.info("inside if block of controller");
            postService.deletePostByUserIdAndPostId(postId, userId);
            return new ResponseEntity<>("Post deleted succesfully",HttpStatus.OK);
        }else {
            throw new PostApplicationException("User not found with id:"+userId);
        }
    }

    @PutMapping("/{userId}/post/{postId}")
    public ResponseEntity<Object> updatePostByUserIdAndPostId(@PathVariable("userId") Long userId,@PathVariable("postId") Long postId,@RequestBody PostDto postDto) throws PostApplicationException {
        Boolean userExist=  webClientBuilder.build().get()
                .uri("http://user-service/api/v1/users",uriBuilder -> uriBuilder.queryParam("userId",userId).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (userExist.equals(Boolean.TRUE)) {
            return new ResponseEntity<>( postService.updatePostByUserIdAndPostId(userId,postId,postDto),HttpStatus.OK);
        }else {
            throw new PostApplicationException("User not found with id:"+userId);
        }
    }

//    todo:fetch the post along with all the comments



}
