package com.ecommerse.catalogo.service;

import com.ecommerse.catalogo.dto.ProductoTO;
import com.ecommerse.catalogo.dto.StockUpdateTO;
import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
    // Método principal para actualizar stock
    public Producto actualizarStock(StockUpdateTO stockUpdate) {
        // Buscar el producto por ID
        Optional<Producto> productoOpt = productoRepository.findById(stockUpdate.getId());
        
        if (!productoOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        
        Producto producto = productoOpt.get();
        int stockActual = producto.getStock();
        int nuevoStock = stockActual + stockUpdate.getCantidad();
        
        // Validar que el stock no sea negativo
        if (nuevoStock < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Stock insuficiente. Stock actual: " + stockActual + 
                ", intentando restar: " + Math.abs(stockUpdate.getCantidad()));
        }
        
        // Actualizar el stock
        producto.setStock(nuevoStock);
        
        // Actualizar disponibilidad basada en el stock
        producto.setDisponibilidad(nuevoStock > 0);
        
        return productoRepository.save(producto);
    }
    
    // Método para sumar stock
    public Producto sumarStock(String id, int cantidad, String motivo, String comentario) {
        StockUpdateTO stockUpdate = new StockUpdateTO();
        stockUpdate.setId(id);
        stockUpdate.setCantidad(cantidad); // Cantidad positiva para sumar
        stockUpdate.setMotivo(motivo);
        stockUpdate.setComentario(comentario);
        
        return actualizarStock(stockUpdate);
    }
    
    // Método para restar stock
    public Producto restarStock(String id, int cantidad, String motivo, String comentario) {
        StockUpdateTO stockUpdate = new StockUpdateTO();
        stockUpdate.setId(id);
        stockUpdate.setCantidad(-cantidad); // Cantidad negativa para restar
        stockUpdate.setMotivo(motivo);
        stockUpdate.setComentario(comentario);
        
        return actualizarStock(stockUpdate);
    }
}