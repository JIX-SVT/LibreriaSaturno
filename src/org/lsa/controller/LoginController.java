package org.lsa.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.lsa.dao.UsuarioDAO;
import org.lsa.daoimpl.UsuarioDAOImpl;
import org.lsa.model.Usuario;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIngresar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuarioText = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        String passText = txtContrasena.getText() != null ? txtContrasena.getText() : "";

        if (usuarioText.isEmpty() || passText.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, ingrese su usuario y contraseña.");
            return;
        }

        // Validación de credenciales usando autenticar
        Usuario usuario = usuarioDAO.autenticar(usuarioText, passText);

        if (usuario != null) {
            if (!usuario.isActivo()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Acceso denegado", "El usuario se encuentra inactivo.");
                return;
            }
            
            mostrarAlerta(Alert.AlertType.INFORMATION, "Bienvenido", "¡Inicio de sesión exitoso!");
            abrirMenuPrincipal();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de autenticación", "Usuario o contraseña incorrectos.");
        }
    }

    private void abrirMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/ListaUsuariosView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Librería Saturno - Panel Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista principal.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}