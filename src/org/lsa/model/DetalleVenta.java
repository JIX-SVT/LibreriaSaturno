package org.lsa.model;

public class DetalleVenta {

    private int idDetalleVenta;
    private int noVenta;
    private String isbn;

    public DetalleVenta(int idDetalleVenta, int noVenta, String isbn) {
        this.idDetalleVenta = idDetalleVenta;
        this.noVenta = noVenta;
        this.isbn = isbn;
    }

    public DetalleVenta() {
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    public int getNoVenta() {
        return noVenta;
    }

    public void setNoVenta(int noVenta) {
        this.noVenta = noVenta;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" + "idDetalleVenta=" + idDetalleVenta + ", noVenta=" + noVenta + ", isbn=" + isbn + '}';
    }
    
}