package org.lsa.model;

import java.util.Date;

/**
 * @author Gregory Jerónimo
 */
public class Libro {

    private String isbn;
    private String titulo;
    private Date fechaPublicacion;
    private double precio;
    private int idAutor;
    private String autor;
    private int idCategoria;
    private String nitEditorial;
    private int stock;

    // Constructor completo (idAutor + nombre autor)
    public Libro(String isbn, String titulo, Date fechaPublicacion, double precio, int idAutor, String autor, int idCategoria, String nitEditorial, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idAutor = idAutor;
        this.autor = autor;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.stock = stock;
    }

    // Constructor con idAutor
    public Libro(String isbn, String titulo, Date fechaPublicacion, double precio, int idAutor, int idCategoria, String nitEditorial, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.stock = stock;
    }

    // Constructor con nombre de autor (String)
    public Libro(String isbn, String titulo, Date fechaPublicacion, double precio, int idCategoria, String nitEditorial, String autor, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.autor = autor;
        this.stock = stock;
    }

    // Constructor de 7 parámetros
    public Libro(String isbn, String titulo, Date fechaPublicacion, double precio, int idCategoria, String nitEditorial, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.stock = stock;
    }

    // Getters y Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNitEditorial() { return nitEditorial; }
    public void setNitEditorial(String nitEditorial) { this.nitEditorial = nitEditorial; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public boolean esStockCritico() {
        return this.stock <= 10;
    }

    @Override
    public String toString() {
        return titulo + " - Q" + String.format("%.2f", precio) + " [Stock: " + stock + "]";
    }
}