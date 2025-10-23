package com.ecommerse.catalogo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateTO {
    private String id;
    private Integer cantidad;
    private String motivo; // "COMPRA", "VENTA", "DEVOLUCION", "DAÑADO", "INVENTARIO"
    private String comentario; // Comentario opcional sobre el motivo
}