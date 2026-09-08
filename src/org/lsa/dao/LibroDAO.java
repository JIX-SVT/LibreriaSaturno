

package org.lsa.dao;

import java.util.List;
import org.lsa.model.Libro;

/**
 *
 * @author informatica
 */
public interface LibroDAO{
    List<Libro> listarTodos();
    Libro buscarLibro (int isbn);
    boolean insertar(Libro libro);
    boolean actualizar(Libro libro);
    boolean eliminar(int isbn);
    Libro filtrarLibros();
    
 
}
