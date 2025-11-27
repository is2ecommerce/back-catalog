package com.ecommerse.catalogo.controller;

import com.ecommerse.catalogo.dto.ComentarioTO;
import com.ecommerse.catalogo.dto.DisponibilidadTO;
import com.ecommerse.catalogo.dto.ProductoTO;
import com.ecommerse.catalogo.model.Comentario;
import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.service.ProductoService;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerse.catalogo.model.ProductChange;
import com.ecommerse.catalogo.repository.ProductChangeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Collections; // Importante para evitar errores de listas nulas
import java.util.stream.Collectors;
import java.util.ArrayList;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para gestionar productos del catálogo")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private ProductChangeRepository productChangeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PutMapping("/{id}")
    @Operation(summary = "Editar producto", description = "Editar un producto del catálogo")
	public ResponseEntity<Producto> editarCatalogo(@PathVariable String id, @RequestBody Producto producto){
        producto.setId(id);
    	Producto obj = productoService.buscarProducto(producto.getId());
		
		if (obj != null) {
            Map<String, Object> modified = new HashMap<>();

			if (producto.getAtributos() != null && !producto.getAtributos().equals(obj.getAtributos())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getAtributos());
                change.put("new", producto.getAtributos());
                modified.put("atributos", change);
                obj.setAtributos(producto.getAtributos());
            }
            if (producto.getCalificacion() != null && !producto.getCalificacion().equals(obj.getCalificacion())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getCalificacion());
                change.put("new", producto.getCalificacion());
                modified.put("calificacion", change);
                obj.setCalificacion(producto.getCalificacion());
            }
            if (producto.getCategoria() != null && !producto.getCategoria().equals(obj.getCategoria())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getCategoria());
                change.put("new", producto.getCategoria());
                modified.put("categoria", change);
                obj.setCategoria(producto.getCategoria());
            }
            if (producto.getComentarios() != null && !producto.getComentarios().equals(obj.getComentarios())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getComentarios());
                change.put("new", producto.getComentarios());
                modified.put("comentarios", change);
                obj.setComentarios(producto.getComentarios());
            }
            if (producto.getDescripcion() != null && !producto.getDescripcion().equals(obj.getDescripcion())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getDescripcion());
                change.put("new", producto.getDescripcion());
                modified.put("descripcion", change);
                obj.setDescripcion(producto.getDescripcion());
            }
            if (producto.getDisponibilidad() != null && !producto.getDisponibilidad().equals(obj.getDisponibilidad())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getDisponibilidad());
                change.put("new", producto.getDisponibilidad());
                modified.put("disponibilidad", change);
                obj.setDisponibilidad(producto.getDisponibilidad());
            }
            if (producto.getGarantia() != null && !producto.getGarantia().equals(obj.getGarantia())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getGarantia());
                change.put("new", producto.getGarantia());
                modified.put("garantia", change);
                obj.setGarantia(producto.getGarantia());
            }
            if (producto.getMarca() != null && !producto.getMarca().equals(obj.getMarca())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getMarca());
                change.put("new", producto.getMarca());
                modified.put("marca", change);
                obj.setMarca(producto.getMarca());
            }
            if (producto.getMultimedia() != null && !producto.getMultimedia().equals(obj.getMultimedia())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getMultimedia());
                change.put("new", producto.getMultimedia());
                modified.put("multimedia", change);
                obj.setMultimedia(producto.getMultimedia());
            }
            if (producto.getNombre() != null && !producto.getNombre().equals(obj.getNombre())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getNombre());
                change.put("new", producto.getNombre());
                modified.put("nombre", change);
                obj.setNombre(producto.getNombre());
            }
            if (producto.getPrecio() != null && !producto.getPrecio().equals(obj.getPrecio())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getPrecio());
                change.put("new", producto.getPrecio());
                modified.put("precio", change);
                obj.setPrecio(producto.getPrecio());
            }
            if (producto.getStock() != null && !producto.getStock().equals(obj.getStock())) {
                Map<String,Object> change = new HashMap<>();
                change.put("old", obj.getStock());
                change.put("new", producto.getStock());
                modified.put("stock", change);
                obj.setStock(producto.getStock());
            }

			productoService.nuevoProducto(obj);

            if (!modified.isEmpty()) {
                try {
                    ProductChange pc = new ProductChange();
                    pc.setProductId(obj.getId());
                    pc.setChangeDate(LocalDateTime.now());
                    pc.setChangeType("UPDATE");
                    pc.setModifiedFieldsJson(objectMapper.writeValueAsString(modified));
                    productChangeRepository.save(pc);
                } catch (Exception ex) {
                }
            }

		}else {
			return new ResponseEntity<>(obj,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        Producto existing = productoService.buscarProducto(id);
        if (existing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            ProductChange pc = new ProductChange();
            pc.setProductId(id);
            pc.setChangeDate(LocalDateTime.now());
            pc.setChangeType("DELETE");
            pc.setModifiedFieldsJson(objectMapper.writeValueAsString(existing));
            productChangeRepository.save(pc);
        } catch (Exception ex) {
        }

        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductoTO pro){
        String createdId = productoService.createProducto(pro);
        try {
            Producto created = productoService.buscarProducto(createdId);
            if (created != null) {
                ProductChange pc = new ProductChange();
                pc.setProductId(created.getId());
                pc.setChangeDate(LocalDateTime.now());
                pc.setChangeType("CREATE");
                pc.setModifiedFieldsJson(objectMapper.writeValueAsString(created));
                productChangeRepository.save(pc);
            }
        } catch (Exception ex) {
        }
        return createdId;
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Obtener historial de cambios de un producto", description = "Devuelve historial (fecha, campos modificados y tipo)")
    public ResponseEntity<List<ProductChange>> obtenerHistorialCambios(@PathVariable String id) {
        List<ProductChange> cambios = productChangeRepository.findByProductIdOrderByChangeDateDesc(id);
        return ResponseEntity.ok(cambios);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Producto> getProduct(){
        List<Producto> list = productoService.getProducto();
        return list != null ? list : Collections.emptyList();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar productos", description = "Filtra productos por múltiples criterios en memoria")
    public List<Producto> filtrarProductos(
            @RequestParam(required = false) BigDecimal precio_min,
            @RequestParam(required = false) BigDecimal precio_max,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Boolean disponibilidad,
            @RequestParam(required = false) Integer stock_min,
            @RequestParam(required = false) Double calificacion_min,
            @RequestParam(required = false) String q
    ) {
        // DEBUG: Imprimir filtros recibidos
        System.out.println("--- FILTRANDO PRODUCTOS ---");
        System.out.println("Precio Min: " + precio_min);
        System.out.println("Precio Max: " + precio_max);
        System.out.println("Categoría: " + categoria);
        System.out.println("Stock Min: " + stock_min);
        System.out.println("Calif Min: " + calificacion_min);

        List<Producto> todos = productoService.getProducto();
        
        if (todos == null) return new ArrayList<>();

        return todos.stream()
            .filter(p -> {
                // Filtro de Búsqueda General (q)
                if (q != null && !q.trim().isEmpty()) {
                    String query = q.toLowerCase();
                    boolean matchNombre = p.getNombre() != null && p.getNombre().toLowerCase().contains(query);
                    boolean matchDesc = p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(query);
                    if (!matchNombre && !matchDesc) return false;
                }

                // Filtro de Precio Mínimo
                if (precio_min != null && p.getPrecio() != null) {
                    if (p.getPrecio().compareTo(precio_min) < 0) return false;
                }

                // Filtro de Precio Máximo
                if (precio_max != null && p.getPrecio() != null) {
                    if (p.getPrecio().compareTo(precio_max) > 0) return false;
                }

                // Filtro de Categoría (Ignorando mayúsculas/minúsculas)
                if (categoria != null && !categoria.trim().isEmpty()) {
                    if (p.getCategoria() == null || !p.getCategoria().trim().equalsIgnoreCase(categoria.trim())) {
                        return false;
                    }
                }

                // Filtro de Marca
                if (marca != null && !marca.trim().isEmpty()) {
                    if (p.getMarca() == null || !p.getMarca().equalsIgnoreCase(marca)) return false;
                }

                // Filtro de Disponibilidad (En Stock)
                if (Boolean.TRUE.equals(disponibilidad)) {
                    if (p.getStock() == null || p.getStock() <= 0) return false;
                }

                // Filtro de Stock Mínimo específico
                if (stock_min != null) {
                    if (p.getStock() == null || p.getStock() < stock_min) return false;
                }

                // Filtro de Calificación (Estrellas)
                if (calificacion_min != null) {
                    double rating = p.getCalificacion() != null ? p.getCalificacion() : 0.0;
                    if (rating < calificacion_min) return false;
                }

                return true;
            })
            .collect(Collectors.toList());
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buscar productos", description = "Buscar productos por texto (insensible a mayúsculas)")
    public List<Producto> searchProducts(@RequestParam(name = "q", required = false) String query){
        return productoService.buscarProductos(query);
    }
    
    
    @PutMapping("/{id}/sumar-stock")
    @Operation(summary = "Sumar stock a producto", 
               description = "Suma una cantidad específica al stock del producto (útil para compras, devoluciones)")
    public ResponseEntity<Producto> sumarStock(
            @PathVariable String id,
            @RequestParam int cantidad,
            @RequestParam(defaultValue = "COMPRA") String motivo,
            @RequestParam(required = false) String comentario) {
        try {
            Producto productoActualizado = productoService.sumarStock(id, cantidad, motivo, comentario);
            return ResponseEntity.ok(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @PutMapping("/{id}/restar-stock")
    @Operation(summary = "Restar stock a producto", 
               description = "Resta una cantidad específica al stock del producto (útil para ventas, productos dañados)")
    public ResponseEntity<Producto> restarStock(
            @PathVariable String id,
            @RequestParam int cantidad,
            @RequestParam(defaultValue = "VENTA") String motivo,
            @RequestParam(required = false) String comentario) {
        try {
            Producto productoActualizado = productoService.restarStock(id, cantidad, motivo, comentario);
            return ResponseEntity.ok(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @GetMapping(params = "page")
    @Operation(summary = "Obtener productos con paginación", 
               description = "Obtiene productos con paginación y ordenamiento. Parámetros opcionales: page (0), size (10), sort (nombre)")
    public ResponseEntity<Page<Producto>> obtenerProductosConPaginacion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String categoria) {
        
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Producto> productos;
            
            if (categoria != null && !categoria.trim().isEmpty()) {
                productos = productoService.obtenerProductosPorCategoriaConPaginacion(categoria, pageable);
            } else {
                productos = productoService.obtenerProductosConPaginacion(pageable);
            }
            
            if (productos == null) {
                return ResponseEntity.ok(Page.empty());
            }
            return ResponseEntity.ok(productos);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/gallery")
    @Operation(
            summary = "Obtener galería de imágenes del producto",
            description = "Devuelve todas las URLs de las imágenes asociadas a un producto por su ID")
    public List<String> obtenerGaleria(@PathVariable String id) {
        return productoService.obtenerGaleriaId(id);
    }
    
    // ==================== ENDPOINTS DE COMENTARIOS ====================
    
    @PostMapping("/{id}/comentarios")
    @Operation(
            summary = "Agregar comentario a producto",
            description = "Agrega un comentario con calificación a un producto. La calificación del producto se recalcula automáticamente como promedio de todos los comentarios.")
    public ResponseEntity<Producto> agregarComentario(
            @PathVariable String id,
            @RequestBody ComentarioTO comentarioTO) {
        try {
            Producto productoActualizado = productoService.agregarComentario(id, comentarioTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @GetMapping("/{id}/comentarios")
    @Operation(
            summary = "Obtener comentarios de producto",
            description = "Devuelve todos los comentarios de un producto incluyendo autor, texto, calificación y fecha")
    public ResponseEntity<List<Comentario>> obtenerComentarios(@PathVariable String id) {
        try {
            List<Comentario> comentarios = productoService.obtenerComentarios(id);
            return ResponseEntity.ok(comentarios);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @PutMapping("/{id}/calificacion")
    @Operation(
            summary = "Actualizar calificación de producto",
            description = "Actualiza manualmente la calificación de un producto (sin agregar comentario). Calificación debe estar entre 1.0 y 5.0")
    public ResponseEntity<Producto> actualizarCalificacion(
            @PathVariable String id,
            @RequestParam Double calificacion) {
        try {
            Producto productoActualizado = productoService.actualizarCalificacion(id, calificacion);
            return ResponseEntity.ok(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    // ==================== ENDPOINTS DE DISPONIBILIDAD ====================
    
    @GetMapping("/{id}/availability")
    @Operation(
            summary = "Obtener disponibilidad de producto",
            description = "Devuelve el estado de disponibilidad y stock de un producto específico. Estados posibles: DISPONIBLE, SIN_STOCK, INACTIVO")
    public ResponseEntity<DisponibilidadTO> obtenerDisponibilidad(@PathVariable String id) {
        try {
            DisponibilidadTO disponibilidad = productoService.obtenerDisponibilidad(id);
            return ResponseEntity.ok(disponibilidad);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @GetMapping("/inactive")
    @Operation(
            summary = "Obtener productos inactivos",
            description = "Devuelve todos los productos que están inactivos (disponibilidad = false) o tienen stock = 0")
    public ResponseEntity<List<Producto>> obtenerProductosInactivos() {
        try {
            List<Producto> productosInactivos = productoService.obtenerProductosInactivos();
            return ResponseEntity.ok(productosInactivos);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    // ==================== ENDPOINTS DE GESTIÓN DE IMÁGENES ====================
    
    @PostMapping("/{id}/images")
    @Operation(
            summary = "Agregar imagen a producto",
            description = "Agrega una nueva URL de imagen al producto. La URL debe ser válida y no puede estar duplicada.")
    public ResponseEntity<Producto> agregarImagen(
            @PathVariable String id,
            @RequestParam String imageUrl) {
        try {
            Producto productoActualizado = productoService.agregarImagen(id, imageUrl);
            return ResponseEntity.status(HttpStatus.CREATED).body(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @DeleteMapping("/{id}/images")
    @Operation(
            summary = "Eliminar imagen específica de producto",
            description = "Elimina una URL de imagen específica del producto. La URL debe existir en la lista de multimedia.")
    public ResponseEntity<Producto> eliminarImagen(
            @PathVariable String id,
            @RequestParam String imageUrl) {
        try {
            Producto productoActualizado = productoService.eliminarImagen(id, imageUrl);
            return ResponseEntity.ok(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    
    @DeleteMapping("/{id}/images/all")
    @Operation(
            summary = "Eliminar todas las imágenes de producto",
            description = "Elimina todas las URLs de imágenes del producto, dejando la lista de multimedia vacía.")
    public ResponseEntity<Producto> eliminarTodasLasImagenes(@PathVariable String id) {
        try {
            Producto productoActualizado = productoService.eliminarTodasLasImagenes(id);
            return ResponseEntity.ok(productoActualizado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

}