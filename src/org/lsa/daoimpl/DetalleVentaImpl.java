package org.lsa.daoimpl;

import org.lsa.dao.DetalleVentaDAO;
import org.lsa.model.DetalleVenta;
import org.lsa.utils.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaImpl implements DetalleVentaDAO {

    public boolean insertar(DetalleVenta objeto) {
        String sql = "{call sp_insertardetalleventa(?, ?, ?, ?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getNoVenta());
            cs.setString(2, objeto.getIsbn());
            cs.setInt(3, objeto.getCantidad());
            cs.setDouble(4, objeto.getPrecioUnitario());
            cs.setDouble(5, objeto.getSubTotalDetalle());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Insertar Detalle Venta]: " + e.getMessage());
            return false;
        }
    }

    public List<DetalleVenta> listar() {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "{call sp_listardetalleventa()}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            
            while (rs.next()) {
                lista.add(new DetalleVenta(
                    rs.getInt("id_detalle_venta"),
                    rs.getInt("id_venta"),
                    rs.getString("isbn"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario"),
                    rs.getDouble("subtotal")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error [Listar Detalle Venta]: " + e.getMessage());
        }
        return lista;
    }

    public DetalleVenta buscar(Integer id) {
        String sql = "{call sp_buscardetalleventa(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    return new DetalleVenta(
                        rs.getInt("id_detalle_venta"),
                        rs.getInt("id_venta"),
                        rs.getString("isbn"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error [Buscar Detalle Venta]: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizar(DetalleVenta objeto) {
        String sql = "{call sp_actualizardetalleventa(?, ?, ?, ?, ?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getIdDetalleventa());
            cs.setInt(2, objeto.getNoVenta());
            cs.setString(3, objeto.getIsbn());
            cs.setInt(4, objeto.getCantidad());
            cs.setDouble(5, objeto.getPrecioUnitario());
            cs.setDouble(6, objeto.getSubTotalDetalle());
            
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Actualizar Detalle Venta]: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(Integer id) {
        String sql = "{call sp_eliminardetalleventa(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error [Eliminar Detalle Venta]: " + e.getMessage());
            return false;
        }
    }
}