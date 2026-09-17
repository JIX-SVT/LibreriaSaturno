package org.lsa.dao;

import java.util.List;
import org.lsa.model.DetalleVenta;

public interface DetalleVentaDAO {
      List<DetalleVenta> listar();
    DetalleVenta buscar (int idDetalleventa);
    boolean insertar(DetalleVenta detalleVenta);
    boolean actualizar(DetalleVenta detalleVenta);
    boolean eliminar(int idDetalleventa);   
}