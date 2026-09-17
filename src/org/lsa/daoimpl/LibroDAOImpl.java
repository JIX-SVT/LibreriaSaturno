package org.lsa.daoimpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lsa.dao.LibroDAO;
import org.lsa.model.Libro;
import org.lsa.utils.Conexion;

public class LibroDAOImpl implements LibroDAO {

    private static final Logger LOGGER = Logger.getLogger(LibroDAOImpl.class.getName());

    @Override
    public List<Libro> listar() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM libros";
        
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar libros", e);
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
                    return mapearLibro(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al buscar libro por ISBN: " + isbn, e);
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
            stmt.setDate(3, convertirAFechaSql(libro.getFechaPublicacion()));
            stmt.setDouble(4, libro.getPrecio());
            stmt.setInt(5, libro.getIdCategoria());
            stmt.setString(6, libro.getNitEditorial());
            stmt.setInt(7, libro.getStock());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al agregar libro con ISBN: " + libro.getIsbn(), e);
            return false;
        }
    }

    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, fecha_publicacion = ?, precio = ?, id_categoria = ?, nit_editorial = ? WHERE isbn = ?";
        
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitulo());
            stmt.setDate(2, convertirAFechaSql(libro.getFechaPublicacion()));
            stmt.setDouble(3, libro.getPrecio());
            stmt.setInt(4, libro.getIdCategoria());
            stmt.setString(5, libro.getNitEditorial());
            stmt.setString(6, libro.getIsbn());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar libro con ISBN: " + libro.getIsbn(), e);
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
            LOGGER.log(Level.SEVERE, "Error al eliminar libro con ISBN: " + isbn, e);
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
            LOGGER.log(Level.SEVERE, "Error al actualizar stock del libro con ISBN: " + isbn, e);
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
                lista.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al consultar libros con stock crítico", e);
        }
        return lista;
    }

    @Override
    public List<Integer> listarIdsCategorias() {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT id_categoria FROM libros ORDER BY id_categoria ASC";
        
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getInt("id_categoria"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar IDs de categorías", e);
        }
        return lista;
    }

    @Override
    public List<String> listarNitsEditoriales() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT nit_editorial FROM libros ORDER BY nit_editorial ASC";
        
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nit_editorial"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al listar NITs de editoriales", e);
        }
        return lista;
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
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

    private Date convertirAFechaSql(java.util.Date fecha) {
        if (fecha == null) {
            return null;
        }
        if (fecha instanceof Date) {
            return (Date) fecha;
        }
        return new Date(fecha.getTime());
    }
}