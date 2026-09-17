

package org.lsa.dao;

import java.util.List;
import org.lsa.model.Libro;

/**
 *
 * @author informatica
 */
public interface LibroDAO{
    List<Libro> listarTodos();
    Libro buscarLibro (String isbn);
    boolean insertar(Libro libro);
    boolean actualizar(Libro libro);
    boolean eliminar(String isbn);
    Libro filtrarLibros();
public List<Libro> obtenerLibrosStockCritico();
    
 
}
