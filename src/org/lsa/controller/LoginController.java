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

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIngresar;

    @FXML
    private void handleLogin(ActionEvent event) {
        String usuarioText = txtUsuario.getText() != null ? txtUsuario.getText().trim().toLowerCase() : "";
        String passText = txtContrasena.getText() != null ? txtContrasena.getText() : "";

        if (usuarioText.isEmpty() || passText.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Por favor, ingrese su usuario y contraseña.");
            return;
        }

        String fxmlPath;
        String tituloVentana;

        switch (usuarioText) {
            case "admin":
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
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario no reconocido", "Para pruebas usa como usuario: admin, cajero o empleado.");
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
