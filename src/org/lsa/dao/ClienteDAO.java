package org.lsa.dao;

import java.util.List;
import org.lsa.model.Cliente;

public interface ClienteDAO {
    List<Cliente> listar() throws Exception;
    Cliente buscarPorId(Long cui) throws Exception;
    boolean crear(Cliente cliente) throws Exception;
    boolean actualizar(Cliente cliente) throws Exception;
    boolean eliminar(Long cui) throws Exception;
}