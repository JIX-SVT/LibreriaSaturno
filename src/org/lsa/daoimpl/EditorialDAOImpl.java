package org.lsa.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lsa.dao.EditorialDAO;
import org.lsa.model.Editorial;
import org.lsa.utils.Conexion;

public class EditorialDAOImpl implements EditorialDAO {

    @Override
    public List<Editorial> listar() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "{call sp_listareditoriales()}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consulta = conexion.prepareCall(sql);
             ResultSet rs = consulta.executeQuery()) {

            while (rs.next()) {
                Editorial e = new Editorial();
                e.setNit(rs.getString("nit_editorial"));
                e.setNombreEditorial(rs.getString("nombre_editorial"));
                e.setTelefonoEditorial(rs.getString("telefono_editorial"));
                e.setDireccionEditorial(rs.getString("direccion_editorial"));
                lista.add(e);
            }
        } catch (SQLException e) {
            System.err.println("Error sp_listareditoriales: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Editorial buscar(String nit) {
        Editorial e = null;
        String sql = "{call sp_buscareditorial(?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consulta = conexion.prepareCall(sql)) {

            consulta.setString(1, nit);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    e = new Editorial();
                    e.setNit(rs.getString("nit_editorial"));
                    e.setNombreEditorial(rs.getString("nombre_editorial"));
                    e.setTelefonoEditorial(rs.getString("telefono_editorial"));
                    e.setDireccionEditorial(rs.getString("direccion_editorial"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error sp_buscareditorial: " + ex.getMessage());
        }
        return e;
    }

    @Override
    public boolean crear(Editorial editorial) {
        String sql = "{call sp_insertareditorial(?,?,?,?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consulta = conexion.prepareCall(sql)) {

            consulta.setString(1, editorial.getNit());
            consulta.setString(2, editorial.getNombreEditorial());
            consulta.setString(3, editorial.getTelefonoEditorial());
            consulta.setString(4, editorial.getDireccionEditorial());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error sp_insertareditorial: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizar(Editorial editorial) {
        String sql = "{call sp_actualizareditorial(?,?,?,?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consulta = conexion.prepareCall(sql)) {

            consulta.setString(1, editorial.getNit());
            consulta.setString(2, editorial.getNombreEditorial());
            consulta.setString(3, editorial.getTelefonoEditorial());
            consulta.setString(4, editorial.getDireccionEditorial());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error sp_actualizareditorial: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean eliminar(String nit) {
        String sql = "{call sp_eliminareditorial(?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consulta = conexion.prepareCall(sql)) {

            consulta.setString(1, nit);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error sp_eliminareditorial: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean insertar(Editorial objeto) {
        return crear(objeto);
    }
}