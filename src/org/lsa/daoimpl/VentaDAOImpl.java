package org.lsa.daoimpl;

import org.lsa.utils.Conexion;
import org.lsa.model.Venta;
import org.lsa.dao.VentaDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAOImpl implements VentaDAO {

    @Override
    public boolean insertar(Venta objeto) {
        String sql = "{call sp_insertarventa(?, ?, ?, ?, ?, ?)}"; 
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            double subtotalNum = Double.parseDouble(objeto.getSubTotal().replace(",", "."));

            cs.setDouble(1, subtotalNum);
            cs.setDouble(2, objeto.getDescuento());
            cs.setDouble(3, objeto.getTotalVenta());
            cs.setLong(4, objeto.getCuiCliente());
            cs.setInt(5, objeto.getId_usuario());
            cs.registerOutParameter(6, Types.INTEGER);
            
            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas > 0) {
                objeto.setIdVenta(cs.getInt(6));
                return true;
            }
            return false;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Venta> listar() {
        List<Venta> lista = new ArrayList<>();
        String sql = "{call sp_listarventas()}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setIdVenta(rs.getInt("id_venta"));
                venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                venta.setSubTotal(String.valueOf(rs.getDouble("subtotal")));
                venta.setDescuento(rs.getDouble("descuento"));
                venta.setTotalVenta(rs.getDouble("total"));
                venta.setEstado(rs.getString("estado"));
                venta.setCuiCliente(rs.getLong("cui_cliente"));
                venta.setId_usuario(rs.getInt("id_usuario"));
                lista.add(venta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Venta buscar(Integer id) {
        Venta venta = null;
        String sql = "{call sp_buscarventa(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    venta = new Venta();
                    venta.setIdVenta(rs.getInt("id_venta"));
                    venta.setFechaVenta(rs.getTimestamp("fecha_venta"));
                    venta.setSubTotal(String.valueOf(rs.getDouble("subtotal")));
                    venta.setDescuento(rs.getDouble("descuento"));
                    venta.setTotalVenta(rs.getDouble("total"));
                    venta.setEstado(rs.getString("estado"));
                    venta.setCuiCliente(rs.getLong("cui_cliente"));
                    venta.setId_usuario(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return venta;
    }

    @Override
    public boolean actualizar(Venta objeto) {
        // Implementar en caso de requerir modificación de estado o total
        return false;
    }

    @Override
    public boolean eliminar(Integer id) {
        // Implementar en caso de requerir anulación de venta
        return false;
    }
}