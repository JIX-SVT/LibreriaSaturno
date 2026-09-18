package org.lsa.dao;

import java.util.List;
import org.lsa.model.Categoria;
import org.lsa.model.Editorial;
import org.lsa.model.Libro;

public interface LibroDAO {

    public List<Libro> listar();

    public Libro buscarPorIsbn(String isbn);

    public boolean agregar(Libro libro);

    public boolean actualizar(Libro libro);

    public boolean eliminar(String isbn);

    public boolean cambiarEstado(String isbn, boolean estado); // T3.4.14

    public boolean actualizarStock(String isbn, int cantidad);

    public List<Libro> obtenerLibrosStockCritico(); // T3.3.6

    public List<Integer> listarIdsCategorias();

    public List<String> listarNitsEditoriales();
    
    List<Categoria> listarCategorias();
List<Editorial> listarEditoriales();

}