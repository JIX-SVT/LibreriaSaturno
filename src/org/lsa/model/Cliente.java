package org.lsa.model;

public class Cliente {
    private long cui;
    private String nombreCliente;
    private String apellidoCliente;
    private String correoElectronico;

    public Cliente(long cui, String nombreCliente, String apellidoCliente, String correoElectronico) {
        this.cui = cui;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.correoElectronico = correoElectronico;
    }

    public long getCui() {
        return cui;
    }

    // CORREGIDO: Cambiado de 'int cui' a 'long cui'
    public void setCui(long cui) {
        this.cui = cui;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    // CORREGIDO: Formato limpio para que se despliegue correctamente en el ComboBox
    @Override
    public String toString() {
        return nombreCliente + " " + apellidoCliente;
    }
}