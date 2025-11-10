package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.model.User;
import com.dailycodework.buynowdotcom.request.CreateUserRequest;
import com.dailycodework.buynowdotcom.request.UserUpdateRequest;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("user/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(new ApiResponse("Found!", user));
    }

    @PostMapping("/add")
    public  ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        User user = userService.createUser(request);
        return ResponseEntity.ok(new ApiResponse("User created successfully!", user));
    }

    @PostMapping("/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId){
        User user = userService.updateUser(request, userId);
        return ResponseEntity.ok(new ApiResponse("User updated successfully!", user));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
    }
}
