package org.lsa.controller;

import java.net.URL;
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
    @FXML private TableColumn<Libro, Integer> colId;
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colStockActual, colStockMinimo;

    @FXML private TextField txtIsbn, txtTitulo, txtAutor, txtPrecio, txtStockActual, txtStockMinimo, txtCantidadMovimiento;
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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStockActual.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
    }

    private void cargarDatos() {
        listaLibros.setAll(libroDAO.listarLibros());
        tblLibros.setItems(listaLibros);
        verificarBajoStock();
    }

    private void verificarBajoStock() {
        long bajoStock = listaLibros.stream().filter(l -> l.getStockActual() < l.getStockMinimo()).count();
        if (bajoStock > 0) {
            lblAlertaBajoStock.setText("️ Hay " + bajoStock + " producto(s) por debajo del stock mínimo.");
        } else {
            lblAlertaBajoStock.setText("Todos los productos tienen stock adecuado.");
        }
    }

    @FXML
    public void handleGuardarLibro(ActionEvent event) {
        try {
            Libro libro = (libroSeleccionado == null) ? new Libro() : libroSeleccionado;
            libro.setIsbn(txtIsbn.getText());
            libro.setTitulo(txtTitulo.getText());
            libro.setAutor(txtAutor.getText());
            libro.setPrecio(Double.parseDouble(txtPrecio.getText()));
            libro.setStockActual(Integer.parseInt(txtStockActual.getText()));
            libro.setStockMinimo(Integer.parseInt(txtStockMinimo.getText()));

            if (libroDAO.guardarOActualizar(libro)) {
                mostrarAlerta("Éxito", "Libro guardado/modificado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo guardar el libro.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Verifique que Precio y Stocks sean numéricos.", Alert.AlertType.WARNING);
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

            if (inventarioDAO.registrarMovimiento(libroSeleccionado.getId(), tipo, cantidad)) {
                mostrarAlerta("Éxito", tipo + " registrado correctamente.", Alert.AlertType.INFORMATION);
                txtCantidadMovimiento.clear();
                cargarDatos();
            } else {
                mostrarAlerta("Error", "No se pudo registrar la " + tipo + " (verifique stock suficiente).", Alert.AlertType.ERROR);
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
        txtAutor.setText(l.getAutor());
        txtPrecio.setText(String.valueOf(l.getPrecio()));
        txtStockActual.setText(String.valueOf(l.getStockActual()));
        txtStockMinimo.setText(String.valueOf(l.getStockMinimo()));
    }

    private void limpiarCampos() {
        libroSeleccionado = null;
        txtIsbn.clear();
        txtTitulo.clear();
        txtAutor.clear();
        txtPrecio.clear();
        txtStockActual.clear();
        txtStockMinimo.clear();
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