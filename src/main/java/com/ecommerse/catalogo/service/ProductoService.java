package com.ecommerse.catalogo.service;

import com.ecommerse.catalogo.dto.ProductoTO;
import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public String createProducto(ProductoTO productoTO) {
        try{
            Producto product = Producto.builder()
                    .nombre(productoTO.getNombre())
                    .descripcion(productoTO.getDescripcion())
                    .precio(productoTO.getPrecio())
                    .categoria(productoTO.getCategoria())
                    .atributos(productoTO.getAtributos())
                    .calificacion(productoTO.getCalificacion())
                    .comentarios(productoTO.getComentarios())
                    .disponibilidad(productoTO.getDisponibilidad())
                    .stock(productoTO.getStock())
                    .marca(productoTO.getMarca())
                    .garantia(productoTO.getGarantia())
                    .multimedia(productoTO.getMultimedia())
                    .build();


            productoRepository.save(product);
            return "Product Created!";

        } catch (Exception e){
            e.printStackTrace();
            return "Error!";

        }

    }
    public List<Producto> getProducto(){
        List<Producto> productList = new ArrayList<>();
        try{
            productList = productoRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return productList;
    }
}