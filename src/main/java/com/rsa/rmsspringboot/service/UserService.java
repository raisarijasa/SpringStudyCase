package com.rsa.rmsspringboot.service;

import com.rsa.rmsspringboot.entity.User;

import java.util.List;

public interface UserService {
    public User findByEmail(String email);

    User save(User user);

    List<User> findAll();

    void deleteById(Long id);

    void resetPassword(String email);
}
