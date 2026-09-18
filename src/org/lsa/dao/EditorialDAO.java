
package org.lsa.dao;

import org.lsa.model.Editorial;


public interface EditorialDAO extends CRUD<Editorial, String>{

    public boolean crear(Editorial editorial);

 
    
}
