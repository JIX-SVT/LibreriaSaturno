package org.lsa.dao;

import javafx.collections.ObservableList;
import org.lsa.model.DetalleVenta;

public interface DetalleVentaDAO extends CRUD<DetalleVenta, Integer> {

    public boolean registrarVentaCompleta(long cui, double totalVenta, ObservableList<DetalleVenta> listaDetalles);
    // Si en el futuro necesitas consultas específicas, se declaran aquí
}