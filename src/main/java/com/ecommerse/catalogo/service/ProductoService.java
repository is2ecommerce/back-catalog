package com.ecommerse.catalogo.service;

import com.ecommerse.catalogo.dto.ComentarioTO;
import com.ecommerse.catalogo.dto.ProductoTO;
import com.ecommerse.catalogo.dto.StockUpdateTO;
import com.ecommerse.catalogo.model.Comentario;
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
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import org.springframework.data.mongodb.core.MongoTemplate;


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
            // Convertir comentarios de List<String> a List<Comentario> si vienen datos antiguos
            List<Comentario> comentariosLegacy = new ArrayList<>();
            if (productoTO.getComentarios() != null && !productoTO.getComentarios().isEmpty()) {
                for (String texto : productoTO.getComentarios()) {
                    comentariosLegacy.add(Comentario.builder()
                            .texto(texto)
                            .autor("Sistema")
                            .calificacion(0.0)
                            .fecha(LocalDateTime.now())
                            .build());
                }
            }
            
            // Adaptar multimedia: DTO trae un único String; convertir a lista si viene informado
            List<String> multimediaList = new ArrayList<>();
            if (productoTO.getMultimedia() != null && !productoTO.getMultimedia().isBlank()) {
                multimediaList.add(productoTO.getMultimedia());
            }

            Producto product = Producto.builder()
                    .nombre(productoTO.getNombre())
                    .descripcion(productoTO.getDescripcion())
                    .precio(productoTO.getPrecio())
                    .categoria(productoTO.getCategoria())
                    .atributos(productoTO.getAtributos())
                    .calificacion(productoTO.getCalificacion())
                    .comentarios(comentariosLegacy)
                    .disponibilidad(productoTO.getDisponibilidad())
                    .stock(productoTO.getStock())
                    .marca(productoTO.getMarca())
                    .garantia(productoTO.getGarantia())
                    .multimedia(multimediaList)
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

    //Método para buscar producto en la base de datos
    public List<Producto> searchProducts(String query) {
        if (query == null || query.isBlank()) {
            return productoRepository.findAll();
        }
        return productoRepository.searchByText(query);
    }

    //Metodo para obtener la galeria de imagenes
    public List<String> obtenerGaleriaId(String id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con id: " + id));
        return producto.getMultimedia();
    }

    public Producto agregarComentario(String id, ComentarioTO comentarioTO) {
        // Validar calificación
        if (comentarioTO.getCalificacion() == null || 
            comentarioTO.getCalificacion() < 1.0 || 
            comentarioTO.getCalificacion() > 5.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "La calificación debe estar entre 1.0 y 5.0");
        }
        
        // Validar campos requeridos
        if (comentarioTO.getTexto() == null || comentarioTO.getTexto().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El texto del comentario es obligatorio");
        }
        
        // Buscar producto
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        
        // Crear comentario (autor siempre "Anónimo" hasta implementar autenticación)
        Comentario comentario = Comentario.builder()
                .autor("Anónimo")
                .texto(comentarioTO.getTexto())
                .calificacion(comentarioTO.getCalificacion())
                .fecha(LocalDateTime.now())
                .build();
        
        // Agregar a la lista
        List<Comentario> comentarios = producto.getComentarios() != null 
                ? new ArrayList<>(producto.getComentarios()) 
                : new ArrayList<>();
        comentarios.add(comentario);
        producto.setComentarios(comentarios);
        
        // Recalcular calificación promedio
        double promedioCalificacion = comentarios.stream()
                .filter(c -> c.getCalificacion() != null && c.getCalificacion() > 0)
                .mapToDouble(Comentario::getCalificacion)
                .average()
                .orElse(0.0);
        producto.setCalificacion(Math.round(promedioCalificacion * 10.0) / 10.0); // Redondear a 1 decimal
        
        return productoRepository.save(producto);
    }
    
    /**
     * Obtiene todos los comentarios de un producto
     * @param id ID del producto
     * @return Lista de comentarios
     */
    public List<Comentario> obtenerComentarios(String id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        return producto.getComentarios() != null ? producto.getComentarios() : new ArrayList<>();
    }
    
    /**
     * Actualiza manualmente la calificación de un producto (sin comentario)
     * @param id ID del producto
     * @param nuevaCalificacion Nueva calificación (1.0 a 5.0)
     * @return Producto actualizado
     */
    public Producto actualizarCalificacion(String id, Double nuevaCalificacion) {
        if (nuevaCalificacion == null || nuevaCalificacion < 1.0 || nuevaCalificacion > 5.0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "La calificación debe estar entre 1.0 y 5.0");
        }
        
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        
        producto.setCalificacion(Math.round(nuevaCalificacion * 10.0) / 10.0);
        return productoRepository.save(producto);
    }
}