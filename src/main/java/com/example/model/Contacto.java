package com.example.model;

public class Contacto {

    private String contactID;
    private String name;
    private String name1;
    private String email;
    private String postalZip;
    private String address;

    public Contacto(String contactID, String name, String name1, String email, String postalZip, String address) {
        this.contactID = contactID;
        this.name = name;
        this.name1 = name1;
        this.email = email;
        this.postalZip = postalZip;
        this.address = address;
    }

    public String getContactID() {
        return contactID;
    }

    public String getName() {
        return name;
    }

    public String getName1() {
        return name1;
    }

    public String getEmail() {
        return email;
    }

    public String getPostalZip() {
        return postalZip;
    }

    public String getAddress() {
        return address;
    }

}
