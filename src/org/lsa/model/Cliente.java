
package org.lsa.model;


public class Cliente {
    private int cui;
    private String nombreCliente;
    private String apellidoCliente;
    private String correoElectronico;

    public Cliente(int cui, String nombreCliente, String apellidoCliente, String correoElectronico) {
        this.cui = cui;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.correoElectronico = correoElectronico;
    }

    public int getCui() {
        return cui;
    }

    public void setCui(int cui) {
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
        
      @Override
    public String toString() {
        return "clientes{" +
                "cui=" + cui +
                ", nombre_cliente=" + nombreCliente +
                ", apellido_cliente=" + apellidoCliente +
                ", correo_electronico=" + correoElectronico +
                '}';
    }
}

    
