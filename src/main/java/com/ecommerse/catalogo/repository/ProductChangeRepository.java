package com.ecommerse.catalogo.repository;

import com.ecommerse.catalogo.model.ProductChange;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductChangeRepository extends MongoRepository<ProductChange, String> {
    List<ProductChange> findByProductIdOrderByChangeDateDesc(String productId);
}
