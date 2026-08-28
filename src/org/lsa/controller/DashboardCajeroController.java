
package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.Alert; 
import javafx.scene.control.Alert.AlertType;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;


 
public class DashboardCajeroController implements Initializable {
 
    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/Login.fxml", "Inicio de Sesión");
    }

    @FXML
    public void handleIngresar(ActionEvent event) {
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Sección No Disponible");
        alerta.setHeaderText(null);
        alerta.setContentText("Esta sección no está disponible.");
        alerta.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  
    
}
