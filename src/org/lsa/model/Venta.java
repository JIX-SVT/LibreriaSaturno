package org.lsa.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Venta {

    private int idVenta;
    private Timestamp fechaVenta; 
    private double totalVenta;
    private String subTotal;
    private long cuiCliente;
    private double descuento;
    private String estado;
    private int id_usuario;
    private List<DetalleVenta> detalles;

    public Venta() {
        this.detalles = new ArrayList<>();
    }

    public Venta(int idVenta, Timestamp fechaVenta, double totalVenta, String subTotal, long cuiCliente, double descuento, String estado, int id_usuario, List<DetalleVenta> detalles) {
        this.idVenta = idVenta;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.subTotal = subTotal;
        this.cuiCliente = cuiCliente;
        this.descuento = descuento;
        this.estado = estado;
        this.id_usuario = id_usuario;
        this.detalles = detalles;
    }

  
 
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public long getCuiCliente() {
        return cuiCliente;
    }

    public void setCuiCliente(long cuiCliente) {
        this.cuiCliente = cuiCliente;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
    
      @Override
    public String toString() {
        return "ventas{" +
                "id_venta=" + idVenta +
                ", fecha_venta=" + fechaVenta +
                ", descuento=" + subTotal +
                ", cui_Cliente=" + cuiCliente +
                 ", estado=" + estado +
                 ", cui_Cliente=" + cuiCliente +
                ", id_usuario=" + id_usuario +
                '}';
    }

    public String getidVenta() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}