package com.dailycodework.buynowdotcom.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CreateUserRequest {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
