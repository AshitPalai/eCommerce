package com.example.user.controller;


import com.example.user.dto.ApiResponse;
import com.example.user.dto.CreateUserRequest;
import com.example.user.dto.UpdateUserRequest;
import com.example.user.dto.UserResponse;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse createdUser = userService.createUser(request);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User created successfully")
                .data(createdUser)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {

        UserResponse user = userService.getUserById(id);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched successfully")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(users)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserResponse updatedUser = userService.updateUser(id, request);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User updated successfully")
                .data(updatedUser)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(
            @RequestParam String email) {

        UserResponse user = userService.getUserByEmail(email);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched successfully")
                .data(user)
                .build();

        return ResponseEntity.ok(response);
    }


//    @GetMapping("/debug")
//    public ResponseEntity<?> getUsers() {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        System.out.println("Auth: " + auth);
//        System.out.println("Authorities: " + auth.getAuthorities());
//
//        return ResponseEntity.ok("Working");
//    }

    @GetMapping("/admin/data")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Only admin can access";
    }

    @GetMapping("/user/profile")
    @PreAuthorize("hasRole('USER')")
    public String userOnly() {
        return "User profile";
    }
}
