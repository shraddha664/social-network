package com.saru.commentservice.repository;

import com.saru.commentservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {


    @Transactional
    @Modifying
    @Query("Update Comment c SET c.content=:content WHERE c.userId=:userId AND c.commentId=:commentId")
    int updateCommentByCommentId(@Param("userId") Long userId, @Param("commentId") Long commentId, @Param("content") String content);

    @Query("Select c From Comment c Where c.postId=:postId")
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);
}
