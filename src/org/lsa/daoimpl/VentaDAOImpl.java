package org.lsa.daoimpl;

import org.lsa.utils.Conexion;
import org.lsa.model.Compra;
import org.lsa.dao.CompraDAO;
import org.lsa.model.DetalleCompra.VentaDTO; // Importante para mapear al formato de la tabla
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAOImpl implements CompraDAO {

    @Override
    public boolean insertar(Compra objeto) {
        String sql = "{call sp_insertarcompra(?, ?, ?)}"; 
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setDouble(1, objeto.getTotalCompra());
            cs.setLong(2, objeto.getCuiCliente());
            cs.registerOutParameter(3, Types.INTEGER);
            
            int filasAfectadas = cs.executeUpdate();
            if (filasAfectadas > 0) {
                objeto.setNoCompra(cs.getInt(3));
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();
        String sql = "{call sp_listarcompras()}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setNoCompra(rs.getInt("no_compra"));
                compra.setFechaCompra(rs.getTimestamp("fecha_compra"));
                compra.setTotalCompra(rs.getDouble("total_compra"));
                compra.setCuiCliente(rs.getLong("cui_cliente"));
                lista.add(compra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Compra buscar(Integer id) {
        Compra compra = null;
        String sql = "{call sp_buscarcompra(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    compra = new Compra();
                    compra.setNoCompra(rs.getInt("no_compra"));
                    compra.setFechaCompra(rs.getTimestamp("fecha_compra"));
                    compra.setTotalCompra(rs.getDouble("total_compra"));
                    compra.setCuiCliente(rs.getLong("cui_cliente"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compra;
    }

    @Override
    public boolean actualizar(Compra objeto) {
        String sql = "{call sp_actualizarcompra(?, ?, ?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, objeto.getNoCompra());
            cs.setDouble(2, objeto.getTotalCompra());
            cs.setLong(3, objeto.getCuiCliente());
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(Integer id) {
        String sql = "{call sp_eliminarcompra(?)}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            
            cs.setInt(1, id);
            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // =========================================================================
    // METODO NUEVO ADICIONAL: Convierte tus entidades 'Compra' a objetos 'VentaDTO'
    // =========================================================================
    public List<VentaDTO> listarHistorialCompras() {
        List<VentaDTO> historialDTO = new ArrayList<>();
        // Reutilizamos de forma interna el método listar() que ya tenías programado
        List<Compra> listaComprasBase = this.listar(); 
        
        for (Compra c : listaComprasBase) {
            String idFactura = "FAC-" + c.getNoCompra();
            String cliente = "Cliente CUI: " + c.getCuiCliente();
            String cajero = "Cajero General"; 
            double total = c.getTotalCompra();

            VentaDTO dto = new VentaDTO(idFactura, cliente, cajero, total);
            historialDTO.add(dto);
        }
        return historialDTO;
    }
}
