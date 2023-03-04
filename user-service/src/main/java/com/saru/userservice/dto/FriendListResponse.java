package com.saru.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendListResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
}
