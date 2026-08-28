package org.lsa.dao;

import java.sql.SQLException; 
import java.sql.Connection; 
import java.sql.ResultSet; 
import java.sql.CallableStatement; 
import org.lsa.model.Usuario;
import org.lsa.utils.Conexion;
 
public class UsuarioDAO {
 
    public Usuario iniciarSesion(String username, String passwordHash) {
        Usuario usuario = null;
        String sql = "{call sp_iniciar_sesion(?, ?)}";
 
        try(Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consultaCall = conexion.prepareCall(sql)) {
 
            consultaCall.setString(1, username);
            consultaCall.setString(2, passwordHash);
 
            try(ResultSet tablaResultado = consultaCall.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    usuario.setId(tablaResultado.getInt(1)); 
                    usuario.setUsrname(tablaResultado.getString(2)); 
                    usuario.setRol(tablaResultado.getString(3));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en iniciar sesion: " + e.getMessage());
        }
 
        return usuario;
    }
 
    public boolean registrarUsuario(String username, String password, String rol) {
        boolean registroExitoso = false;
        String sql = "{call sp_registrar_usuario(?, ?, ?)}";
 
        try(Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consultaCall = conexion.prepareCall(sql)) {
 
            consultaCall.setString(1, username);
            consultaCall.setString(2, password);
            consultaCall.setString(3, rol);
 
            int filasAfectadas = consultaCall.executeUpdate();
            if (filasAfectadas > 0) {
                registroExitoso = true;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
        }
 
        return registroExitoso;
    }                             
}