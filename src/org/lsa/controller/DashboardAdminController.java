package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.lsa.utils.SesionUsuario;

public class DashboardAdminController implements Initializable {

    private static final Logger log = Logger.getLogger(DashboardAdminController.class.getName());

    @FXML private Label lblVentasTotales;
    @FXML private Label lblTotalLibros;
    @FXML private Label lblUsuariosActivos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Inicializando DashboardAdminController...");
        cargarKpis();
    }

    private void cargarKpis() {
        log.info("Cargando indicadores clave de rendimiento (KPIs)...");
        lblVentasTotales.setText("Q0.00");
        lblTotalLibros.setText("0"); 
        lblUsuariosActivos.setText("1");
    }

    @FXML
    public void handleReportesVentas(ActionEvent event) {
        log.info("Navegando hacia la vista de Reportes de Ventas (DashboardCajeroView.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardCajeroView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Reportes de Ventas");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al cargar la vista de reportes de ventas: /org/lsa/view/DashboardCajeroView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de reportes de ventas.");
        }
    }

    @FXML
    public void handleReportesInventario(ActionEvent event) {
        log.info("Navegando hacia la vista de Reportes de Inventario (DashboardBodegaView.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardBodegaView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Reportes de Inventario");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al cargar la vista de reportes de inventario: /org/lsa/view/DashboardBodegaView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de reportes de inventario.");
        }
    }

    @FXML
    public void handleGestionarLibros(ActionEvent event) {
        log.info("Navegando hacia la vista de Gestión de Libros (DashboardCajeroView.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardCajeroView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Reportes de Inventario");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al cargar la vista de gestión de libros: /org/lsa/view/DashboardCajeroView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de reportes de inventario.");
        }
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
        log.info("Regresando al menú principal (DashboardMenuView.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Menú Principal - Librería Saturno");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al cargar la vista del menú principal: /org/lsa/view/DashboardMenuView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista del menú principal.");
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        log.info("Cerrando sesión de administrador e intentando navegar a LoginView.fxml.");
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
            log.log(Level.SEVERE, "Error al intentar volver al inicio de sesión: /org/lsa/view/LoginView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo regresar a la pantalla de inicio de sesión.");
        }
    }

    @FXML
    public void handleGestionarUsuarios(ActionEvent event) {
        log.info("Navegando hacia la vista de Gestión de Usuarios (ListaUsuariosView.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/ListaUsuariosView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Gestionar Usuarios");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al cargar la vista de gestión de usuarios: /org/lsa/view/ListaUsuariosView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de gestión de usuarios.");
        }
    }

    @FXML
    private void handleCambiarContraseña() {
        log.info("Abriendo ventana emergente para Cambio de Contraseña (CambioContrasenaView.fxml).");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/CambioContrasenaView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al cargar la ventana modal de cambio de contraseña: /org/lsa/view/CambioContrasenaView.fxml", e);
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de cambio de contraseña.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        log.info("Mostrando ventana emergente de alerta. Tipo: " + tipo + " | Título: " + titulo);
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
