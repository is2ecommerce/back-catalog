package com.ecommerse.catalogo.service;

import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    
    //NuevoProducto
	public Producto nuevoProducto(Producto producto) {
		return productoRepository.save(producto);
	}
    
    //BuscarProducto
    public Producto buscarProducto(String id) {
		Producto producto = null;
		producto = productoRepository.findById(id).orElse(null);	
		if(producto == null) {
			return null;
		}else {
			return producto;			
		}
	}
    
    // Eliminar producto
    public boolean eliminarProducto(String id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}