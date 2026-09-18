package org.lsa.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.lsa.dao.ClienteDAO;
import org.lsa.model.Cliente;
import org.lsa.utils.Conexion;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "{call sp_listarclientes()}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql);
             ResultSet rs = cs.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getLong("cui"),
                    rs.getString("nombre_cliente"),
                    rs.getString("apellido_cliente"),
                    rs.getString("correo_electronico")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }

    @Override
    public Cliente buscarLibro(long cui) {
        Cliente cliente = null;
        String sql = "{call sp_buscarcliente(?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setLong(1, cui);

            try (ResultSet rs = cs.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                        rs.getLong("cui"),
                        rs.getString("nombre_cliente"),
                        rs.getString("apellido_cliente"),
                        rs.getString("correo_electronico")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por CUI: " + e.getMessage());
        }
        return cliente;
    }

    @Override
    public boolean insertar(Cliente cliente) {
        String sql = "{call sp_insertarcliente(?, ?, ?, ?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setLong(1, cliente.getCui());
            cs.setString(2, cliente.getNombreCliente());
            cs.setString(3, cliente.getApellidoCliente());
            cs.setString(4, cliente.getCorreoElectronico());

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        String sql = "{call sp_actualizarcliente(?, ?, ?, ?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setLong(1, cliente.getCui());
            cs.setString(2, cliente.getNombreCliente());
            cs.setString(3, cliente.getApellidoCliente());
            cs.setString(4, cliente.getCorreoElectronico());

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(long cui) {
        String sql = "{call sp_eliminarcliente(?)}";

        try (Connection con = Conexion.getInstancia().conectar();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setLong(1, cui);

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        }
    }
}