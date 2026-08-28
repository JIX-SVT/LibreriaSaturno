package org.lsa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.lsa.utils.ConexionSingleton;

public class UsuarioDAO {
    
    public boolean validarContrasenaActual(int idUsuario, String contrasenaIngresada) {
            String sql = "SELECT 1 FROM usuarios WHERE id = ? AND password_hash = SHA2(?, 256)";
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, contrasenaIngresada);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password_hash = SHA2(?, 256) WHERE id = ?";
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevaPassword);
            pstmt.setInt(2, idUsuario);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}