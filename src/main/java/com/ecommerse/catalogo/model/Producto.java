package com.ecommerse.catalogo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productos")
public class Producto {
    
    @Id
    private String id;
    
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoria;
    private List<String> atributos;
    private Double calificacion;
    private List<String> comentarios;
    private Boolean disponibilidad;
    private Integer stock;
    private String marca;
    private String garantia;
    private String multimedia; // URL de la imagen
}