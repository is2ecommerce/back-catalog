package com.ecommerse.catalogo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {
    private String autor;
    private String texto;
    private Double calificacion; // 1.0 a 5.0
    private LocalDateTime fecha;
}
