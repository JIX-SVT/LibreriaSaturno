package org.lsa.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.lsa.model.DetalleCompra;
import org.lsa.model.Compra;
import org.lsa.utils.Conexion;

public class CompraService {

    public boolean validarStock(String isbn, int cantidad) {
        String sql = "{call sp_buscarlibro(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, isbn);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    int stockActual = rs.getInt("stock");
                    return stockActual >= cantidad;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error [Validar Stock]: " + e.getMessage());
        }
        return false;
    }

    public boolean procesarCompra(Compra compra, List<DetalleCompra> detalles) {
        for (DetalleCompra det : detalles) {
            if (!validarStock(det.getIsbn(), 1)) {
                System.err.println("Stock insuficiente para el libro ISBN: " + det.getIsbn());
                return false;
            }
        }

        Connection con = null;
        try {
            con = Conexion.getInstancia().conectar();
            con.setAutoCommit(false); // Iniciar Transacción

            String sqlCompra = "{call sp_insertarcompra(?, ?, ?)}";
            int noCompraGenerado = -1;

            try (CallableStatement csCompra = con.prepareCall(sqlCompra)) {
                csCompra.setDouble(1, compra.getTotalCompra());
                csCompra.setLong(2, compra.getCuiCliente());
                csCompra.registerOutParameter(3, Types.INTEGER); 
                csCompra.executeUpdate();
                noCompraGenerado = csCompra.getInt(3);
            }

            if (noCompraGenerado <= 0) {
                con.rollback();
                return false;
            }

            String sqlDetalle = "{call sp_insertardetallecompra(?, ?)}";
            String sqlStock = "{call sp_descontarstock(?, ?)}";

            try (CallableStatement csDetalle = con.prepareCall(sqlDetalle);
                 CallableStatement csStock = con.prepareCall(sqlStock)) {

                for (DetalleCompra det : detalles) {
                    det.setNoCompra(noCompraGenerado);

                    csDetalle.setInt(1, det.getNoCompra());
                    csDetalle.setString(2, det.getIsbn());
                    csDetalle.executeUpdate();

                    csStock.setString(1, det.getIsbn());
                    csStock.setInt(2, 1);
                    csStock.executeUpdate();
                }
            }

            con.commit(); 
            return true;

        } catch (SQLException e) {
            System.err.println("Error en la transacción de compra. Ejecutando Rollback... " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Error al ejecutar Rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }
}