package com.example.authservice.repository;

import com.example.authservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    Optional<User> getById(String id);

    Optional<User> getByEmail(String email);

    void save(User user);
}
