package org.lsa.daoimpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lsa.dao.LibroDAO;
import org.lsa.model.Libro;
import org.lsa.utils.Conexion;

public class LibroDAOImpl implements LibroDAO {

    @Override
    public List<Libro> listar() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM libros";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Libro(
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getDate("fecha_publicacion"),
                    rs.getDouble("precio"),
                    rs.getInt("id_categoria"),
                    rs.getString("nit_editorial"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM libros WHERE isbn = ?";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Libro(
                        rs.getString("isbn"),
                        rs.getString("titulo"),
                        rs.getDate("fecha_publicacion"),
                        rs.getDouble("precio"),
                        rs.getInt("id_categoria"),
                        rs.getString("nit_editorial"),
                        rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean agregar(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitulo());
            stmt.setDate(3, (Date) libro.getFechaPublicacion());
            stmt.setDouble(4, libro.getPrecio());
            stmt.setInt(5, libro.getIdCategoria());
            stmt.setString(6, libro.getNitEditorial());
            stmt.setInt(7, libro.getStock());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, fecha_publicacion = ?, precio = ?, id_categoria = ?, nit_editorial = ? WHERE isbn = ?";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setDate(2, (Date) libro.getFechaPublicacion());
            stmt.setDouble(3, libro.getPrecio());
            stmt.setInt(4, libro.getIdCategoria());
            stmt.setString(5, libro.getNitEditorial());
            stmt.setString(6, libro.getIsbn());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarStock(String isbn, int cantidad) {
        String sql = "UPDATE libros SET stock = stock + ? WHERE isbn = ?";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cantidad);
            stmt.setString(2, isbn);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Libro> obtenerLibrosStockCritico() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM libros WHERE stock <= 10";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Libro(
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getDate("fecha_publicacion"),
                    rs.getDouble("precio"),
                    rs.getInt("id_categoria"),
                    rs.getString("nit_editorial"),
                    rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}