package org.lsa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.lsa.utils.ConexionSingleton;

public class InventarioDAO {

    public boolean registrarMovimiento(String isbn, String tipo, int cantidad) {
        String sqlMov = "INSERT INTO inventario (isbn_libro, tipo_movimiento, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionSingleton.getInstancia().getConexion()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement stmtMov = conn.prepareStatement(sqlMov)) {

                stmtMov.setString(1, isbn);
                stmtMov.setString(2, tipo);
                stmtMov.setInt(3, cantidad);

                int filas = stmtMov.executeUpdate();
                
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