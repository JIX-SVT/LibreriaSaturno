
package org.lsa.dao.IMPL;

import com.mysql.cj.jdbc.CallableStatement;
import com.sun.jdi.connect.spi.Connection;
import java.util.ArrayList;
import java.util.List;
import org.lsa.dao.UsuariosDAO;
import org.lsa.model.Usuarios;
import org.lsa.utils.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuariosDAOIMPL implements UsuariosDAO{
    
    @Override
    public boolean insertar(Usuarios usuarios) {
        return false;
    }

    @Override
    public List<Usuarios> listar() {
       
        List<Usuarios> editoriales = new ArrayList<>();
        
        String consulta = "{call sp_listarusuarios()}";
        
        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta);
              ResultSet tablaResultado = consultaCall.executeQuery();) {
                
                
              while (tablaResultado.next()) {
                  Usuarios.add(new Usuarios(
                          tablaResultado.getString("_username"),
                          tablaResultado.getString("_passwordhash"),
                          tablaResultado.getString("_confirmarPassword")
                  ));
            }
         
              
        } catch (SQLException e) {
            System.err.print("Error al Listar Editoriales " + e.getMessage());
        }
      
         return editoriales;
    }

    @Override
    public Usuarios buscar(String username) {
             Usuarios usuarios = new Usuarios();

        String consultaSQL = "{call sp_buscareditorial(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar(); 
                CallableStatement consultaCall = conexion.prepareCall(consultaSQL);) {
            consultaCall.setString(1, username);
            ResultSet tablaResultado = consultaCall.executeQuery();
            if (tablaResultado.next()) {
                usuarios.setUsername(tablaResultado.getString("username"));
                usuarios.setPasswordHash(tablaResultado.getString("passwordhash"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.err.print("Error al buscar su usuario: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean actualizar(Usuarios usuarios) {
        String sql = "{call sp_actualizarusuarios(?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setString(1, usuarios.getUsername());
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
            System.err.println("Error [Eliminar Usuario]: " + e.getMessage());
            return false;
        }
    }
}

