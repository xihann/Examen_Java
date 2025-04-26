package com.example.controller;


import com.example.model.Contacto;
import com.example.model.Coincidencia;
import com.example.service.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contactos") // Se define la ruta
public class ContactoController {

    private static final String FILE_PATH = "src/main/resources/data/contactos.xlsx"; // Ruta del archivo

    @Autowired
    private ContactoService contactoService;

    @PostMapping("/procesar")
    public  ResponseEntity<?> procesarContacto(){
        try {
            Map<Contacto, List<Coincidencia>> result = contactoService.procesarArchivoExcel(FILE_PATH);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al leer el archivo: " + e.getMessage());
        }
    }
}
