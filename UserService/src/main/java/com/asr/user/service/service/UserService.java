package com.asr.user.service.service;

import com.asr.user.service.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    String deleteUserById(Long id);
}
