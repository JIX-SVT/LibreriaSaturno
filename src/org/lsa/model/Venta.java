package org.lsa.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.lsa.daoimpl.DetalleVenta;

/**
 *
 * @author Gregory Jerónimo 2026116
 */
public class Venta extends DetalleVenta {

    private int idVenta;
    private Date fecha;
    private double total;
    private int idCajero;
    private int idCliente;
    private List<DetalleVenta> detalles;

    public Venta() {
        this.detalles = new ArrayList<>();
    }

    public Venta(int idVenta, Date fecha, int idCajero, int idCliente) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.idCajero = idCajero;
        this.idCliente = idCliente;
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }

  

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        this.calcularTotal();
    }

    public void agregarDetalle(DetalleVenta detalle) {
        this.detalles.add(detalle);
        this.calcularTotal();
    }

    public void calcularTotal() {
        double suma = 0.0;
        if (this.detalles != null) {
            for (DetalleVenta detalle : this.detalles) {
                suma += detalle.getSubtotal();
            }
        }
        this.total = suma;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fecha=" + fecha +
                ", total=" + total +
                ", idCajero=" + idCajero +
                ", idCliente=" + idCliente +
                ", detalles=" + detalles +
                '}';
    }
}
