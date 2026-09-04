package org.lsa.dao;

import java.util.List;
import org.lsa.model.DetalleVenta;

public interface DetalleVentaDAO {

    boolean registrarDetalle(DetalleVenta detalle);

    boolean registrarListaDetalles(List<DetalleVenta> detalles);

    List<DetalleVenta> listarPorNoCompra(int noCompra);
}