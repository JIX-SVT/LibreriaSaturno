package org.lsa.model;

public class Usuario {
    
    
    private int id;
    private String username;
    private String correo;
    private String contrasena;
    private String rol;
    private boolean activo;
    private String nombre;
    private String apellido;

    
    public Usuario() {
    }

    
    public Usuario(int id, String username, String correo, String rol) {
        this.id = id;
        this.username = username;
        this.correo = correo;
        this.rol = rol;
    }

    // --- GETTERS Y SETTERS EXACTOS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}