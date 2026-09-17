
package org.lsa.dao;

import java.util.List;
import org.lsa.model.MovimientoInventario;



public interface MovimientoInventarioDAO {
    List<MovimientoInventario> listarTodos();
    MovimientoInventario buscarLibro (int idMovimiento);
    boolean insertar(MovimientoInventario movimientoInventario);
    boolean actualizar(MovimientoInventario movimientoInventario);
    boolean eliminar(int idMovimiento);
}
