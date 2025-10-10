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
	
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public List<String> getAtributos() {
		return atributos;
	}
	public void setAtributos(List<String> atributos) {
		this.atributos = atributos;
	}
	public Double getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(Double calificacion) {
		this.calificacion = calificacion;
	}
	public List<String> getComentarios() {
		return comentarios;
	}
	public void setComentarios(List<String> comentarios) {
		this.comentarios = comentarios;
	}
	public Boolean getDisponibilidad() {
		return disponibilidad;
	}
	public void setDisponibilidad(Boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getGarantia() {
		return garantia;
	}
	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}
	public String getMultimedia() {
		return multimedia;
	}
	public void setMultimedia(String multimedia) {
		this.multimedia = multimedia;
	}
	
	
    
    
}