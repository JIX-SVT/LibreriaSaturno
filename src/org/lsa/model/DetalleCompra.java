package org.lsa.model;

public class DetalleCompra {

    private int idDetalleCompra;
    private int noCompra;
    private String isbn;

    public DetalleCompra() {
    }

    public DetalleCompra(int idDetalleCompra, int noCompra, String isbn) {
        this.idDetalleCompra = idDetalleCompra;
        this.noCompra = noCompra;
        this.isbn = isbn;
    }

    public int getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(int idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public int getNoCompra() {
        return noCompra;
    }

    public void setNoCompra(int noCompra) {
        this.noCompra = noCompra;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" + 
               "idDetalleCompra=" + idDetalleCompra + 
               ", noCompra=" + noCompra + 
               ", isbn='" + isbn + '\'' + 
               '}';
    }
}