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
import org.lsa.utils.ControlAcceso; // O la clase donde guardes la sesión del usuario

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIngresar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @FXML
    private void handleLogin(ActionEvent event) {
        String correoText = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
        String passText = txtContrasena.getText() != null ? txtContrasena.getText() : "";

        if (correoText.isEmpty() || passText.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, ingrese su correo/usuario y contraseña.");
            return;
        }

        Usuario usuarioLogueado = usuarioDAO.autenticar(correoText, passText);

        if (usuarioLogueado == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Acceso Denegado", "Correo o contraseña incorrectos, o usuario inactivo.");
            return;
        }

        ControlAcceso.setUsuarioLogueado(usuarioLogueado);

        String fxmlPath;
        String tituloVentana;
        String rol = usuarioLogueado.getRol() != null ? usuarioLogueado.getRol().toLowerCase() : "";

        switch (rol) {
            case "admin":
            case "administrador":
                fxmlPath = "/org/lsa/view/DashboardAdminView.fxml";
                tituloVentana = "Librería Saturno - Panel de Administración";
                break;
            case "cajero":
                fxmlPath = "/org/lsa/view/DashboardCajeroView.fxml";
                tituloVentana = "Librería Saturno - Módulo de Ventas";
                break;
            case "empleado":
            case "bodega":
                fxmlPath = "/org/lsa/view/DashboardBodegaView.fxml";
                tituloVentana = "Librería Saturno - Módulo de Inventario";
                break;
            default:
                mostrarAlerta(Alert.AlertType.ERROR, "Rol no autorizado", "El rol asignado (" + rol + ") no tiene una interfaz configurada.");
                return;
        }

        try {
            Stage escenarioPrincipal = (Stage) btnIngresar.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            escenarioPrincipal.setTitle(tituloVentana);
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.centerOnScreen();
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de carga", "No se pudo abrir la vista:\n" + fxmlPath + "\n\nCausa: El archivo FXML interno tiene un error de componentes.");
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