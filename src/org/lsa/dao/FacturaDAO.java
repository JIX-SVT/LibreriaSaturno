package org.lsa.dao;


import java.util.ArrayList;
import org.lsa.model.Factura;

public interface FacturaDAO {
    ArrayList<Factura> buscarFactura(int noVenta);
}