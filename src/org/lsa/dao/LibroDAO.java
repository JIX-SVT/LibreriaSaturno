package org.lsa.dao;

import java.util.List;
import org.lsa.model.Libro;

public interface LibroDAO {

    public List<Libro> listar();

    public Libro buscarPorIsbn(String isbn);

    public boolean agregar(Libro libro);

    public boolean actualizar(Libro libro);

    public boolean eliminar(String isbn);

    public boolean actualizarStock(String isbn, int cantidad);

    public List<Libro> obtenerLibrosStockCritico();
}