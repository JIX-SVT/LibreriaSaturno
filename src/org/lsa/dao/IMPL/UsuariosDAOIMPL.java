package org.lsa.dao.IMPL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.lsa.dao.UsuariosDAO;
import org.lsa.model.Usuarios;
import org.lsa.utils.Conexion;

public class UsuariosDAOIMPL implements UsuariosDAO {

    @Override
    public boolean insertar(Usuarios usuarios) {
        String sql = "{call sp_insertarusuario(?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setString(1, usuarios.getUsuario());
            cs.setString(2, usuarios.getPasswordHash());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Insertar Usuario]: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuarios> listar() {
        List<Usuarios> usuarios = new ArrayList<>();
        String consulta = "{call sp_listarusuarios()}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta);
             ResultSet tablaResultado = consultaCall.executeQuery()) {

while (tablaResultado.next()) {
    usuarios.add(new Usuarios(
        tablaResultado.getString("id"),
        tablaResultado.getString("nombre"),       
        tablaResultado.getString("_username"),      
        tablaResultado.getString("_Apellido"),   
        null,                                  
        tablaResultado.getString("rol"),            
        tablaResultado.getString("correo"),        
        tablaResultado.getString("estado"),          
        tablaResultado.getString("_passwordhash"),    
        tablaResultado.getString("fecha_creacion"),   
        tablaResultado.getString("fecha_actualizacion") 
    ));
            }

        } catch (SQLException e) {
            System.err.println("Error al Listar Usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    @Override
    public Usuarios buscar(String username) {
        Usuarios usuario = null;
        String consultaSQL = "{call sp_buscarusuario(?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consultaSQL)) {

            consultaCall.setString(1, username);
            try (ResultSet tablaResultado = consultaCall.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuarios();
                    usuario.setUsuario(tablaResultado.getString("username"));
                    usuario.setPasswordHash(tablaResultado.getString("passwordhash"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar su usuario: " + e.getMessage());
        }

        return usuario;
    }

    @Override
    public boolean actualizar(Usuarios usuarios) {
        String sql = "{call sp_actualizarusuarios(?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, usuarios.getUsuario());
            cs.setString(2, usuarios.getPasswordHash());

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Actualizar Usuario]: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(String username) {
        String sql = "{call sp_eliminarusuarios(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setString(1, username);

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ErrorEliminar Usuario: " + e.getMessage());
            return false;
        }
    }
}