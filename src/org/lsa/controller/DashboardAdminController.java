package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.lsa.dao.LibroDAO;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;

public class DashboardAdminController implements Initializable {

    @FXML private Label lblVentasTotales;
    @FXML private Label lblTotalLibros;
    @FXML private Label lblUsuariosActivos;

    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarKpis();
    }

    private void cargarKpis() {
        lblVentasTotales.setText("Q0.00");
        int cantidadLibros = libroDAO.listarLibros().size();
        lblTotalLibros.setText(String.valueOf(cantidadLibros));
        lblUsuariosActivos.setText("1");
    }

    @FXML
    public void handleGestionarUsuarios(ActionEvent event) {
        mostrarNotificacion("Gestión de Usuarios", "Abriendo módulo de administración de usuarios.");
    }

    @FXML
    public void handleReportesVentas(ActionEvent event) {
        mostrarNotificacion("Reportes de Ventas", "Abriendo módulo de reportes periódicos.");
    }

    @FXML
    public void handleReportesInventario(ActionEvent event) {
        mostrarNotificacion("Reportes de Inventario", "Abriendo reporte de libros más vendidos y stock valorizado.");
    }
    
    @FXML
    public void handleVolverMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/DashboardMenuView.fxml", "Menú Principal - Librería Saturno");
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/Login.fxml", "Inicio de Sesión");
    }

    private void mostrarNotificacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
}

