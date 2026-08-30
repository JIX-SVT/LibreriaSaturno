package org.lsa.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.lsa.model.Libro;
import org.lsa.utils.ConexionSingleton;

public class LibroDAO {

    public List<Libro> listarLibros() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial FROM libros";
        
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                libro.setPrecio(rs.getDouble("precio"));
                libro.setIdCategoria(rs.getInt("id_categoria"));
                libro.setNitEditorial(rs.getString("nit_editorial"));
                
                lista.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean existeIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean guardarOActualizar(Libro libro) {
        String sql;
        boolean esUpdate = existeIsbn(libro.getIsbn());

        if (esUpdate) {
            sql = "UPDATE libros SET titulo=?, fecha_publicacion=?, precio=?, id_categoria=?, nit_editorial=? WHERE isbn=?";
        } else {
            sql = "INSERT INTO libros (titulo, fecha_publicacion, precio, id_categoria, nit_editorial, isbn) VALUES (?, ?, ?, ?, ?, ?)";
        }

        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, libro.getTitulo());
            
            if (libro.getFechaPublicacion() != null) {
                stmt.setDate(2, new java.sql.Date(libro.getFechaPublicacion().getTime()));
            } else {
                stmt.setNull(2, Types.DATE);
            }
            
            stmt.setDouble(3, libro.getPrecio());
            
            if (libro.getIdCategoria() != 0) {
                stmt.setInt(4, libro.getIdCategoria());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, libro.getNitEditorial());
            stmt.setString(6, libro.getIsbn());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}