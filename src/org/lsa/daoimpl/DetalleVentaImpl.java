
package org.lsa.daoimpl;


public class DetalleVentaImpl {
    
}
package org.rocka.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.rocka.dao.VentaDAO;
import org.rocka.model.Venta;
import org.rocka.util.Conexion;

public class VentaDAOImpl implements VentaDAO {

    @Override
    public int registrarVentaMaestra(Venta venta) {
        String consulta = "{call sp_insertarventa(?, ?, ?)}";
        int idGenerado = -1;

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta)) {

            consultaCall.setDouble(1, venta.getTotalCompra());
            consultaCall.setLong(2, venta.getCuiCliente());
            consultaCall.registerOutParameter(3, Types.INTEGER); // ID Retornado (no_compra)

            int filasAfectadas = consultaCall.executeUpdate();

            if (filasAfectadas > 0) {
                idGenerado = consultaCall.getInt(3);
            }

        } catch (SQLException e) {
            System.err.print("Error al registrar Venta Maestra: " + e.getMessage());
        }

        return idGenerado;
    }

    @Override
    public Venta buscarPorId(int noCompra) {
        Venta venta = new Venta();
        String consulta = "{call sp_buscarventa(?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta)) {

            consultaCall.setInt(1, noCompra);
            ResultSet tablaResultado = consultaCall.executeQuery();

            if (tablaResultado.next()) {
                venta.setNoCompra(tablaResultado.getInt("no_compra"));
                venta.setFechaCompra(tablaResultado.getTimestamp("fecha_compra"));
                venta.setTotalCompra(tablaResultado.getDouble("total_compra"));
                venta.setCuiCliente(tablaResultado.getLong("cui_cliente"));
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.err.print("Error al buscar Venta: " + e.getMessage());
        }

        return venta;
    }

    @Override
    public List<Venta> listarTodas() {
        List<Venta> ventas = new ArrayList<>();
        String consulta = "{call sp_listarventas()}";

        try (Connection conexion = Conexion.getInstancia().conectar();
             CallableStatement consultaCall = conexion.prepareCall(consulta);
             ResultSet tablaResultado = consultaCall.executeQuery()) {

            while (tablaResultado.next()) {
                Venta venta = new Venta();
                venta.setNoCompra(tablaResultado.getInt("no_compra"));
                venta.setFechaCompra(tablaResultado.getTimestamp("fecha_compra"));
                venta.setTotalCompra(tablaResultado.getDouble("total_compra"));
                venta.setCuiCliente(tablaResultado.getLong("cui_cliente"));
                ventas.add(venta);
            }

        } catch (SQLException e) {
            System.err.print("Error al listar Ventas: " + e.getMessage());
        }

        return ventas;
    }
}