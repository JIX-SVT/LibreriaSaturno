package org.lsa.daoimpl;

import org.lsa.model.DetalleCompra;
import org.lsa.dao.DetalleCompraDAO;
import org.lsa.utils.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleCompraImpl implements DetalleCompraDAO {

    @Override
    public boolean insertar(DetalleCompra objeto) {
        String sql = "{call sp_insertardetallecompra(?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getNoCompra());
            cs.setString(2, objeto.getIsbn());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Insertar Detalle]: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<DetalleCompra> listar() {
        List<DetalleCompra> lista = new ArrayList<>();
        String sql = "{call sp_listardetallecompra()}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new DetalleCompra(
                    rs.getInt("id_detalle_compra"),
                    rs.getInt("no_compra"),
                    rs.getString("isbn")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error [Listar Detalle]: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public DetalleCompra buscar(Integer id) {
        String sql = "{call sp_buscardetallecompra(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new DetalleCompra(
                        rs.getInt("id_detalle_compra"),
                        rs.getInt("no_compra"),
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
    public boolean actualizar(DetalleCompra objeto) {
        String sql = "{call sp_actualizardetallecompra(?, ?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getIdDetalleCompra());
            cs.setInt(2, objeto.getNoCompra());
            cs.setString(3, objeto.getIsbn());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Actualizar Detalle]: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "{call sp_eliminardetallecompra(?)}";
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