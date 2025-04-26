package com.example.model;

public class Coincidencia {

    private String contactIdOrigen;
    private String contactIdCoincidencia;
    private String precision;

    public Coincidencia(String contactIdOrigen, String contactIdCoincidencia, String precision) {
        this.contactIdOrigen = contactIdOrigen;
        this.contactIdCoincidencia = contactIdCoincidencia;
        this.precision = precision;
    }

    public String getContactIdOrigen() {
        return contactIdOrigen;
    }

    public String getContactIdCoincidencia() {
        return contactIdCoincidencia;
    }

    public String getPrecision() {
        return precision;
    }
}
