package org.lsa.model;


public class Usuarios  {
    private String ID;
    private String Nombre;
    private String Usuario;
    private String Apellido;
    private String Contraseña;
    private String Rol;
    private String Correo;
    private String Estado;
    private String PasswordHash;
    private String FechaCreacion;
    private String FechaActualizacion;
    public Usuarios(){
  
}

    public Usuarios(String ID, String Nombre, String Usuario, String Apellido, String Contraseña, String Rol, String Correo, String Estado, String PasswordHash, String FechaCreacion, String FechaActualizacion) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Usuario = Usuario;
        this.Apellido = Apellido;
        this.Contraseña = Contraseña;
        this.Rol = Rol;
        this.Correo = Correo;
        this.Estado = Estado;
        this.PasswordHash = PasswordHash;
        this.FechaCreacion = FechaCreacion;
        this.FechaActualizacion = FechaActualizacion;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String Contraseña) {
        this.Contraseña = Contraseña;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String Rol) {
        this.Rol = Rol;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String PasswordHash) {
        this.PasswordHash = PasswordHash;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

    public String getFechaActualizacion() {
        return FechaActualizacion;
    }

    public void setFechaActualizacion(String FechaActualizacion) {
        this.FechaActualizacion = FechaActualizacion;
    }
    
 }
