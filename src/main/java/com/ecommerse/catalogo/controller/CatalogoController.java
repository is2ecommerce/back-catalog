package com.ecommerse.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogoController {
	
	@Autowired
	CatalogoService catalogoservice;
	
	@PutMapping("/updateCatalogo")
	public ResponseEntity<Catalogo> editarCatalogo(@Valid @RequestBody Catalogo catalogo){
		Catalogo obj = catalogoservice.buscarCatalogo(catalogo.getId());
		
		if (obj != null) {
			obj.setIdProveedor(catalogo.getIdProveedor());
			
			catalogoservice.nuevoProducto(catalogo);
		}else {
			return new ResponseEntity<>(obj,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

}
