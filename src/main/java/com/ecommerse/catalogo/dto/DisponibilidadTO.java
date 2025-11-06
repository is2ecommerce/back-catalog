package com.ecommerse.catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadTO {
    private String id;
    private String nombre;
    private Boolean disponibilidad;
    private Integer stock;
    private String estado; // "DISPONIBLE", "SIN_STOCK", "INACTIVO"
    private String mensaje;
}