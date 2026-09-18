package org.lsa.model;

public class Editorial {
    private String nit;
    private String nombreEditorial;
    private String telefonoEditorial;
    private String direccionEditorial;

    // 1. Constructor vacío (indispensable)
    public Editorial() {
    }

    // 2. Constructor de 2 parámetros (el que exige LibroDAOImpl)
    public Editorial(String nit, String nombreEditorial) {
        this.nit = nit;
        this.nombreEditorial = nombreEditorial;
    }

    // 3. Constructor completo (opcional)
    public Editorial(String nit, String nombreEditorial, String telefonoEditorial, String direccionEditorial) {
        this.nit = nit;
        this.nombreEditorial = nombreEditorial;
        this.telefonoEditorial = telefonoEditorial;
        this.direccionEditorial = direccionEditorial;
    }

    // Getters y Setters
    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombreEditorial() {
        return nombreEditorial;
    }

    public void setNombreEditorial(String nombreEditorial) {
        this.nombreEditorial = nombreEditorial;
    }

    public String getTelefonoEditorial() {
        return telefonoEditorial;
    }

    public void setTelefonoEditorial(String telefonoEditorial) {
        this.telefonoEditorial = telefonoEditorial;
    }

    public String getDireccionEditorial() {
        return direccionEditorial;
    }

    public void setDireccionEditorial(String direccionEditorial) {
        this.direccionEditorial = direccionEditorial;
    }

    @Override
    public String toString() {
        return nombreEditorial; // Útil para mostrar el nombre en el ComboBox del Dashboard
    }
}