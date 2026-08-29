package org.lsa.model;

import java.sql.Timestamp;

public class Inventario {
    private int id;
    private int libroId;
    private String tipoMovimiento; // "INGRESO" o "SALIDA"
    private int cantidad;
    private Timestamp fecha;

    public Inventario() {}

    public Inventario(int id, int libroId, String tipoMovimiento, int cantidad, Timestamp fecha) {
        this.id = id;
        this.libroId = libroId;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLibroId() { return libroId; }
    public void setLibroId(int libroId) { this.libroId = libroId; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}