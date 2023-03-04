package com.saru.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String userName;

    @NonNull
    private String password;

    @ManyToMany
    @JoinTable(name = "friends",
            joinColumns =@JoinColumn(name = "user_Id"),
            inverseJoinColumns =@JoinColumn(name = "friend_Id")
    )
    private List<User> friends=new ArrayList<>();
}
