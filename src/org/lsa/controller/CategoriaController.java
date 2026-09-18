package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.lsa.dao.CategoriaDAO;
import org.lsa.daoimpl.CategoriaDAOImpl;
import org.lsa.model.Categoria;

public class CategoriaController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(CategoriaController.class.getName());

    @FXML
    private TextField txtIdCategoria;
    @FXML
    private TextField txtNombre;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Categoria> tablaCategorias;
    @FXML
    private TableColumn<Categoria, Integer> colIdCategoria;
    @FXML
    private TableColumn<Categoria, String> colNombreCategoria;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnPrimero;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnUltimo;
    @FXML
    private TextField txtBuscar;

    private boolean modoEdicion = false;
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final ObservableList<Categoria> listaCategorias = FXCollections.observableArrayList();
    private final FilteredList<Categoria> categoriasFiltradas = new FilteredList<>(listaCategorias, p -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarTabla();
        tablaCategorias.setItems(categoriasFiltradas);
        seleccionarFila();
        configurarBusqueda();
    }

    public void configurarTabla() {
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNombreCategoria.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
    }

    private void cargarTabla() {
        try {
            listaCategorias.setAll(categoriaDAO.listar());
        } catch (Exception e) {
            mostrarError("Error al cargar la lista de categorías: " + e.getMessage());
        }
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarCategorias());
    }

    private void filtrarCategorias() {
        String busqueda = txtBuscar.getText() != null ? txtBuscar.getText().trim().toLowerCase() : "";
        if (busqueda.isEmpty()) {
            categoriasFiltradas.setPredicate(p -> true);
        } else {
            categoriasFiltradas.setPredicate(categoria ->
                    String.valueOf(categoria.getIdCategoria()).contains(busqueda)
                    || (categoria.getNombreCategoria() != null && categoria.getNombreCategoria().toLowerCase().contains(busqueda)));
        }
    }

    private void seleccionarFila() {
        tablaCategorias.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        if (txtIdCategoria != null) {
                            txtIdCategoria.setText(String.valueOf(newSelection.getIdCategoria()));
                        }
                        txtNombre.setText(newSelection.getNombreCategoria());
                        desactivarFormulario();
                    }
                });
    }

    @FXML
    private void handleGuardar() {
        try {
            String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";

            if (nombre.isEmpty()) {
                mostrarAdvertencia("El nombre de la categoría es obligatorio.");
                lblMensaje.setText("Ingrese el nombre de la categoría.");
                return;
            }

            Categoria categoria = new Categoria();
            categoria.setNombreCategoria(nombre);

            if (modoEdicion && txtIdCategoria != null && !txtIdCategoria.getText().isEmpty()) {
                categoria.setIdCategoria(Integer.parseInt(txtIdCategoria.getText().trim()));
            }

            boolean guardado;
            if (modoEdicion) {
                guardado = categoriaDAO.actualizar(categoria);
            } else {
                guardado = categoriaDAO.crear(categoria);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Categoría actualizada exitosamente."
                        : "Categoría registrada exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la información de la categoría.");
            }
        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        limpiarFormulario();
        desactivarFormulario();
        activarNavegacion();
        modoEdicion = false;
        lblMensaje.setText("");
    }

    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaCategorias.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtNombre.requestFocus();
    }

    @FXML
    private void handleEditar() {
        Categoria seleccion = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una categoría de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    @FXML
    private void handlePrimero() {
        if (!tablaCategorias.getItems().isEmpty()) {
            tablaCategorias.getSelectionModel().selectFirst();
            tablaCategorias.scrollTo(0);
        }
    }

    @FXML
    private void handleAnterior() {
        if (!tablaCategorias.getItems().isEmpty()) {
            tablaCategorias.getSelectionModel().selectPrevious();
            if (tablaCategorias.getSelectionModel().getSelectedIndex() >= 0) {
                tablaCategorias.scrollTo(tablaCategorias.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleSiguiente() {
        if (!tablaCategorias.getItems().isEmpty()) {
            tablaCategorias.getSelectionModel().selectNext();
            if (tablaCategorias.getSelectionModel().getSelectedIndex() >= 0) {
                tablaCategorias.scrollTo(tablaCategorias.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleUltimo() {
        if (!tablaCategorias.getItems().isEmpty()) {
            tablaCategorias.getSelectionModel().selectLast();
            tablaCategorias.scrollTo(tablaCategorias.getItems().size() - 1);
        }
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
        LOGGER.info("Navegando de regreso al menú principal.");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/MenuBodegaView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Menú");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al intentar cargar la vista del menú principal", e);
            mostrarError("No se pudo cargar la vista del menú: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        if (txtIdCategoria != null) {
            txtIdCategoria.clear();
        }
        txtNombre.clear();
    }

    private void activarFormulario() {
        txtNombre.setDisable(false);
        if (btnGuardar != null) btnGuardar.setDisable(false);
        if (btnCancelar != null) btnCancelar.setDisable(false);
    }

    private void desactivarFormulario() {
        txtNombre.setDisable(true);
        if (btnGuardar != null) btnGuardar.setDisable(true);
        if (btnCancelar != null) btnCancelar.setDisable(true);
    }

    private void activarNavegacion() {
        tablaCategorias.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        txtBuscar.setDisable(false);
    }

    private void desactivarNavegacion() {
        tablaCategorias.setDisable(true);
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnPrimero.setDisable(true);
        btnAnterior.setDisable(true);
        btnSiguiente.setDisable(true);
        btnUltimo.setDisable(true);
        txtBuscar.setDisable(true);
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}