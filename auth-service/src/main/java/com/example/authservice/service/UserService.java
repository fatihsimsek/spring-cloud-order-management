package com.example.authservice.service;

import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.setUserRepository(userRepository);
    }

    public List<User> getAll() {
        return this.userRepository.getAll();
    }

    public User getById(String id) {
        return this.userRepository.getById(id).get();
    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByEmail(String email) {
        return this.userRepository.getByEmail(email);
    }
}
