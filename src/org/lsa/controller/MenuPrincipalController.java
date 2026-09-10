package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;

public class MenuPrincipalController implements Initializable {

    private static final Logger log = Logger.getLogger(MenuPrincipalController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Inicializando MenuPrincipalController...");
    }

    @FXML
    public void handleAbrirAdmin(ActionEvent event) {
        log.info("Navegando al Panel de Administración (DashboardAdminView.fxml).");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/DashboardAdminView.fxml", "Panel Administración");
    }

    @FXML
    public void handleAbrirBodega(ActionEvent event) {
        log.info("Navegando al Panel de Bodega (DashboardBodegaView.fxml).");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/DashboardBodegaView.fxml", "Panel Bodega");
    }

    @FXML
    public void handleAbrirCajero(ActionEvent event) {
        log.info("Navegando al Panel de Caja (DashboardCajeroView.fxml).");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/DashboardCajeroView.fxml", "Panel Caja");
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        log.info("Cerrando sesión de usuario desde el menú principal y regresando al Login.");
        SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/LoginView.fxml", "Inicio de Sesión");
    }
}