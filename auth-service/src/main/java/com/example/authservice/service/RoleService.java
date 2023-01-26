package com.example.authservice.service;

import com.example.authservice.entity.Role;
import com.example.authservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.setRoleRepository(roleRepository);
    }

    public List<Role> getAll() {
        return this.roleRepository.getAll();
    }

    public Role getById(String id) {
        return this.roleRepository.getById(id).get();
    }

    public void create(Role role) {
        this.roleRepository.save(role);
    }

    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getByCode(String code) {
        return this.roleRepository.getByCode(code);
    }
}
