package com.ecommerse.catalogo.repository;

import com.ecommerse.catalogo.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {

    @Query("{ $text: { $search: ?0 } }")
    List<Producto> searchByText(String query);

    // Métodos con paginación
    Page<Producto> findAll(Pageable pageable);
    Page<Producto> findByCategoria(String categoria, Pageable pageable);
    Page<Producto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
    Page<Producto> findByDisponibilidadTrue(Pageable pageable);
}