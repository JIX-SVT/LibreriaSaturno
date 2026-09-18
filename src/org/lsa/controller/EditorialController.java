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

import org.lsa.dao.EditorialDAO;
import org.lsa.daoimpl.EditorialDAOImpl;
import org.lsa.model.Editorial;

public class EditorialController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(EditorialController.class.getName());

    @FXML private TextField txtNit;
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDireccion;
    @FXML private Label lblMensaje;
    @FXML private TextField txtBuscar;
    
    @FXML private TableView<Editorial> tablaEditoriales;
    @FXML private TableColumn<Editorial, String> colNit;
    @FXML private TableColumn<Editorial, String> colNombre;
    @FXML private TableColumn<Editorial, String> colTelefono;
    @FXML private TableColumn<Editorial, String> colDireccion;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnPrimero;
    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnUltimo;

    private boolean modoEdicion = false;
    private final EditorialDAO editorialDAO = new EditorialDAOImpl();
    private final ObservableList<Editorial> listaEditoriales = FXCollections.observableArrayList();
    private final FilteredList<Editorial> editorialesFiltradas = new FilteredList<>(listaEditoriales, p -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarTabla();
        tablaEditoriales.setItems(editorialesFiltradas);
        seleccionarFila();
        configurarBusqueda();
    }

    public void configurarTabla() {
        colNit.setCellValueFactory(new PropertyValueFactory<>("nit"));
colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreEditorial"));
colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoEditorial"));
colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionEditorial"));
    }

    private void cargarTabla() {
        try {
            listaEditoriales.setAll(editorialDAO.listar());
        } catch (Exception e) {
            mostrarError("Error al cargar la tabla: " + e.getMessage());
        }
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarEditoriales());
    }

    private void filtrarEditoriales() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            editorialesFiltradas.setPredicate(p -> true);
        } else {
            editorialesFiltradas.setPredicate(editorial ->
                    editorial.getNit().toLowerCase().contains(busqueda)
                    || editorial.getNombreEditorial().toLowerCase().contains(busqueda)
                    || editorial.getTelefonoEditorial().toLowerCase().contains(busqueda)
                    || editorial.getDireccionEditorial().toLowerCase().contains(busqueda));
        }
    }

    private void seleccionarFila() {
        tablaEditoriales.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtNit.setText(newSelection.getNit());
                        txtNombre.setText(newSelection.getNombreEditorial());
                        txtTelefono.setText(newSelection.getTelefonoEditorial());
                        txtDireccion.setText(newSelection.getDireccionEditorial());
                        desactivarFormulario();
                    }
                });
    }

    @FXML
    private void handleGuardar() {
        try {
            if (txtNit.getText().trim().isEmpty() || 
                txtNombre.getText().trim().isEmpty() || 
                txtTelefono.getText().trim().isEmpty() || 
                txtDireccion.getText().trim().isEmpty()) {
                
                mostrarAdvertencia("Por favor, llene todos los campos obligatorios.");
                lblMensaje.setText("Campos incompletos.");
                return;
            }

            Editorial editorial = new Editorial();
            editorial.setNit(txtNit.getText().trim());
            editorial.setNombreEditorial(txtNombre.getText().trim());
            editorial.setTelefonoEditorial(txtTelefono.getText().trim());
            editorial.setDireccionEditorial(txtDireccion.getText().trim());

            boolean guardado;
            if (modoEdicion) {
                guardado = editorialDAO.actualizar(editorial);
            } else {
                guardado = editorialDAO.crear(editorial);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion ? "Editorial actualizada exitosamente." : "Editorial registrada exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la editorial en la base de datos.");
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
        tablaEditoriales.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaEditoriales.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtNit.requestFocus();
    }

    @FXML
    private void handleEditar() {
        Editorial seleccion = tablaEditoriales.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una editorial de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    @FXML
    private void handlePrimero() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectFirst();
            tablaEditoriales.scrollTo(0);
        }
    }

    @FXML
    private void handleAnterior() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectPrevious();
            if (tablaEditoriales.getSelectionModel().getSelectedIndex() >= 0) {
                tablaEditoriales.scrollTo(tablaEditoriales.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleSiguiente() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectNext();
            if (tablaEditoriales.getSelectionModel().getSelectedIndex() >= 0) {
                tablaEditoriales.scrollTo(tablaEditoriales.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleUltimo() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectLast();
            tablaEditoriales.scrollTo(tablaEditoriales.getItems().size() - 1);
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
        txtNit.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    private void activarFormulario() {
        txtNit.setDisable(false);
        txtNombre.setDisable(false);
        txtTelefono.setDisable(false);
        txtDireccion.setDisable(false);
    }

    private void desactivarFormulario() {
        txtNit.setDisable(true);
        txtNombre.setDisable(true);
        txtTelefono.setDisable(true);
        txtDireccion.setDisable(true);
    }

    private void activarNavegacion() {
        tablaEditoriales.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        txtBuscar.setDisable(false);
    }

    private void desactivarNavegacion() {
        tablaEditoriales.setDisable(true);
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