
package org.lsa.dao;



import org.lsa.model.Categoria;


public interface CategoriaDAO extends CRUD<Categoria, Integer>{

    public boolean crear(Categoria categoria);
    
}