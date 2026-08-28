
package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;

public class DashboardAdminController implements Initializable {

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
         SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/Login.fxml", "Inicio de Sesión");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización de componentes aquí
    }
}
