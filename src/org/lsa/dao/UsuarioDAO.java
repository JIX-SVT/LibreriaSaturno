package org.lsa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.lsa.utils.ConexionSingleton;

public class UsuarioDAO{
    
    public boolean validarContrasenaActual(int idUsuario, String contrasenaIngresada) {
        String sql = "SELECT password FROM usuario WHERE id = ?";
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String passAlmacenada = rs.getString("password");
                    return passAlmacenada.equals(contrasenaIngresada);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}