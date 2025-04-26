package com.example.service;

import com.example.model.Contacto;
import com.example.model.Coincidencia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ContactoServiceTest {

    @InjectMocks
    private ContactoService contactoService;

    @Test
    public void testIdentificarCoincidencias() {
        // Preparar datos de prueba
        List<Contacto> contactos = Arrays.asList(
                new Contacto("1", "J", "Perez", "juan@google.com", "1234", "Calle Falsa 123"),
                new Contacto("2", "Juan", "P", "juan@google.com", "1234", "Calle Falsa 123"),
                new Contacto("3", "Anahi", "Garcia", "anahi@google.com", "5678", "Calle Verdadera 456")
        );

        Map<Contacto, List<Coincidencia>> resultado = contactoService.identificarCoincidencias(contactos);

        assertNotNull(resultado);

        assertEquals(2, resultado.size()); // Debería haber coincidencias para Juan

        List<Coincidencia> coincidenciasAnahi = resultado.get(contactos.get(1));
        assertNotNull(coincidenciasAnahi);
        assertEquals("Baja", coincidenciasAnahi.get(0).getPrecision()); // Ana no debería tener coincidencias

        // Verificar que Juan tenga presicion Alta
        List<Coincidencia> coincidenciasJuan = resultado.get(contactos.get(0));
        assertNotNull(coincidenciasJuan);
        assertTrue(coincidenciasJuan.size() > 0);
        assertEquals("Alta", coincidenciasJuan.get(0).getPrecision());
    }
}