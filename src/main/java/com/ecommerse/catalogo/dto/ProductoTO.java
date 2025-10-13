package com.ecommerse.catalogo.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductoTO {
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
    private String multimedia;
}
