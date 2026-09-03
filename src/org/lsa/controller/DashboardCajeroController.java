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
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.lsa.utils.SesionUsuario;
 
public class DashboardCajeroController implements Initializable {
 
    @FXML private TextField txtBusqueda;
    @FXML private TableView<?> tblResultados; 
    @FXML private TableColumn<?, ?> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<?, ?> colPrecio;
    @FXML private TableColumn<?, ?> colStock;
    @FXML private Label lblVentasHoy;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        actualizarVentasHoy();
    }
 
    private void actualizarVentasHoy() {
        lblVentasHoy.setText("Q0.00");
    }
 
    @FXML
    public void handleBuscar(ActionEvent event) {
        
    }
 
    @FXML
    public void handleNuevaVenta(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Punto de Venta");
        alerta.setHeaderText(null);
        alerta.setContentText("Módulo de caja listo para procesar una nueva venta.");
        alerta.showAndWait();
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Menú Principal");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la vista del menú.", ButtonType.OK);
            alerta.showAndWait();
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        SesionUsuario.getInstancia().cerrarSesion();
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Inicio de Sesión");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo regresar al login.", ButtonType.OK);
            alerta.showAndWait();
        }
    }
}
