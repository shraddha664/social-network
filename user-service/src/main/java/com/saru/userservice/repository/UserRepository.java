package com.saru.userservice.repository;

import com.saru.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u JOIN u.friends f WHERE u.id = :userId AND f.id = :friendId")
    User findByUserIdAndFriendId(@Param("userId") Long userId,@Param("friendId") Long friendId);


    @Query("SELECT u FROM User u LEFT JOIN FETCH u.friends f WHERE u.id = :id")
    Optional<User> findByIdWithFriends(@Param("id") Long userId);
}
