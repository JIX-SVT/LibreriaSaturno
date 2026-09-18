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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.lsa.dao.AutorDAO;
import org.lsa.daoimpl.AutorDAOImpl;
import org.lsa.model.Autor;

public class AutorController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(InventarioController.class.getName());
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtNacionalidad;
    @FXML
    private TextArea txtBiografia;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Autor> tablaAutores;
    @FXML
    private TableColumn<Autor, Integer> colIdAutor;
    @FXML
    private TableColumn<Autor, String> colNombreAutor;
    @FXML
    private TableColumn<Autor, String> colApellidoAutor;
    @FXML
    private TableColumn<Autor, String> colNacionalidad;
    @FXML
    private TableColumn<Autor, String> colBiografia;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
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
    @FXML
private Button btnGuardar;
@FXML
private Button btnCancelar;

    private boolean modoEdicion = false;
    private Autor enEdicion;
    private final AutorDAO autorDAO = new AutorDAOImpl();
    private final ObservableList<Autor> listaAutores = FXCollections.observableArrayList();
    private final FilteredList<Autor> autoresFiltrados = new FilteredList<>(listaAutores, p -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        tablaAutores.setItems(autoresFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    public void configurarTabla() {
        colIdAutor.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        colNombreAutor.setCellValueFactory(new PropertyValueFactory<>("nombreAutor"));
        colApellidoAutor.setCellValueFactory(new PropertyValueFactory<>("apellidoAutor"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
        colBiografia.setCellValueFactory(new PropertyValueFactory<>("biografia"));
    }

    private void cargarTabla() {
        try {
            listaAutores.setAll(autorDAO.listar());
        } catch (Exception e) {
            mostrarError("Error al cargar la lista de autores: " + e.getMessage());
        }
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarAutores());
    }

    private void filtrarAutores() {
        String busqueda = txtBuscar.getText() != null ? txtBuscar.getText().trim().toLowerCase() : "";
        if (busqueda.isEmpty()) {
            autoresFiltrados.setPredicate(p -> true);
        } else {
            autoresFiltrados.setPredicate(autor ->
                    String.valueOf(autor.getIdAutor()).contains(busqueda)
                    || (autor.getNombreAutor() != null && autor.getNombreAutor().toLowerCase().contains(busqueda))
                    || (autor.getApellidoAutor() != null && autor.getApellidoAutor().toLowerCase().contains(busqueda))
                    || (autor.getNacionalidad() != null && autor.getNacionalidad().toLowerCase().contains(busqueda))
                    || (autor.getBiografia() != null && autor.getBiografia().toLowerCase().contains(busqueda)));
        }
    }

    private void seleccionarFila() {
        tablaAutores.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtNombre.setText(newSelection.getNombreAutor());
                        txtApellido.setText(newSelection.getApellidoAutor());
                        txtNacionalidad.setText(newSelection.getNacionalidad());
                        txtBiografia.setText(newSelection.getBiografia());
                        desactivarFormulario();
                    }
                });
    }

    @FXML
    private void handleGuardar() {
        try {
            if (txtNombre.getText().trim().isEmpty() 
                    || txtApellido.getText().trim().isEmpty() 
                    || txtNacionalidad.getText().trim().isEmpty()) {
                mostrarAdvertencia("Los campos Nombre, Apellido y Nacionalidad son obligatorios.");
                lblMensaje.setText("Por favor complete los campos requeridos.");
                return;
            }

            Autor autor = new Autor(
                    modoEdicion ? enEdicion.getIdAutor() : 0,
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtNacionalidad.getText().trim(),
                    txtBiografia.getText().trim());

            boolean guardado;
            if (modoEdicion) {
                guardado = autorDAO.actualizar(autor);
            } else {
                guardado = autorDAO.crear(autor);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Autor actualizado exitosamente."
                        : "Autor registrado exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la información del autor.");
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
        enEdicion = null;
        lblMensaje.setText("");
    }

    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        enEdicion = null;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaAutores.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtNombre.requestFocus();
    }

    @FXML
    private void handleEditar() {
        Autor seleccion = tablaAutores.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un autor de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        enEdicion = seleccion;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    @FXML
    private void handlePrimero() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectFirst();
            tablaAutores.scrollTo(0);
        }
    }

    @FXML
    private void handleAnterior() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectPrevious();
            if (tablaAutores.getSelectionModel().getSelectedIndex() >= 0) {
                tablaAutores.scrollTo(tablaAutores.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleSiguiente() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectNext();
            if (tablaAutores.getSelectionModel().getSelectedIndex() >= 0) {
                tablaAutores.scrollTo(tablaAutores.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleUltimo() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectLast();
            tablaAutores.scrollTo(tablaAutores.getItems().size() - 1);
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
        txtNombre.clear();
        txtApellido.clear();
        txtNacionalidad.clear();
        txtBiografia.clear();
    }

private void activarFormulario() {
    txtNombre.setDisable(false);
    txtApellido.setDisable(false);
    txtNacionalidad.setDisable(false);
    txtBiografia.setDisable(false);
    btnGuardar.setDisable(false);
    btnCancelar.setDisable(false);
}

private void desactivarFormulario() {
    txtNombre.setDisable(true);
    txtApellido.setDisable(true);
    txtNacionalidad.setDisable(true);
    txtBiografia.setDisable(true);
    btnGuardar.setDisable(true);
    btnCancelar.setDisable(true);
}

    private void activarNavegacion() {
        tablaAutores.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        txtBuscar.setDisable(false);
    }

    private void desactivarNavegacion() {
        tablaAutores.setDisable(true);
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