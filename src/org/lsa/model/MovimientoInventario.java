package org.lsa.model;

import java.util.Date;

public class MovimientoInventario {
    private int idMovimiento;
    private String isbn;
    private String tipoMovimiento;
    private int cantidad;
    private Date fechaMovimiento;
    private String idUsuario;
    private String Observaciòn;

    public MovimientoInventario(int idMovimiento, String isbn, String tipoMovimiento, int cantidad, Date fechaMovimiento, String idUsuario, String Observaciòn) {
        this.idMovimiento = idMovimiento;
        this.isbn = isbn;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.idUsuario = idUsuario;
        this.Observaciòn = Observaciòn;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObservaciòn() {
        return Observaciòn;
    }

    public void setObservaciòn(String Observaciòn) {
        this.Observaciòn = Observaciòn;
    }
    
    
}
