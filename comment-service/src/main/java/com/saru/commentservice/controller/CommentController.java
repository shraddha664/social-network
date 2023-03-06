package com.saru.commentservice.controller;

import com.saru.commentservice.dto.CommentRequestDto;
import com.saru.commentservice.dto.CommentsResponse;
import com.saru.commentservice.exception.CommentServiceException;
import com.saru.commentservice.repository.CommentRepository;
import com.saru.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@EnableDiscoveryClient
@RequiredArgsConstructor
public class CommentController {

    private final WebClient.Builder builder;

    private final CommentService commentService;

    private final CommentRepository commentRepository;


    @PostMapping("/user/{userId}/post/{postId}")
    public ResponseEntity<Object> postCommentByUserIdAndPostId(@PathVariable("userId") Long userId, @PathVariable("postId") Long postId, @RequestBody CommentRequestDto comment) throws CommentServiceException {

        if ((builder.build().get()
                .uri("http://user-service/api/v1/users", uriBuilder -> uriBuilder.queryParam("userId", userId).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block()).equals(Boolean.TRUE)){
            if ((builder.build().get()
                    .uri("http://post-service/api/v1/posts",uriBuilder -> uriBuilder.queryParam("postId",postId).build()).retrieve().bodyToMono(Boolean.class).block()).equals(Boolean.TRUE)){
                commentService.postCommentByUserIdAndPostId(userId,postId,comment);
                return new ResponseEntity<>("Comment posted succesfully", HttpStatus.OK);
            }else {
                throw new CommentServiceException("Post not found");
            }
        }else {
            throw new CommentServiceException("User not found");
        }
    }


    @PatchMapping("/user/{userId}/comment/{commentId}")
    public ResponseEntity<Object> updateComment( @PathVariable("commentId") Long commentId,@PathVariable("userId") Long userId, @RequestBody CommentRequestDto comment) throws CommentServiceException {
            commentService.updateComment( userId,commentId,comment);
            return new ResponseEntity<>("Comment updated succesfully",HttpStatus.OK);
    }

    @GetMapping
    public List<CommentsResponse> getComments(@RequestParam("postId")Long postId){
        return commentService.getComments(postId);
    }

}
