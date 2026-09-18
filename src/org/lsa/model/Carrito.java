package org.lsa.model;

public class Carrito {
    private String isbn;
    private String titulo;
    private double precio;
    private int stock;
    private double subtotal;

    public Carrito(String isbn, String titulo, double precio, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precio = precio;
        this.stock = stock;
        this.subtotal = precio * stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public int getStock() {
            return stock;
        }

    public void setStock(int stock) {
        this.stock = stock;
        this.subtotal = this.precio * this.stock;
    }

    public double getSubtotal() {
        return this.precio * this.stock;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}