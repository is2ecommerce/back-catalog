package com.ecommerse.catalogo.controller;

import com.ecommerse.catalogo.dto.ProductoTO;
import com.ecommerse.catalogo.model.Producto;
import com.ecommerse.catalogo.service.ProductoService;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

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
}