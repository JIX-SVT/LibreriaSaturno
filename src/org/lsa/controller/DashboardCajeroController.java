package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import org.lsa.model.Libro;
import org.lsa.utils.SesionUsuario;

public class DashboardCajeroController implements Initializable {

    @FXML private TextField txtBusqueda;
    @FXML private TableView<Libro> tblResultados; 
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colStock;
    @FXML private Label lblVentasHoy;

    private final LibroDAO libroDAO = new LibroDAOImpl();

    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private final FilteredList<Libro> librosFiltrados = new FilteredList<>(listaLibros, p -> true);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarTabla();
        tblResultados.setItems(librosFiltrados);
        configurarBusqueda();
        actualizarVentasHoy();
    }

    private void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void cargarTabla() {
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

    private void actualizarVentasHoy() {
        lblVentasHoy.setText("Q0.00");
    }

    @FXML
    public void handleBuscar(ActionEvent event) {
        filtrarLibros();
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