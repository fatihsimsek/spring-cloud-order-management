package com.example.orderservice.repository;

import com.example.orderservice.entity.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Order> getAll() {
        TypedQuery<Order> query = entityManager.createQuery("from Order", Order.class);
        return query.getResultList();
    }

    public Optional<Order> getById(String id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.of(order);
    }

    public void save(Order order) {
        String id = order.getId();

        if(StringUtils.isEmpty(id)) {
            order.setId(UUID.randomUUID().toString());
            entityManager.persist(order);
        }
        else {
            entityManager.merge(order);
        }
    }

    public void delete(String id) {
        Query query = entityManager.createQuery("DELETE Order WHERE id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
