package com.example.user.service;

import com.example.user.dto.CreateUserRequest;
import com.example.user.dto.UpdateUserRequest;
import com.example.user.dto.UserResponse;
import com.example.user.entity.User;

import java.util.List;

public interface UserService {


    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByEmail(String email);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id,  UpdateUserRequest request);



    void deleteUser(Long id);
}
