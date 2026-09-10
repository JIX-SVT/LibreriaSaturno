package org.lsa.controller;

import java.io.IOException;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.lsa.dao.UsuarioDAO;
import org.lsa.daoimpl.UsuarioDAOImpl;

public class CambioContrasenaController {

    private static final Logger log = Logger.getLogger(CambioContrasenaController.class.getName());

    @FXML
    private PasswordField txtCurrentPassword;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private void handleUpdatePassword(ActionEvent event) {
        log.info("Iniciando proceso de actualización de contraseña.");

        String currentPass = txtCurrentPassword.getText();
        String newPass = txtNewPassword.getText();
        String confirmPass = txtConfirmPassword.getText();

        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            log.warning("Validación fallida: existen campos de contraseña vacíos.");
            showAlert(AlertType.ERROR, "Error", "Todos los campos son obligatorios.");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            log.warning("Validación fallida: la nueva contraseña y la confirmación no coinciden.");
            showAlert(AlertType.ERROR, "Error", "La nueva contraseña y la confirmación no coinciden.");
            return;
        }

        int idUsuarioActual = 1; 
        UsuarioDAO dao = new UsuarioDAOImpl();

        log.info("Validando contraseña actual para el usuario con ID: " + idUsuarioActual);
        boolean esValida = dao.validarContrasenaActual(idUsuarioActual, currentPass);
        if (!esValida) {
            log.warning("Intento de cambio de contraseña denegado: contraseña actual incorrecta para ID " + idUsuarioActual);
            showAlert(AlertType.ERROR, "Contraseña Incorrecta", "La contraseña actual indicada no coincide con nuestros registros.");
            return;
        }

        log.info("Enviando petición de actualización de contraseña al DAO para usuario ID: " + idUsuarioActual);
        boolean actualizado = dao.actualizarPassword(idUsuarioActual, newPass);
        if (actualizado) {
            log.info("Contraseña actualizada exitosamente en la base de datos para ID: " + idUsuarioActual);
            showAlert(AlertType.INFORMATION, "Éxito", "La contraseña se ha actualizado correctamente.");
            
            try {
                Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/ListaUsuariosView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                escenarioPrincipal.setTitle("Lista de Usuarios");
                escenarioPrincipal.setScene(scene);
                escenarioPrincipal.show();
            } catch (IOException e) {
                log.severe("Error de interfaz al intentar cargar ListaUsuarios.fxml: " + e.getMessage());
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista lista usuario.");
            }
        } else {
            log.severe("Error de persistencia: no se pudo actualizar la contraseña en la base de datos para ID: " + idUsuarioActual);
            showAlert(AlertType.ERROR, "Error", "No se pudo actualizar la contraseña en la base de datos.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        log.info("Mostrando ventana de alerta [Tipo: " + alertType + "]: " + title);
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}