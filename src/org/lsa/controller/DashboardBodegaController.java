package org.lsa.controller;
 
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
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
 
public class DashboardBodegaController implements Initializable {
 
    @FXML private TableView<?> tblLibros; 
    @FXML private TableColumn<?, ?> colIsbn, colTitulo, colNitEditorial;
    @FXML private TableColumn<?, ?> colFechaPublicacion;
    @FXML private TableColumn<?, ?> colPrecio;
    @FXML private TableColumn<?, ?> colIdCategoria;
 
    @FXML private TextField txtIsbn, txtTitulo, txtPrecio, txtIdCategoria, txtNitEditorial, txtCantidadMovimiento;
    @FXML private DatePicker dpFechaPublicacion;
    @FXML private Label lblAlertaBajoStock;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
 
    @FXML
    public void handleGuardarLibro(ActionEvent event) {
        mostrarAlerta("Éxito", "Simulación: Libro guardado correctamente.", Alert.AlertType.INFORMATION);
        limpiarCampos();
    }
 
    @FXML
    public void handleRegistrarIngreso(ActionEvent event) {
        mostrarAlerta("Éxito", "Simulación: Ingreso registrado correctamente.", Alert.AlertType.INFORMATION);
    }
 
    @FXML
    public void handleRegistrarSalida(ActionEvent event) {
        mostrarAlerta("Éxito", "Simulación: Salida registrada correctamente.", Alert.AlertType.INFORMATION);
    }
 
    @FXML
    public void handleLimpiarCampos(ActionEvent event) {
        limpiarCampos();
    }
 
    @FXML
    public void handleVolverMenu(ActionEvent event) {
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Menú");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista del menú.", Alert.AlertType.ERROR);
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
            escenarioPrincipal.setTitle("Librería Saturno - Inicio de Sesión");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }
 
    private void limpiarCampos() {
        txtIsbn.clear();
        txtTitulo.clear();
        dpFechaPublicacion.setValue(null);
        txtPrecio.clear();
        txtIdCategoria.clear();
        txtNitEditorial.clear();
        txtCantidadMovimiento.clear();
    }
 
    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
