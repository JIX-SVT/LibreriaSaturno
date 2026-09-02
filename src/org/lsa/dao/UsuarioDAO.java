package org.lsa.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.lsa.model.Usuario;
import org.lsa.utils.Conexion;

public class UsuarioDAO {

    public boolean validarContrasenaActual(int idUsuario, String contrasenaIngresada) {
        String sql = "SELECT 1 FROM usuarios WHERE id_usuario = ? AND contrasena = SHA2(?, 256)";
        try (Connection conn = new Conexion().conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, contrasenaIngresada);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al validar contraseña actual: " + e.getMessage());
        }
        return false;
    }

    public boolean actualizarPassword(int idUsuario, String nuevaPassword) {
        String sql = "UPDATE usuarios SET contrasena = SHA2(?, 256) WHERE id_usuario = ?";
        try (Connection conn = new Conexion().conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevaPassword);
            pstmt.setInt(2, idUsuario);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar contraseña: " + e.getMessage());
            return false;
        }
    }

    public Usuario autenticar(String username, String password) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
        
        try (Connection conexion = new Conexion().conectar();
             PreparedStatement consultaPreparada = conexion.prepareStatement(sql)) {
             
            consultaPreparada.setString(1, username);
            consultaPreparada.setString(2, password);
            
            try (ResultSet tablaResultado = consultaPreparada.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(tablaResultado.getInt("id_usuario"));
                    usuario.setNombreUsuario(tablaResultado.getString("nombre_usuario"));
                    usuario.setCorreo(tablaResultado.getString("correo"));
                    usuario.setRol(tablaResultado.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en la consulta de autenticación: " + e.getMessage());
        }
        
        return usuario;
    }

    public boolean registrarUsuario(String username, String password, String rol) {
        boolean registroExitoso = false;
        String sql = "{call sp_registrar_usuario(?, ?, ?)}";
        
        try (Connection conexion = new Conexion().conectar();
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

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "{call sp_listarusuarios()}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(sql);
             ResultSet tablaResultado = consultaCall.executeQuery()) {
             
            while (tablaResultado.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(tablaResultado.getInt("id_usuario"));
                usuario.setNombreUsuario(tablaResultado.getString("nombre_usuario"));
                usuario.setCorreo(tablaResultado.getString("correo"));
                usuario.setRol(tablaResultado.getString("rol"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar Usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario buscarPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "{call sp_buscarusuario(?)}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(sql)) {
             
            consultaCall.setInt(1, idUsuario);
            try (ResultSet tablaResultado = consultaCall.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(tablaResultado.getInt("id_usuario"));
                    usuario.setNombreUsuario(tablaResultado.getString("nombre_usuario"));
                    usuario.setCorreo(tablaResultado.getString("correo"));
                    usuario.setRol(tablaResultado.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Usuario: " + e.getMessage());
        }
        return usuario;
    }

    public boolean insertar(Usuario usuario) {
        String sql = "{call sp_insertarusuario(?, ?, ?, ?)}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(sql)) {
             
            consultaCall.setString(1, usuario.getNombreUsuario());
            consultaCall.setString(2, usuario.getCorreo());
            consultaCall.setString(3, usuario.getContrasena());
            consultaCall.setString(4, usuario.getRol());
            
            return consultaCall.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear Usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario usuario) {
        return false;
    }

    public boolean eliminar(int idUsuario) {
        return false;
    }
}