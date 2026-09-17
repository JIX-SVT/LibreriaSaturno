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
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM Libros";
        
        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                libro.setPrecio(rs.getDouble("precio"));
                libro.setIdCategoria(rs.getInt("id_categoria"));
                libro.setNitEditorial(rs.getString("nit_editorial"));
                libro.setStock(rs.getInt("stock"));
                lista.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public Libro buscarLibro(String isbn) {
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM Libros WHERE isbn = ?";
        Libro libro = null;

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    libro = new Libro();
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                    libro.setPrecio(rs.getDouble("precio"));
                    libro.setIdCategoria(rs.getInt("id_categoria"));
                    libro.setNitEditorial(rs.getString("nit_editorial"));
                    libro.setStock(rs.getInt("stock"));
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
        String sql = "INSERT INTO Libros (isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setDate(3, libro.getFechaPublicacion() != null ? new java.sql.Date(libro.getFechaPublicacion().getTime()) : null);
            ps.setDouble(4, libro.getPrecio());
            ps.setInt(5, libro.getIdCategoria());
            ps.setString(6, libro.getNitEditorial());
            ps.setInt(7, libro.getStock());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Libro libro) {
        String sql = "UPDATE Libros SET titulo = ?, fecha_publicacion = ?, precio = ?, id_categoria = ?, nit_editorial = ?, stock = ? WHERE isbn = ?";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setDate(2, libro.getFechaPublicacion() != null ? new java.sql.Date(libro.getFechaPublicacion().getTime()) : null);
            ps.setDouble(3, libro.getPrecio());
            ps.setInt(4, libro.getIdCategoria());
            ps.setString(5, libro.getNitEditorial());
            ps.setInt(6, libro.getStock());
            ps.setString(7, libro.getIsbn());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    
    public boolean eliminar(String isbn) {
        String sql = "DELETE FROM Libros WHERE isbn = ?";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Libro> filtrarLibros(String criterio) {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, precio, stock FROM Libros WHERE titulo LIKE ? OR isbn LIKE ?";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String busqueda = "%" + criterio + "%";
            ps.setString(1, busqueda);
            ps.setString(2, busqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setPrecio(rs.getDouble("precio"));
                    libro.setStock(rs.getInt("stock"));
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
    public List<Libro> obtenerLibrosStockCritico() {
        List<Libro> lista = new ArrayList<>();
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM Libros WHERE stock <= 10";

        try (Connection con = ConexionSingleton.getInstance().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setFechaPublicacion(rs.getDate("fecha_publicacion"));
                libro.setPrecio(rs.getDouble("precio"));
                libro.setIdCategoria(rs.getInt("id_categoria"));
                libro.setNitEditorial(rs.getString("nit_editorial"));
                libro.setStock(rs.getInt("stock"));
                lista.add(libro);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener libros en stock crítico: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

 

    @Override
    public Libro filtrarLibros() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
}