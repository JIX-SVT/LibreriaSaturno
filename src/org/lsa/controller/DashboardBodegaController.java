package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger log = Logger.getLogger(DashboardBodegaController.class.getName());

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
        log.info("Inicializando DashboardBodegaController...");
    }

    @FXML
    public void handleGuardarLibro(ActionEvent event) {
        log.info("Ejecutando proceso de guardado de libro (Simulación).");
        mostrarAlerta("Éxito", "Simulación: Libro guardado correctamente.", Alert.AlertType.INFORMATION);
        limpiarCampos();
    }

    @FXML
    public void handleRegistrarIngreso(ActionEvent event) {
        log.info("Registrando ingreso de inventario (Simulación).");
        mostrarAlerta("Éxito", "Simulación: Ingreso registrado correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void handleRegistrarSalida(ActionEvent event) {
        log.info("Registrando salida de inventario (Simulación).");
        mostrarAlerta("Éxito", "Simulación: Salida registrada correctamente.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void handleLimpiarCampos(ActionEvent event) {
        log.info("Solicitud de limpieza manual de campos del formulario de bodega.");
        limpiarCampos();
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
        log.info("Navegando de regreso al menú principal (DashboardMenuView.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Menú");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al intentar cargar la vista del menú principal: /org/lsa/view/DashboardMenuView.fxml", e);
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la vista del menú.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        log.info("Cerrando sesión del usuario de bodega y redirigiendo al Login.");
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
            log.log(Level.SEVERE, "Error al intentar redirigir a la pantalla de Inicio de Sesión: /org/lsa/view/LoginView.fxml", e);
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo regresar a la pantalla de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        log.info("Limpiando los campos de entrada de datos de la vista de bodega.");
        txtIsbn.clear();
        txtTitulo.clear();
        dpFechaPublicacion.setValue(null);
        txtPrecio.clear();
        txtIdCategoria.clear();
        txtNitEditorial.clear();
        txtCantidadMovimiento.clear();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        log.info("Desplegando alerta gráfica [Tipo: " + tipo + "] - Título: " + titulo);
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
