package com.example.authservice.repository;

import com.example.authservice.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Role> getAll() {
        TypedQuery<Role> query = entityManager.createQuery("from Role", Role.class);
        return query.getResultList();
    }

    public Optional<Role> getById(String id) {
        Role role = entityManager.find(Role.class, id);
        return Optional.of(role);
    }

    public Optional<Role> getByCode(String code) {
        TypedQuery<Role> query = entityManager.createQuery("from Role Where Code=:Code", Role.class)
                .setParameter("Code", code);

        List<Role> roles = query.getResultList();

        return roles.size() > 0 ? Optional.of(roles.get(0)) : Optional.of(null);
    }

    public void save(Role role) {
        String id = role.getId();

        if(StringUtils.isEmpty(id)) {
            role.setId(UUID.randomUUID().toString());
            entityManager.persist(role);
        }
        else {
            entityManager.merge(role);
        }
    }

}
