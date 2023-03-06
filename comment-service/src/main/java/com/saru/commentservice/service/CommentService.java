package com.saru.commentservice.service;

import com.saru.commentservice.dto.CommentRequestDto;
import com.saru.commentservice.dto.CommentsResponse;
import com.saru.commentservice.entity.Comment;
import com.saru.commentservice.exception.CommentServiceException;
import com.saru.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    public void postCommentByUserIdAndPostId(Long userId, Long postId, CommentRequestDto comment) throws CommentServiceException {
        if (comment.getContent().isBlank()){
            throw new CommentServiceException("Comment cannot be empty");

       }else {
            commentRepository.save(Comment.builder().content(comment.getContent())
                    .postId(postId)
                    .userId(userId)
                    .build());
        }
    }

    public void updateComment(Long userId,Long commentId, CommentRequestDto comment) throws CommentServiceException {

        int rowsUpdated=commentRepository.updateCommentByCommentId(userId,commentId, comment.getContent());

        if (rowsUpdated == 0) {
            throw new CommentServiceException("Comment not found");
        }
    }

    public List<CommentsResponse> getComments(Long postId) {
        List<Comment>comments= commentRepository.findCommentsByPostId(postId);
        return comments.stream().map(comment -> CommentsResponse.builder()
                        .content(comment.getContent())
                        .commentId(comment.getCommentId()).build())
                .collect(Collectors.toList());
    }
}
