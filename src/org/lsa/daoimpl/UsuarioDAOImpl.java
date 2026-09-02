package org.lsa.daoimpl;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.lsa.dao.UsuarioDAO;
import org.lsa.model.Usuario;
import org.lsa.utils.Conexion;

public class UsuarioDAOImpl extends UsuarioDAO {

    @Override
    public Usuario autenticar(String correo, String contrasena) {
        Usuario usuario = null;
        String consulta = "{call sp_autenticarusuario(?, ?)}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta)) {
             
            consultaCall.setString(1, correo);
            consultaCall.setString(2, contrasena);
            
            try (ResultSet tablaResultado = consultaCall.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    // Asegúrate de que los nombres de los campos coincidan con los que devuelve tu procedimiento almacenado
                    usuario.setIdUsuario(tablaResultado.getInt("id_usuario"));
                    usuario.setNombreUsuario(tablaResultado.getString("nombre_usuario"));
                    usuario.setCorreo(tablaResultado.getString("correo"));
                    usuario.setRol(tablaResultado.getString("rol"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al autenticar Usuario: " + e.getMessage());
        }
        
        return usuario;
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String consulta = "{call sp_listarusuarios()}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta);
             ResultSet tablaResultado = consultaCall.executeQuery()) {
             
            while (tablaResultado.next()) {
                usuarios.add(new Usuario(
                        tablaResultado.getInt("id_usuario"),
                        tablaResultado.getString("nombre_usuario"),
                        tablaResultado.getString("correo"),
                        tablaResultado.getString("rol")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario buscarUsuario(int idUsuario) {
        Usuario usuario = new Usuario();
        String consultaSQL = "{call sp_buscarusuario(?)}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consultaSQL)) {
             
            consultaCall.setInt(1, idUsuario);
            try (ResultSet tablaResultado = consultaCall.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario.setIdUsuario(tablaResultado.getInt("id_usuario"));
                    usuario.setNombreUsuario(tablaResultado.getString("nombre_usuario"));
                    usuario.setCorreo(tablaResultado.getString("correo"));
                    usuario.setRol(tablaResultado.getString("rol"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar Usuario: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public boolean insertar(Usuario usuario) {
        String consulta = "{call sp_insertarusuario(?, ?, ?, ?)}";
        
        try (Connection conexion = new Conexion().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta)) {
             
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

    @Override
    public boolean actualizar(Usuario usuario) {
        return false;
    }

    @Override
    public boolean eliminar(int idUsuario) {
        return false;
    }
}