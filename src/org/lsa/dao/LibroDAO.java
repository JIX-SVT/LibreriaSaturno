package org.lsa.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.lsa.model.Libro;
import org.lsa.utils.ConexionSingleton;

public class LibroDAO {

    public List<Libro> listarLibros() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Libro(
                    rs.getInt("id"),
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getDouble("precio"),
                    rs.getInt("stock_actual"),
                    rs.getInt("stock_minimo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean guardarOActualizar(Libro libro) {
        String sql;
        if (libro.getId() == 0) {
            sql = "INSERT INTO libros (isbn, titulo, autor, precio, stock_actual, stock_minimo) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE libros SET isbn=?, titulo=?, autor=?, precio=?, stock_actual=?, stock_minimo=? WHERE id=?";
        }
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitulo());
            stmt.setString(3, libro.getAutor());
            stmt.setDouble(4, libro.getPrecio());
            stmt.setInt(5, libro.getStockActual());
            stmt.setInt(6, libro.getStockMinimo());
            if (libro.getId() != 0) {
                stmt.setInt(7, libro.getId());
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}