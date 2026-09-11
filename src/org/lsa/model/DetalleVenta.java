package org.lsa.model;

public class DetalleVenta {

   
    
    private int idDetalleventa;
    private int noVenta;
    private String isbn;
    private int cantidad;
    private double precioUnitario;
    private double subTotalDetalle;

    public DetalleVenta(int idDetalleventa, int noVenta, String isbn, int cantidad, double precioUnitario, double subTotalDetalle) {
        this.idDetalleventa = idDetalleventa;
        this.noVenta = noVenta;
        this.isbn = isbn;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subTotalDetalle = subTotalDetalle;
    }
    @Override
    public String toString() {
        return "detalle_venta{" + 
               "id_detalle=" + idDetalleventa + 
               ", id_Venta=" + noVenta + 
                ", cantidad=" + cantidad + 
                ", precio_unitario=" + precioUnitario + 
                ", subtotal=" + subTotalDetalle + 
               ", isbn='" + isbn + '\'' + 
               '}';
    }

    public int getIdDetalleventa() {
        return idDetalleventa;
    }

    public void setIdDetalleventa(int idDetalleventa) {
        this.idDetalleventa = idDetalleventa;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getSubTotalDetalle() {
        return subTotalDetalle;
    }

    public void setSubTotalDetalle(double subTotalDetalle) {
        this.subTotalDetalle = subTotalDetalle;
    }
}