package org.lsa.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lsa.dao.LibroDAO;
import org.lsa.model.Libro;
import org.lsa.utils.ConexionSingleton;

/**
 * @author informatica
 */
public class LibroDAOImpl implements LibroDAO {

    @Override
    public List<Libro> listarTodos() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, precio FROM Libros";
        String consulta = "{call sp_listarlibros()}";
        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setPrecio(rs.getDouble("precio"));
                lista.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Libro buscarLibro(int isbn) {
        String sql = "SELECT isbn, titulo, precio  FROM Libros WHERE isbn = ?";
        Libro libro = null;

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = new Libro();
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setPrecio(rs.getDouble("precio"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por ISBN: " + e.getMessage());
            e.printStackTrace();
        }

        return libro;
    }

    @Override
    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO Libros (isbn, titulo, precio) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setDouble(3, libro.getPrecio());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE Libros SET titulo = ?, precio = ? WHERE isbn = ?";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setDouble(2, libro.getPrecio());
            ps.setString(3, libro.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int isbn) {
        String sql = "DELETE FROM Libros WHERE isbn = ?";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, isbn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Libro> filtrarLibros(String criterio) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, precio FROM Libros WHERE titulo LIKE ? OR autor LIKE ? OR CAST(isbn AS CHAR) LIKE ?";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String busqueda = "%" + criterio + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);
            ps.setString(3, busqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setPrecio(rs.getDouble("precio"));
                    lista.add(libro);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar libros en BD: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Libro filtrarLibros() {
        return null;
    }
}
