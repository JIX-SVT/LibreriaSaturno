package org.lsa.model;

import java.util.Date;

public class Libro {
    private String isbn;
    private String titulo;
    private Date fechaPublicacion;
    private double precio;
    private int idCategoria;
    private String nitEditorial;
    private String autor;
    private int stockActual;

    public Libro() {}

    public Libro(String isbn, String titulo, Date fechaPublicacion, double precio, int idCategoria, String nitEditorial, String autor, int stockActual) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.autor = autor;
        this.stockActual = stockActual;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNitEditorial() { return nitEditorial; }
    public void setNitEditorial(String nitEditorial) { this.nitEditorial = nitEditorial; }

  
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
}