package org.lsa.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.lsa.model.DetalleVenta;
import org.lsa.model.Venta;
import org.lsa.utils.Conexion;

public class VentaService {

    // T2.17: Validar stock disponible
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

    // T2.18 & T2.19: Transacción Completa de Venta (Maestro + Detalle + Stock)
    public boolean procesarVenta(Venta venta, List<DetalleVenta> detalles) {
        
        // 1. Validar existencias en inventario antes de iniciar
        for (DetalleVenta det : detalles) {
            if (!validarStock(det.getIsbn(), 1)) {
                System.err.println("Stock insuficiente para el libro ISBN: " + det.getIsbn());
                return false;
            }
        }

        Connection con = null;
        try {
            con = Conexion.getInstancia().conectar();
            con.setAutoCommit(false); // Iniciar Transacción JDBC

            // A. Registrar Venta Maestra (compras)
            String sqlVenta = "{call sp_insertarventa(?, ?, ?)}";
            int noVentaGenerado = -1;

            try (CallableStatement csVenta = con.prepareCall(sqlVenta)) {
                csVenta.setDouble(1, venta.getTotalVenta());
                csVenta.setLong(2, venta.getCuiCliente());
                csVenta.registerOutParameter(3, Types.INTEGER); // Asigna no_compra generado
                
                csVenta.executeUpdate();
                noVentaGenerado = csVenta.getInt(3);
            }

            if (noVentaGenerado <= 0) {
                con.rollback();
                return false;
            }

            // B. Registrar Detalles y Descontar Stock
            String sqlDetalle = "{call sp_insertardetallecompra(?, ?)}";
            String sqlStock = "{call sp_descontarstock(?, ?)}";

            for (DetalleVenta det : detalles) {
                // Asignar el ID recién generado de la venta maestro
                det.setNoVenta(noVentaGenerado);

                // Insertar detalle_compra
                try (CallableStatement csDetalle = con.prepareCall(sqlDetalle)) {
                    csDetalle.setInt(1, det.getNoVenta());
                    csDetalle.setString(2, det.getIsbn());
                    csDetalle.executeUpdate();
                }

                // T2.19: Descontar 1 unidad del stock por libro
                try (CallableStatement csStock = con.prepareCall(sqlStock)) {
                    csStock.setString(1, det.getIsbn());
                    csStock.setInt(2, 1);
                    csStock.executeUpdate();
                }
            }

            // Commit final: Confirmar todos los cambios
            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error en la transacción de venta. Ejecutando Rollback... " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback(); // T2.20 Rollback en caso de falla
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