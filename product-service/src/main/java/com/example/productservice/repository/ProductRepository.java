package com.example.productservice.repository;

import com.example.productservice.entity.Product;
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
public class ProductRepository {

    private EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Product> getAll() {
        TypedQuery<Product> query = entityManager.createQuery("from Product", Product.class);
        return query.getResultList();
    }

    public Optional<Product> getById(String id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.of(product);
    }

    public void save(Product product) {
        String id = product.getId();

        if(StringUtils.isEmpty(id)) {
            product.setId(UUID.randomUUID().toString());
            entityManager.persist(product);
        }
        else {
            entityManager.merge(product);
        }
    }

    public void delete(String id) {
        Query query = entityManager.createQuery("DELETE Product WHERE id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
