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
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API para eliminar productos del catálogo")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    @PutMapping("/editarProducto")
    @Operation(summary = "Editar producto", description = "Editar un producto del catálogo")
	public ResponseEntity<Producto> editarCatalogo(@RequestBody Producto producto){
    	Producto obj = productoService.buscarProducto(producto.getId());
		
		if (obj != null) {
			obj.setAtributos(producto.getAtributos());
			obj.setCalificacion(producto.getCalificacion());
			obj.setCategoria(producto.getCategoria());
			obj.setComentarios(producto.getComentarios());
			obj.setDescripcion(producto.getDescripcion());
			obj.setDisponibilidad(producto.getDisponibilidad());
			obj.setGarantia(producto.getGarantia());
			obj.setMarca(producto.getMarca());
			obj.setMultimedia(producto.getMultimedia());
			obj.setNombre(producto.getNombre());
			obj.setPrecio(producto.getPrecio());
			obj.setStock(producto.getStock());			
			productoService.nuevoProducto(producto);
		}else {
			return new ResponseEntity<>(obj,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductoTO pro){
        return productoService.createProducto(pro);
    }

    @GetMapping("/get/product")
    @ResponseStatus(HttpStatus.OK)
    public List<Producto> getProduct(){
        return productoService.getProducto();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar productos", description = "Filtra productos por múltiples criterios")
    public List<Producto> filtrarProductos(
            @RequestParam(required = false) BigDecimal precio_min,
            @RequestParam(required = false) BigDecimal precio_max,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) Boolean disponibilidad,
            @RequestParam(required = false) Integer stock_min,
            @RequestParam(required = false) String q
    ) {
        return productoService.filtrarProductos(
            precio_min, precio_max, categoria, marca, 
            disponibilidad, stock_min, q
        );
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
    
    @GetMapping("/paginated")
    @Operation(summary = "Obtener productos con paginación", 
               description = "Obtiene productos con paginación y ordenamiento. Parámetros opcionales: page (0), size (10), sort (nombre)")
    public ResponseEntity<Page<Producto>> obtenerProductosConPaginacion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String categoria) {
        
        try {
            // Crear Sort basado en dirección
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            // Crear Pageable
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Producto> productos;
            
            // Si se especifica categoría, filtrar por categoría
            if (categoria != null && !categoria.trim().isEmpty()) {
                productos = productoService.obtenerProductosPorCategoriaConPaginacion(categoria, pageable);
            } else {
                productos = productoService.obtenerProductosConPaginacion(pageable);
            }
            
            return ResponseEntity.ok(productos);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/Search")
    public ResponseEntity<List<Producto>> searchProductss(
            @RequestParam(name = "query", required = false) String query) {

        List<Producto> results = productoService.searchProducts(query);
        return ResponseEntity.ok(results);
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

}