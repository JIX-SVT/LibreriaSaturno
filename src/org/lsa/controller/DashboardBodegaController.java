package org.lsa.controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.lsa.dao.InventarioDAO;
import org.lsa.dao.LibroDAO;
import org.lsa.model.Libro;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;

public class DashboardBodegaController implements Initializable {

    @FXML private TableView<Libro> tblLibros;
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colNitEditorial;
    @FXML private TableColumn<Libro, Date> colFechaPublicacion;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colIdCategoria;

    @FXML private TextField txtIsbn, txtTitulo, txtPrecio, txtIdCategoria, txtNitEditorial, txtCantidadMovimiento;
    @FXML private DatePicker dpFechaPublicacion;
    @FXML private Label lblAlertaBajoStock;

    private final LibroDAO libroDAO = new LibroDAO();
    private final InventarioDAO inventarioDAO = new InventarioDAO();
    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private Libro libroSeleccionado = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos();
        
        tblLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                libroSeleccionado = newSelection;
                completarCampos(newSelection);
            }
        });
    }

    private void configurarColumnas() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaPublicacion.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNitEditorial.setCellValueFactory(new PropertyValueFactory<>("nitEditorial"));
    }

    private void cargarDatos() {
        listaLibros.setAll(libroDAO.listarLibros());
        tblLibros.setItems(listaLibros);
    }

    @FXML
    public void handleGuardarLibro(ActionEvent event) {
        try {
            Libro libro = (libroSeleccionado == null) ? new Libro() : libroSeleccionado;
            libro.setIsbn(txtIsbn.getText());
            libro.setTitulo(txtTitulo.getText());
            
            if (dpFechaPublicacion.getValue() != null) {
                libro.setFechaPublicacion(Date.valueOf(dpFechaPublicacion.getValue()));
            } else {
                libro.setFechaPublicacion(null);
            }

            libro.setPrecio(Double.parseDouble(txtPrecio.getText()));
            
            if (!txtIdCategoria.getText().isEmpty()) {
                libro.setIdCategoria(Integer.parseInt(txtIdCategoria.getText()));
            } else {
                libro.setIdCategoria(0);
            }

            libro.setNitEditorial(txtNitEditorial.getText());

            if (libroDAO.guardarOActualizar(libro)) {
                mostrarAlerta("Éxito", "Libro guardado/modificado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el libro.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Verifique que Precio e Id Categoría sean valores numéricos válidos.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleRegistrarIngreso(ActionEvent event) {
        procesarMovimiento("INGRESO");
    }

    @FXML
    public void handleRegistrarSalida(ActionEvent event) {
        procesarMovimiento("SALIDA");
    }

    private void procesarMovimiento(String tipo) {
        if (libroSeleccionado == null) {
            mostrarAlerta("Atención", "Seleccione un libro de la tabla primero.", Alert.AlertType.WARNING);
            return;
        }
        try {
            int cantidad = Integer.parseInt(txtCantidadMovimiento.getText());
            if (cantidad <= 0) throw new NumberFormatException();

            if (inventarioDAO.registrarMovimiento(libroSeleccionado.getIsbn(), tipo, cantidad)) {
                mostrarAlerta("Éxito", tipo + " registrado correctamente.", Alert.AlertType.INFORMATION);
                txtCantidadMovimiento.clear();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo registrar la " + tipo + " (verifique los datos ingresados).", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ingrese una cantidad entera válida mayor a 0.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleLimpiarCampos(ActionEvent event) {
        limpiarCampos();
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/Login.fxml", "Inicio de Sesión");
    }

    private void completarCampos(Libro l) {
        txtIsbn.setText(l.getIsbn());
        txtTitulo.setText(l.getTitulo());
        
        if (l.getFechaPublicacion() != null) {
            dpFechaPublicacion.setValue(new java.sql.Date(l.getFechaPublicacion().getTime()).toLocalDate());
        } else {
            dpFechaPublicacion.setValue(null);
        }
        
        txtPrecio.setText(String.valueOf(l.getPrecio()));
        txtIdCategoria.setText(String.valueOf(l.getIdCategoria()));
        txtNitEditorial.setText(l.getNitEditorial());
    }

    private void limpiarCampos() {
        libroSeleccionado = null;
        txtIsbn.clear();
        txtTitulo.clear();
        dpFechaPublicacion.setValue(null);
        txtPrecio.clear();
        txtIdCategoria.clear();
        txtNitEditorial.clear();
        txtCantidadMovimiento.clear();
        tblLibros.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}