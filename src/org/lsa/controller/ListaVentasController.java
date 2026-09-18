package org.lsa.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.lsa.dao.ClienteDAO;
import org.lsa.dao.UsuarioDAO;
import org.lsa.dao.VentaDAO;
import org.lsa.daoimpl.ClienteDAOImpl;
import org.lsa.daoimpl.UsuarioDAOImpl;
import org.lsa.daoimpl.VentaDAOImpl;
import org.lsa.exception.ValidacionException;
import org.lsa.model.Cliente;
import org.lsa.model.Usuario;
import org.lsa.model.Venta;
import org.lsa.system.Main;
import org.lsa.utils.ControlAcceso;

public class ListaVentasController implements Initializable {

    private static final Logger log = Logger.getLogger(ListaVentasController.class.getName());

    // Componentes del FXML - Panel Izquierdo
    @FXML private TextField txtTotal;
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<Usuario> cmbUsuario;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Label lblMensaje;

    // Componentes del FXML - Panel Central
    @FXML private TextField txtBuscar;
    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, Integer> colNoVenta;
    @FXML private TableColumn<Venta, Timestamp> colFechaVenta;
    @FXML private TableColumn<Venta, Double> colTotalVenta;
    @FXML private TableColumn<Venta, Long> colCuiCliente;
    @FXML private TableColumn<Venta, Integer> colUsuario;

    // Botones de Navegación
    @FXML private Button btnPrimero;
    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private Button btnUltimo;

    // Variables de Estado y Persistencia
    private boolean modoEdicion = false;
    private Venta enEdicion;
    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    
    private final ObservableList<Venta> listaVentas = FXCollections.observableArrayList();
    private final FilteredList<Venta> ventasFiltradas = new FilteredList<>(listaVentas, p -> true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        configurarFormatosCombo();
        cargarClientes();
        cargarUsuarios();
        cargarTabla();
        
        tablaVentas.setItems(ventasFiltradas);
        seleccionarFila();
        configurarBusqueda();
    }

    public void configurarTabla() {
        colNoVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colFechaVenta.setCellValueFactory(new PropertyValueFactory<>("fechaVenta"));
        colTotalVenta.setCellValueFactory(new PropertyValueFactory<>("totalVenta"));
        colCuiCliente.setCellValueFactory(new PropertyValueFactory<>("cuiCliente"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
    }

    private void configurarFormatosCombo() {
        cmbUsuario.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? "" : usuario.getIdUsuario() + " - " + usuario.getNombreUsuario();
            }

            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });
        cmbCliente.setConverter(new StringConverter<Cliente>() {
            @Override
            public String toString(Cliente cliente) {
                return cliente == null ? "" : cliente.getCui() + " - " + cliente.getNombreCliente() + " " + cliente.getApellidoCliente();
            }

            @Override
            public Cliente fromString(String string) {
                return null;
            }
        });
    }

    private void cargarTabla() {
        try {
            listaVentas.setAll(ventaDAO.listar());
        } catch (Exception e) {
            mostrarError("Error al cargar ventas: " + e.getMessage());
        }
    }

    private void cargarClientes() {
        try {
            cmbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos()));
        } catch (Exception e) {
            mostrarError("Error al cargar clientes: " + e.getMessage());
        }
    }

    private void cargarUsuarios() {
        try {
            cmbUsuario.setItems(FXCollections.observableArrayList(usuarioDAO.listarTodos()));
        } catch (Exception e) {
            mostrarError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarVentas());
    }

    private void filtrarVentas() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            ventasFiltradas.setPredicate(p -> true);
        } else {
            ventasFiltradas.setPredicate(venta ->
                    String.valueOf(venta.getIdVenta()).contains(busqueda)
                    || (venta.getFechaVenta() != null && venta.getFechaVenta().toString().toLowerCase().contains(busqueda))
                    || String.valueOf(venta.getTotalVenta()).contains(busqueda)
                    || String.valueOf(venta.getCuiCliente()).contains(busqueda)
                    || String.valueOf(venta.getId_usuario()).contains(busqueda));
        }
    }

    private void seleccionarFila() {
        tablaVentas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtTotal.setText(String.valueOf(newSelection.getTotalVenta()));
                        
                        cmbCliente.setValue(null);
                        for (Cliente cliente : cmbCliente.getItems()) {
                            if (cliente.getCui() == newSelection.getCuiCliente()) {
                                cmbCliente.setValue(cliente);
                                break;
                            }
                        }
                        
                        Timestamp timestamp = newSelection.getFechaVenta();
                        if (timestamp != null) {
                            dpFecha.setValue(timestamp.toLocalDateTime().toLocalDate());
                        } else {
                            dpFecha.setValue(null);
                        }
                        
                        cmbUsuario.setValue(null);
                        for (Usuario usuario : cmbUsuario.getItems()) {
                            if (usuario.getIdUsuario() == newSelection.getId_usuario()) {
                                cmbUsuario.setValue(usuario);
                                break;
                            }
                        }
                        
                        desactivarFormulario();
                    }
                });
    }

    @FXML
    private void handleGuardar() {
        try {
            if (txtTotal.getText().trim().isEmpty()) {
                throw new ValidacionException("El campo total es obligatorio.");
            }
            if (cmbCliente.getValue() == null) {
                throw new ValidacionException("Seleccione un cliente.");
            }

            double totalNum = Double.parseDouble(txtTotal.getText().trim().replace(",", "."));

            Venta venta = new Venta();
            venta.setIdVenta(modoEdicion ? enEdicion.getIdVenta() : 0);
            
            if (dpFecha.getValue() != null) {
                venta.setFechaVenta(Timestamp.valueOf(dpFecha.getValue().atStartOfDay()));
            } else {
                venta.setFechaVenta(new Timestamp(System.currentTimeMillis()));
            }
            venta.setSubTotal(String.valueOf(totalNum));
            venta.setDescuento(0.00);
            venta.setTotalVenta(totalNum);
            venta.setCuiCliente(cmbCliente.getValue().getCui());
            if (cmbUsuario.getValue() != null) {
                venta.setId_usuario(cmbUsuario.getValue().getIdUsuario());
            } else if (ControlAcceso.getUsuarioLogueado() != null) {
                venta.setId_usuario(ControlAcceso.getUsuarioLogueado().getIdUsuario());
            } else {
                throw new ValidacionException("No hay usuario en sesión ni seleccionado.");
            }

            boolean guardado;
            if (modoEdicion) {
                guardado = ventaDAO.actualizar(venta);
            } else {
                guardado = ventaDAO.insertar(venta);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion ? "Venta actualizada." : "Venta registrada.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la venta.");
            }
        } catch (ValidacionException e) {
            mostrarAdvertencia(e.getMessage());
            lblMensaje.setText(e.getMessage());
        } catch (NumberFormatException e) {
            mostrarAdvertencia("El formato del total no es válido.");
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
        tablaVentas.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        dpFecha.setValue(LocalDate.now());
        if (ControlAcceso.getUsuarioLogueado() != null) {
            for (Usuario u : cmbUsuario.getItems()) {
                if (u.getIdUsuario() == ControlAcceso.getUsuarioLogueado().getIdUsuario()) {
                    cmbUsuario.setValue(u);
                    break;
                }
            }
        }
        txtTotal.requestFocus();
    }

    @FXML
    private void handleEditar() {
        Venta seleccion = tablaVentas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una venta de la tabla para editar.");
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
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectFirst();
            tablaVentas.scrollTo(0);
        }
    }

    @FXML
    private void handleAnterior() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectPrevious();
            if (tablaVentas.getSelectionModel().getSelectedIndex() >= 0) {
                tablaVentas.scrollTo(tablaVentas.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleSiguiente() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectNext();
            if (tablaVentas.getSelectionModel().getSelectedIndex() >= 0) {
                tablaVentas.scrollTo(tablaVentas.getSelectionModel().getSelectedIndex());
            }
        }
    }

    @FXML
    private void handleUltimo() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectLast();
            tablaVentas.scrollTo(tablaVentas.getItems().size() - 1);
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        try {
            Main.cambiarVista(
                    "/org/lsa/view/DashboardCajeroView.fxml");
        } catch (Exception e) {
            mostrarError(
                    "Error al volver al menú: "
                    + e.getMessage()
            );
        }
    }
        @FXML
    private void handleFactura(ActionEvent event) {
        try {
            Main.cambiarVista(
                        "/org/lsa/view/FacturaView.fxml");
        } catch (Exception e) {
            mostrarError(
                    "Error al volver al menú: "
                    + e.getMessage()
            );
        }
    }
    private void limpiarFormulario() {
        txtTotal.clear();
        dpFecha.setValue(null);
        cmbUsuario.setValue(null);
        cmbCliente.setValue(null);
    }

    private void activarFormulario() {
        txtTotal.setDisable(false);
        dpFecha.setDisable(false);
        cmbCliente.setDisable(false);
        cmbUsuario.setDisable(false);
    }

    private void desactivarFormulario() {
        txtTotal.setDisable(true);
        dpFecha.setDisable(true);
        cmbUsuario.setDisable(true);
        cmbCliente.setDisable(true);
    }

    private void activarNavegacion() {
        tablaVentas.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        txtBuscar.setDisable(false);
    }

    private void desactivarNavegacion() {
        tablaVentas.setDisable(true);
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