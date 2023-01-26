package com.example.authservice.repository;

import com.example.authservice.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<User> getAll() {
        TypedQuery<User> query = entityManager.createQuery("from User", User.class);
        return query.getResultList();
    }

    public Optional<User> getById(String id) {
        User user = entityManager.find(User.class, id);
        return Optional.of(user);
    }

    public Optional<User> getByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("from User Where Email=:Email", User.class)
                .setParameter("Email", email);

        List<User> users = query.getResultList();

        return users.size() > 0 ? Optional.of(users.get(0)) : Optional.of(null);
    }

    public void save(User user) {
        String id = user.getId();

        if (StringUtils.isEmpty(id)) {
            user.setId(UUID.randomUUID().toString());
            entityManager.persist(user);
        } else {
            entityManager.merge(user);
        }
    }
}
