package org.lsa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.lsa.utils.ConexionSingleton;

public class InventarioDAO {

    public boolean registrarMovimiento(int libroId, String tipo, int cantidad) {
        String sqlMov = "INSERT INTO inventario (libro_id, tipo_movimiento, cantidad) VALUES (?, ?, ?)";
        String sqlStock = tipo.equalsIgnoreCase("INGRESO") 
                ? "UPDATE libros SET stock_actual = stock_actual + ? WHERE id = ?"
                : "UPDATE libros SET stock_actual = stock_actual - ? WHERE id = ? AND stock_actual >= ?";

        try (Connection conn = ConexionSingleton.getInstancia().getConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtMov = conn.prepareStatement(sqlMov);
                 PreparedStatement stmtStock = conn.prepareStatement(sqlStock)) {

                stmtMov.setInt(1, libroId);
                stmtMov.setString(2, tipo);
                stmtMov.setInt(3, cantidad);
                stmtMov.executeUpdate();

                stmtStock.setInt(1, cantidad);
                stmtStock.setInt(2, libroId);
                if (tipo.equalsIgnoreCase("SALIDA")) {
                    stmtStock.setInt(3, cantidad);
                }

                int filas = stmtStock.executeUpdate();
                if (filas > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}