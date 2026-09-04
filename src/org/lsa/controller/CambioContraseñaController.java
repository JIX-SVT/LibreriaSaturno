package org.lsa.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import org.lsa.dao.UsuarioDAO;
import org.lsa.daoimpl.UsuarioDAOImpl;

public class CambioContraseñaController {

    @FXML
    private PasswordField txtCurrentPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private void handleUpdatePassword() {
        String currentPass = txtCurrentPassword.getText();
        String newPass = txtNewPassword.getText();
        String confirmPass = txtConfirmPassword.getText();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Todos los campos son obligatorios.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showAlert(AlertType.ERROR, "Error", "La nueva contraseña y la confirmación no coinciden.");
            return;
        }

        int idUsuarioActual = 1; 
        UsuarioDAO dao = new UsuarioDAOImpl();

        boolean esValida = dao.validarContrasenaActual(idUsuarioActual, currentPass);
        if (!esValida) {
            showAlert(AlertType.ERROR, "Contraseña Incorrecta", "La contraseña actual indicada no coincide con nuestros registros.");
            return;
        }

        boolean actualizado = dao.actualizarPassword(idUsuarioActual, newPass);
        if (actualizado) {
            showAlert(AlertType.INFORMATION, "Éxito", "La contraseña se ha actualizado correctamente.");
        } else {
            showAlert(AlertType.ERROR, "Error", "No se pudo actualizar la contraseña en la base de datos.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}