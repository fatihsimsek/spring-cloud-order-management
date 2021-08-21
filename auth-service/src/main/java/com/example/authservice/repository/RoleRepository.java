package com.example.authservice.repository;

import com.example.authservice.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    List<Role> getAll();

    Optional<Role> getById(String id);

    Optional<Role> getByCode(String code);

    void save(Role role);
}
