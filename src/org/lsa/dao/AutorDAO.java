
package org.lsa.dao;

import org.lsa.model.Autor;


public interface AutorDAO extends CRUD<Autor, Integer>{

    public boolean crear(Autor autor);
    
}

