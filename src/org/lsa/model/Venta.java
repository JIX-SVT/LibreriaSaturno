package org.lsa.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private int noCompra;
    private Timestamp fechaCompra;
    private double totalCompra;
    private long cuiCliente;
    private List<DetalleCompra> detalles;

    public Compra() {
        this.detalles = new ArrayList<>();
    }

    public Compra(int noCompra, Timestamp fechaCompra, double totalCompra, long cuiCliente) {
        this.noCompra = noCompra;
        this.fechaCompra = fechaCompra;
        this.totalCompra = totalCompra;
        this.cuiCliente = cuiCliente;
        this.detalles = new ArrayList<>();
    }

    public int getNoCompra() {
        return noCompra;
    }

    public void setNoCompra(int noCompra) {
        this.noCompra = noCompra;
    }

    public Timestamp getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Timestamp fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public long getCuiCliente() {
        return cuiCliente;
    }

    public void setCuiCliente(long cuiCliente) {
        this.cuiCliente = cuiCliente;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(DetalleCompra detalle) {
        if (this.detalles == null) {
            this.detalles = new ArrayList<>();
        }
        this.detalles.add(detalle);
    }

    @Override
    public String toString() {
        return "Compra{" +
                "noCompra=" + noCompra +
                ", fechaCompra=" + fechaCompra +
                ", totalCompra=" + totalCompra +
                ", cuiCliente=" + cuiCliente +
                ", detalles=" + detalles +
                '}';
    }
}