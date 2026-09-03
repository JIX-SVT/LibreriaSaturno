package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void handleReportesVentas(ActionEvent event) {
      Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
    Navegador.cargarVista(stage, "/org/lsa/view/DashboardCajeroView.fxml", "Reportes de Ventas");  
    }

    @FXML
    public void handleReportesInventario(ActionEvent event) {
      Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
    Navegador.cargarVista(stage,"/org/lsa/view/DashboardBodegaView.fxml", "Reportes de Inventario");  
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
        Navegador.cargarVista(stage, "/org/lsa/view/LoginView.fxml", "Inicio de Sesión");
    }
    
@FXML
public void handleGestionarUsuarios(ActionEvent event) {
    Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
    Navegador.cargarVista(stage, "/org/lsa/view/ListaUsuariosView.fxml", "Gestionar Usuarios");
}
    private void mostrarNotificacion(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    @FXML
private void handleCambiarContraseña() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/CambioContrasenaView.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
        mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de cambio de contraseña.");
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

