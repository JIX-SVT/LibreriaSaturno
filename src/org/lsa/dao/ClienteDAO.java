
package org.lsa.dao;

import java.util.List;
import org.lsa.model.Cliente;


public interface ClienteDAO {  
      List<Cliente> listarTodos();
    Cliente buscarLibro (long cui);
    boolean insertar(Cliente cliente);
    boolean actualizar(Cliente cliente);
    boolean eliminar(long cui);   
}
