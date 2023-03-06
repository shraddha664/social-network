package com.saru.postservice.service;

import com.saru.postservice.dto.CommentsResponse;
import com.saru.postservice.dto.PostDto;
import com.saru.postservice.dto.PostListResponseDto;
import com.saru.postservice.entity.Post;
import com.saru.postservice.exception.PostApplicationException;
import com.saru.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final WebClient.Builder webClientBuilder;

    private final PostRepository postRepository;

    public Boolean createPost(Long userId, PostDto postRequestDto) {
        log.info("inside create post");
        Boolean userExist = webClientBuilder.build().get()
                .uri("http://user-service/api/v1/users", uriBuilder -> uriBuilder.queryParam("userId", userId).build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        log.info("After rest cliend request ");

        if (Boolean.TRUE.equals(userExist)) {
            Post post = Post.builder()
                    .userId(userId)
                    .postDescription(postRequestDto.getDescription())
                    .build();
            postRepository.save(post);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> fetchAllPostsById(Long userId) {

        List<Post> postList = postRepository.findAllPostByUserId(userId);
        List<PostListResponseDto> postListResponseDto = new ArrayList<>();

        return postList.stream().map(post1 -> PostListResponseDto.builder()
                .postId(post1.getPostId())
                .description(post1.getPostDescription())
                .build()).collect(Collectors.toList());
    }

    public void deletePostByUserIdAndPostId(Long postId, Long userId) throws PostApplicationException {
        if (((postRepository.findPostByUserIdAndPostId(userId, postId))) != null) {
            postRepository.deletePostByUserIdAndPostId(userId, postId);
        } else {
            throw new PostApplicationException("No post With post id :" + postId);
        }
    }

    public PostListResponseDto updatePostByUserIdAndPostId(Long userId, Long postId, PostDto postDto) throws PostApplicationException {
        if (((postRepository.findPostByUserIdAndPostId(userId, postId))) != null) {
            postRepository.updatePostByUserIdAndPostId(userId, postId, postDto.getDescription());
            return PostListResponseDto.builder()
                    .postId(postId)
                    .description(postDto.getDescription())
                    .build();
        } else {
            throw new PostApplicationException("No post With post id :" + postId);
        }
    }

    public Boolean checkPostIdExistOrNot(Long postId) {
        return postRepository.findById(postId).isPresent();
    }

    public List<CommentsResponse> fetchComments(Long userId, Long postId) {
        Post post = postRepository.findPostByUserIdAndPostId(userId, postId);
        if (post != null) {
           return webClientBuilder.build().get()
                    .uri("http://comment-service/api/v1/comments", uriBuilder -> uriBuilder.queryParam("postId", postId)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CommentsResponse>>() {
                    })
                    .block();
        } else {
            return Collections.emptyList();
        }
    }
}




