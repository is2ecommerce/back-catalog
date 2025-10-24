package com.ecommerse.catalogo.service;

import com.ecommerse.catalogo.dto.ProductoTO;
import com.ecommerse.catalogo.dto.StockUpdateTO;
import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import java.math.BigDecimal;
import java.util.regex.Pattern;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    
    
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

    // Método para filtrar productos con múltiples criterios
    public List<Producto> filtrarProductos(
            BigDecimal precioMin,
            BigDecimal precioMax,
            String categoria,
            String marca,
            Boolean disponibilidad,
            Integer stockMin,
            String q
    ) {
        try {
            // Crear query base
            Query query = new Query();
            List<Criteria> criterias = new ArrayList<>();

            // Añadir criterios según los parámetros proporcionados
            if (precioMin != null) {
                criterias.add(Criteria.where("precio").gte(precioMin));
            }
            if (precioMax != null) {
                criterias.add(Criteria.where("precio").lte(precioMax));
            }
            if (categoria != null && !categoria.trim().isEmpty()) {
                criterias.add(Criteria.where("categoria").is(categoria));
            }
            if (marca != null && !marca.trim().isEmpty()) {
                criterias.add(Criteria.where("marca").is(marca));
            }
            if (disponibilidad != null) {
                criterias.add(Criteria.where("disponibilidad").is(disponibilidad));
            }
            if (stockMin != null) {
                criterias.add(Criteria.where("stock").gte(stockMin));
            }
            if (q != null && !q.trim().isEmpty()) {
                // Reutilizar lógica de búsqueda case-insensitive
                Pattern pattern = Pattern.compile(Pattern.quote(q), Pattern.CASE_INSENSITIVE);
                criterias.add(new Criteria().orOperator(
                    Criteria.where("nombre").regex(pattern),
                    Criteria.where("descripcion").regex(pattern),
                    Criteria.where("categoria").regex(pattern)
                ));
            }

            // Combinar todos los criterios con AND
            if (!criterias.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criterias.toArray(new Criteria[0])));
            }

            return mongoTemplate.find(query, Producto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public List<Producto> buscarProductos(String q) {
        if (q == null || q.trim().isEmpty()) {
            return getProducto();
        }
        try {

            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(java.util.regex.Pattern.quote(q), java.util.regex.Pattern.CASE_INSENSITIVE);
            Query query = new Query();
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("nombre").regex(pattern),
                    Criteria.where("descripcion").regex(pattern),
                    Criteria.where("categoria").regex(pattern)
            ));
            return mongoTemplate.find(query, Producto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

<<<<<<< Updated upstream
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
    
    // Método para obtener productos con paginación
    public Page<Producto> obtenerProductosConPaginacion(Pageable pageable) {
        try {
            return productoRepository.findAll(pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener productos con paginación");
        }
    }
    
    // Método para obtener productos por categoría con paginación
    public Page<Producto> obtenerProductosPorCategoriaConPaginacion(String categoria, Pageable pageable) {
        try {
            return productoRepository.findByCategoria(categoria, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener productos por categoría con paginación");
        }
    }
}