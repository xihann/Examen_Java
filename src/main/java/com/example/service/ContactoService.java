package com.example.service;

import com.example.model.Coincidencia;
import com.example.model.Contacto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.*;

@Service
    public class ContactoService {

        public Map<Contacto, List<Coincidencia>> procesarArchivoExcel(String filePath) {
            List<Contacto> contactos = leerContactosDesdeExcel(filePath);
            return identificarCoincidencias(contactos);
        }

        public List<Contacto> leerContactosDesdeExcel(String filePath) {
            List<Contacto> contactos = new ArrayList<>();

            try (FileInputStream fis = new FileInputStream(filePath);
                 Workbook workbook = WorkbookFactory.create(fis)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    String contactId = getCellValueAsString(row.getCell(0)); // id contacto
                    String name = getCellValueAsString(row.getCell(1));      // nombre
                    String name1 = getCellValueAsString(row.getCell(2));     // apellido
                    String email = getCellValueAsString(row.getCell(3));     // correo
                    String postalZip = getCellValueAsString(row.getCell(4)); // cp
                    String address = getCellValueAsString(row.getCell(5));   // direccion
                    contactos.add(new Contacto(contactId, name, name1, email, postalZip, address));
                }
            } catch (Exception e) {
                System.err.println("Error al leer el archivo: " + e.getMessage());
            }
            return contactos;
        }

        private String getCellValueAsString(Cell cell) {
            if (cell == null) {
                return null;
            };
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getRichStringCellValue().getString().trim();
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                default:
                    return "";
            }
        }

        public Map<Contacto, List<Coincidencia>> identificarCoincidencias(List<Contacto> contactos) {
            Map<Contacto, List<Coincidencia>> resultado = new HashMap<>();
            Set<String> coincidenciasRegistradas = new HashSet<>();

            if (contactos != null) {
                for (int i = 0; i < contactos.size(); i++) {
                    Contacto contactoOrigen = contactos.get(i);
                    List<Coincidencia> coincidencias = new ArrayList<>();

                    for (int j = 0; j < contactos.size(); j++) {
                        if (i != j) { // No comparar el mismo contacto
                            Contacto contactoComparar = contactos.get(j);
                            String precision = calcularPrecision(contactoOrigen, contactoComparar);

                            //if (precision.equals("Alta")) { // Solo agregar coincidencias con puntuación positiva
                                String contactIdCoincidencia = contactoComparar.getContactID();

                                // Crear una clave única para la coincidencia
                                String claveCoincidencia = contactoOrigen.getContactID() + "-" + contactIdCoincidencia;
                                String claveInversa = contactIdCoincidencia + "-" + contactoOrigen.getContactID();

                                // Verificar si la coincidencia ya ha sido registrada
                                if (!coincidenciasRegistradas.contains(claveCoincidencia) && !coincidenciasRegistradas.contains(claveInversa)) {
                                    coincidencias.add(new Coincidencia(
                                            String.valueOf((int) Double.parseDouble(contactoOrigen.getContactID())), // formatea ei id de origen
                                            String.valueOf((int) Double.parseDouble(contactIdCoincidencia)),
                                            precision
                                    ));                                    coincidenciasRegistradas.add(claveCoincidencia); // Registrar la coincidencia
                                }
                            //}
                        }
                    }

                    if (!coincidencias.isEmpty()) {
                        resultado.put(contactoOrigen, coincidencias);
                    }
                }
            } else {
                // Manejo de caso donde 'contactos' es nulo
                System.out.println("La lista de contactos es nula.");
            }

            return resultado;
        }

        private String calcularPrecision(Contacto c1, Contacto c2) {
            String precision = "Baja";
            boolean name = false;
            boolean name1 = false;
            boolean email = false;
            boolean postalZIp = false;
            boolean address = false;

                if (c1.getName() != null && !c1.getName().isEmpty() && c2.getName() != null && !c2.getName().isEmpty()) {
                    if (c1.getName().equals(c2.getName()) || c1.getName().charAt(0) == c2.getName().charAt(0)) {
                        name = true;
                    }
                }

                if (c1.getName1() != null && !c1.getName1().isEmpty() && c2.getName1() != null && !c2.getName1().isEmpty()) {
                    if (c1.getName1().equals(c2.getName1()) || c1.getName1().charAt(0) == c2.getName1().charAt(0)) {
                        name1 = true;
                    }
                }

                if (c1.getEmail() != null && !c1.getEmail().isEmpty() && c2.getEmail() != null && !c2.getEmail().isEmpty()) {
                    if (c1.getEmail().equals(c2.getEmail())) {
                        email = true;
                    }
                }

            if (c1.getPostalZip() != null && !c1.getPostalZip().isEmpty() && c2.getPostalZip() != null && !c2.getPostalZip().isEmpty()) {
                if (c1.getPostalZip().equals(c2.getPostalZip())) {
                    postalZIp = true;
                }
            }

            if (c1.getAddress() != null && !c1.getAddress().isEmpty() && c2.getAddress() != null && !c2.getAddress().isEmpty()) {
                if (c1.getAddress().equals(c2.getAddress()) || c1.getAddress().contains(c2.getAddress()) ) {
                    address = true;
                }
            }

            if ((name && name1 && email && postalZIp && address ) || (name && name1 && email) || (name && name1 && postalZIp)
                    || (name && name1 && address)){
                    precision = "Alta";
                }

            return precision;
        }
    }
