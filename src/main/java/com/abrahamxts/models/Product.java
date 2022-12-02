package com.abrahamxts.models;

public class Product {

    private Float identificador;

    private String nombre;

    private Float precio;

    public Product(Float identificador, String nombre, Float precio) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.precio = precio;
    }

    public Product() {}

    public Float getIdProduct() {
        return identificador;
    }

    public void setIdProduct(Float identificador) {
        this.identificador = identificador;
    }

    public String getProductName() {
        return nombre;
    }

    public void setProductName(String nombre) {
        this.nombre = nombre;
    }

    public Float getPrice() {
        return precio;
    }

    public void setPrice(Float precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Product {" + "identificador = " + identificador + ", nombre = " + nombre + ", precio = " + precio + '}';
    }
}