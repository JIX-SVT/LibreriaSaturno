package org.lsa.daoimpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.lsa.dao.MovimientoInventarioDAO;
import org.lsa.model.MovimientoInventario;
import org.lsa.utils.ConexionSingleton;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {

    @Override
    public List<MovimientoInventario> listarTodos() {
        List<MovimientoInventario> lista = new ArrayList<>();
        String consulta = "{call sp_listarmovimientos()}";

        try (Connection conexion = ConexionSingleton.getInstancia().getConexion();
             CallableStatement consultaCall = conexion.prepareCall(consulta);
             ResultSet rs = consultaCall.executeQuery()) {

            while (rs.next()) {
                MovimientoInventario movimiento = new MovimientoInventario(
                    rs.getInt("id_movimiento"),
                    rs.getString("isbn"),
                    rs.getString("tipo_movimiento"),
                    rs.getInt("cantidad"),
                    rs.getTimestamp("fecha_movimiento"),
                    String.valueOf(rs.getInt("id_usuario")),
                    rs.getString("observacion")
                );
                lista.add(movimiento);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar movimientos de inventario: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public MovimientoInventario buscarLibro(int idMovimiento) {
        MovimientoInventario movimiento = null;
        String consultaSQL = "{call sp_buscarmovimientosporisbn(?)}";

        try (Connection conexion = ConexionSingleton.getInstancia().getConexion();
             CallableStatement consultaCall = conexion.prepareCall(consultaSQL)) {
            consultaCall.setString(1, String.valueOf(idMovimiento));

            try (ResultSet rs = consultaCall.executeQuery()) {
                if (rs.next()) {
                    movimiento = new MovimientoInventario(
                        rs.getInt("id_movimiento"),
                        rs.getString("isbn"),
                        rs.getString("tipo_movimiento"),
                        rs.getInt("cantidad"),
                        rs.getTimestamp("fecha_movimiento"),
                        String.valueOf(rs.getInt("id_usuario")),
                        rs.getString("observacion")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar movimiento: " + e.getMessage());
        }
        return movimiento;
    }

    @Override
    public boolean insertar(MovimientoInventario movimiento) {
        String sql = "{call sp_insertarmovimiento(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, movimiento.getIsbn());
            cs.setString(2, movimiento.getTipoMovimiento());
            cs.setInt(3, movimiento.getCantidad());
            cs.setInt(4, Integer.parseInt(movimiento.getIdUsuario()));
            cs.setString(5, movimiento.getObservaciòn());

            return cs.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar movimiento: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Error: El idUsuario debe ser un número entero válido: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean actualizar(MovimientoInventario movimiento) {
        String consulta = "{call sp_actualizarmovimiento(?, ?, ?, ?, ?, ?)}";

        try (Connection conexion = ConexionSingleton.getInstancia().getConexion();
             CallableStatement consultaCall = conexion.prepareCall(consulta)) {
            consultaCall.setInt(1, movimiento.getIdMovimiento());
            consultaCall.setString(2, movimiento.getIsbn());
            consultaCall.setString(3, movimiento.getTipoMovimiento());
            consultaCall.setInt(4, movimiento.getCantidad());
            consultaCall.setInt(5, Integer.parseInt(movimiento.getIdUsuario()));
            consultaCall.setString(6, movimiento.getObservaciòn());
            return consultaCall.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar movimiento: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Error: El idUsuario debe ser un número entero válido: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(int idMovimiento) {
        String consulta = "{call sp_eliminarmovimiento(?)}";

        try (Connection conexion = ConexionSingleton.getInstancia().getConexion();
             CallableStatement consultaCall = conexion.prepareCall(consulta)) {

            consultaCall.setInt(1, idMovimiento);
            return consultaCall.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar movimiento: " + e.getMessage());
            return false;
        }
    }
}