package org.lsa.daoimpl;

import org.lsa.model.DetalleVenta;
import org.lsa.dao.DetalleVentaDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.lsa.utils.Conexion;
public class DetalleVentaImpl implements DetalleVentaDAO {

    @Override
    public boolean insertar(DetalleVenta objeto) {
        String sql = "{call sp_insertardetalleventa(?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getNoVenta());
            cs.setString(2, objeto.getIsbn());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Insertar Detalle]: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<DetalleVenta> listar() {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "{call sp_listardetalleventa()}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new DetalleVenta(
                    rs.getInt("id_detalle_venta"),
                    rs.getInt("no_venta"),
                    rs.getString("isbn")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error [Listar Detalle]: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public DetalleVenta buscar(Integer id) {
        String sql = "{call sp_buscardetalleventa(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new DetalleVenta(
                        rs.getInt("id_detalle_venta"),
                        rs.getInt("no_venta"),
                        rs.getString("isbn")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error [Buscar Detalle]: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualizar(DetalleVenta objeto) {
        String sql = "{call sp_actualizardetalleventa(?, ?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getIdDetalleVenta());
            cs.setInt(2, objeto.getNoVenta());
            cs.setString(3, objeto.getIsbn());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Actualizar Detalle]: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "{call sp_eliminardetalleventa(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Eliminar Detalle]: " + e.getMessage());
            return false;
        }
    }
}