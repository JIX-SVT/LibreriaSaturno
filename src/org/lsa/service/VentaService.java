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
 
    public boolean procesarVenta(Venta venta, List<DetalleVenta> detalles) {
        for (DetalleVenta det : detalles) {
            if (!validarStock(det.getIsbn(), 1)) {
                System.err.println("Stock insuficiente para el libro ISBN: " + det.getIsbn());
                return false;
            }
        }
 
        Connection con = null;
        try {
            con = Conexion.getInstancia().conectar();
            con.setAutoCommit(false);
 
            // A. Registrar Venta Maestra (compras)
            String sqlVenta = "{call sp_insertarventa(?, ?, ?)}";
            int noVentaGenerado = -1;
 
            try (CallableStatement csVenta = con.prepareCall(sqlVenta)) {
                csVenta.setDouble(1, venta.getTotalVenta());
                csVenta.setLong(2, venta.getCuiCliente());
                csVenta.registerOutParameter(3, Types.INTEGER); 
                csVenta.executeUpdate();
                noVentaGenerado = csVenta.getInt(3);
            }
 
            if (noVentaGenerado <= 0) {
                con.rollback();
                return false;
            }
 
            String sqlDetalle = "{call sp_insertardetallecompra(?, ?)}";
            String sqlStock = "{call sp_descontarstock(?, ?)}";
 
            for (DetalleVenta det : detalles) {
                det.setNoVenta(noVentaGenerado);
 
                try (CallableStatement csDetalle = con.prepareCall(sqlDetalle)) {
                    csDetalle.setInt(1, det.getNoVenta());
                    csDetalle.setString(2, det.getIsbn());
                    csDetalle.executeUpdate();
                }
 
                try (CallableStatement csStock = con.prepareCall(sqlStock)) {
                    csStock.setString(1, det.getIsbn());
                    csStock.setInt(2, 1);
                    csStock.executeUpdate();
                }
            }
 
            con.commit();
            return true;
 
        } catch (SQLException e) {
            System.err.println("Error en la transacción de venta. Ejecutando Rollback... " + e.getMessage());
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