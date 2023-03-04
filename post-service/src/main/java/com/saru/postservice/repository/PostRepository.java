package com.saru.postservice.repository;

import com.saru.postservice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("Select p from Post p where p.userId=:userId ")
    List<Post> findAllPostByUserId(@Param("userId") Long userId);

    @Query("Select p from Post p where p.userId=:userId and p.postId=:postId")
    Post findPostByUserIdAndPostId(@Param("userId") Long userId,@Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query("Delete from Post p where p.userId=:userId And p.postId=:postId")
    void deletePostByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);

    @Transactional
    @Modifying
    @Query("Update Post p set p.postDescription=:post where p.userId=:userId And p.postId=:postId ")
    void updatePostByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId,@Param("post") String updatedPostDesc);
}
