package com.notebook.app.services;

import com.notebook.app.dtos.UserDTO;
import com.notebook.app.models.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);
}
