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
        String sql = "{call sp_insertarventa(?, ?)}"; 
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {
            cs.setDouble(1, objeto.getTotalVenta());
            cs.setLong(2, objeto.getCuiCliente());
            return cs.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    @Override
    public List<Venta> listar() {
        List<Venta> lista = new ArrayList<>();
        String sql = "{call sp_listarventas()}";
        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                lista.add(new Venta(rs.getInt("no_venta"), rs.getTimestamp("fecha_venta"), rs.getDouble("total_venta"), rs.getLong("cui_cliente")));
            }
        } catch (SQLException e) { }
        return lista;
    }

    @Override
    public Venta buscar(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean actualizar(Venta objeto) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean eliminar(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}