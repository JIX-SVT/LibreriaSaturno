package org.lsa.model;

public class Categoria {
    private int idCategoria;
    private String nombre;

    public Categoria() {}

    public Categoria(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public int getIdCategoria() { 
        return idCategoria; 
    }
    
    public void setIdCategoria(int idCategoria) { 
        this.idCategoria = idCategoria; 
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    // Métodos delegados para mantener compatibilidad con el DAO y el Controller
    public String getNombreCategoria() {
        return nombre;
    }

    public void setNombreCategoria(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre; 
    }
}