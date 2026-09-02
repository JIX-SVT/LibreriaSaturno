
package org.lsa.dao;

import java.util.List;
import org.lsa.model.Usuarios;

public interface UsuariosDAO {
    
    boolean insertar(Usuarios usuarios);
    List<Usuarios> listar();
    Usuarios buscar(String username);
    boolean actualizar(Usuarios Usuarios);
    boolean eliminar(String username);  
}

