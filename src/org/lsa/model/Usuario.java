package org.lsa.model;

public class Usuario {
    private int idUsuario;
    private String nombreUsuario;
    private String correo;
    private String contrasena;
    private String rol;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor con 4 parámetros (para listar)
    public Usuario(int idUsuario, String nombreUsuario, String correo, String rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.correo = correo;
        this.rol = rol;
    }

    // Getters y Setters con los nombres EXACTOS que pide el error
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}