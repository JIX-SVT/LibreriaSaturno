package org.lsa.service;

import org.lsa.dao.UsuarioDAO;
import org.lsa.daoimpl.UsuarioDAOImpl;
import org.lsa.model.Usuario;

public class AuthService {

    private UsuarioDAO usuarioDAO;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario login(String correo, String contrasena) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El campo de correo no puede estar vacío.");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("La contraseña no puede estar vacía.");
        }

        Usuario usuario = usuarioDAO.autenticar(correo, contrasena);

        if (usuario == null) {
            throw new Exception("Credenciales incorrectas. Verifica tu correo y contraseña.");
        }

        return usuario;
    }
}