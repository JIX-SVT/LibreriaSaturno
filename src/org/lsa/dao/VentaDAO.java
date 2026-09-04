package org.lsa.dao;

import java.util.List;
import org.lsa.model.Venta;

public interface VentaDAO {

    int registrarVentA(Venta venta);

    Venta buscarPorNoCompra(int noCompra);

    List<Venta> listarTodas();
}
