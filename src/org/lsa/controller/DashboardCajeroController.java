package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.lsa.dao.LibroDAO;
import org.lsa.daoimpl.LibroDAOImpl;
import org.lsa.model.Carrito;
import org.lsa.model.DetalleVenta;
import org.lsa.model.Libro;
import org.lsa.model.Venta;
import org.lsa.service.VentaService;
import org.lsa.utils.SesionUsuario;

public class DashboardCajeroController implements Initializable {

    private static final Logger log = Logger.getLogger(DashboardCajeroController.class.getName());

    @FXML private TextField txtBusqueda;
    @FXML private TableView<Libro> tblLibros; 
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colStock;
    @FXML private Label lblVentasHoy;

    @FXML private TableView<Carrito> tblCarrito;
    @FXML private TableColumn<Carrito, String> colCarritoIsbn;
    @FXML private TableColumn<Carrito, String> colCarritoTitulo;
    @FXML private TableColumn<Carrito, Double> colCarritoPrecio;
    @FXML private TableColumn<Carrito, Integer> colCarritoStock;
    @FXML private TableColumn<Carrito, Double> colCarritoSubtotal;
    
    @FXML private Label lblTotalPagar;
    @FXML private TextField txtCuiCliente;

    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final VentaService ventaService = new VentaService();

    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private final FilteredList<Libro> librosFiltrados = new FilteredList<>(listaLibros, p -> true);
    private final ObservableList<Carrito> listaCarrito = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Inicializando DashboardCajeroController...");
        configurarTabla();
        cargarTabla();
        tblLibros.setItems(librosFiltrados);
        configurarBusqueda();
    }

    private void configurarTabla() {
        log.info("Configurando columnas de la tabla de libros y carrito.");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

       
    }

    private void cargarTabla() {
        log.info("Cargando lista completa de libros desde el DAO.");
        listaLibros.clear();
        listaLibros.addAll(libroDAO.listarTodos());
    }

    private void configurarBusqueda() {
        txtBusqueda.textProperty().addListener(
            (obs, oldValue, newValue) -> filtrarLibros()
        );
    }

    private void filtrarLibros() {
        String busqueda = txtBusqueda.getText().trim().toLowerCase();
        log.info("Aplicando filtro de búsqueda de libros: '" + busqueda + "'");

        if (busqueda.isEmpty()) {
            librosFiltrados.setPredicate(p -> true);
        } else {
            librosFiltrados.setPredicate(libro -> {
                boolean coincideTitulo = libro.getTitulo() != null 
                        && libro.getTitulo().toLowerCase().contains(busqueda);

                boolean coincideAutor = libro.getAutor() != null 
                        && libro.getAutor().toLowerCase().contains(busqueda);

                String isbnStr = String.valueOf(libro.getIsbn());
                boolean coincideIsbn = isbnStr.contains(busqueda);

                return coincideTitulo || coincideAutor || coincideIsbn;
            });
        }
    }

    @FXML
    public void handleBuscar(ActionEvent event) {
        log.info("Acción manual de búsqueda ejecutada.");
        filtrarLibros();
    }

    @FXML
    public void cargarDatosTabla() {
        log.info("Actualizando listaLibros desde el DAO.");
        cargarTabla();
    }

   @FXML
    public void handlenuevaventa(ActionEvent event) {
        log.info("Se agrego un venta nueva.");
         SesionUsuario.getInstancia().cerrarSesion();
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/NuevaVentaView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Registro de Venta");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al intentar volver a la vista de Login", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo regresar al login.");
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        log.info("Cerrando sesión de usuario e intentando volver al Login.");
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
            log.log(Level.SEVERE, "Error al intentar volver a la vista de Login", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo regresar al login.");
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