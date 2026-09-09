package org.lsa.model;

import java.sql.Timestamp;

public class Venta {

    private int noVenta;
    private Timestamp fechaVenta;
    private double totalVenta;
    private Long cuiCliente;

    public Venta() {
    }

    public Venta(int noVenta, Timestamp fechaVenta, double totalVenta, Long cuiCliente) {
        this.noVenta = noVenta;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.cuiCliente = cuiCliente;
    }


    public int getNoVenta() {
        return noVenta;
    }

    public void setNoVenta(int noVenta) {
        this.noVenta = noVenta;
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

    public Long getCuiCliente() {
        return cuiCliente;
    }

    public void setCuiCliente(Long cuiCliente) {
        this.cuiCliente = cuiCliente;
    }
}