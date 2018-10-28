package com.javiles.eshop.services;

import com.javiles.eshop.models.User;

import java.util.List;

public interface UserService
{
    void save(User user);

    User findByUsername(String username);

    User findByUserId(long id);

    void deleteUserById(long id);

    List<User> searchUserByUsername(String name);

    void updateUser(User user);
}