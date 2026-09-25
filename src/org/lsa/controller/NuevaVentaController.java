package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.lsa.dao.ClienteDAO;
import org.lsa.dao.LibroDAO;
import org.lsa.dao.VentaDAO;
import org.lsa.daoimpl.ClienteDAOImpl;
import org.lsa.daoimpl.LibroDAOImpl;
import org.lsa.daoimpl.VentaDAOImpl;
import org.lsa.exception.ValidacionException;
import org.lsa.model.Cliente;
import org.lsa.model.DetalleVenta;
import org.lsa.model.Libro;
import org.lsa.model.Usuario;
import org.lsa.model.Venta;
import org.lsa.service.VentaService;
import org.lsa.system.Main;
import org.lsa.utils.SesionUsuario;

public class NuevaVentaController implements Initializable {

    private static final Logger log = Logger.getLogger(NuevaVentaController.class.getName());

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Libro> cmbLibro;
    @FXML private Spinner<Integer> spCantidad;
    @FXML private Button btnAgregar;
    @FXML private Button btnRegistrar;
    @FXML private Button btnQuitar;
    @FXML private Button btnVaciar;
    @FXML private TableView<DetalleVenta> tablaLineas;
    @FXML private TableColumn<DetalleVenta, String> colIsbn;
    @FXML private TableColumn<DetalleVenta, String> colTitulo;
    @FXML private TableColumn<DetalleVenta, Double> colPrecio;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotal;
    @FXML private Label lblTotal;
    @FXML private Label lblMensaje;

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final VentaService ventaService = new VentaService();
    private final ObservableList<DetalleVenta> lineasVenta = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarCombos();
        tablaLineas.setItems(lineasVenta);
        configurarTabla();
        configurarSpinner();
        calcularTotal();
    }

    private void cargarCombos() {
        try {
            cmbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listar()));
            cmbLibro.setItems(FXCollections.observableArrayList(libroDAO.listar()));
        } catch (Exception e) {
            mostrarError("Error al cargar combos: " + e.getMessage());
        }
    }

    private void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subTotalDetalle"));
    }

    private void configurarSpinner() {
        spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
    }

    private double calcularTotal() {
        double total = 0;
        for (DetalleVenta linea : lineasVenta) {
            total += linea.getSubTotalDetalle();
        }
        lblTotal.setText(String.format("Total: Q%.2f", total));
        return total;
    }

    @FXML
    private void handleAgregarLinea() {
        Libro libro = cmbLibro.getValue();
        if (libro == null) {
            mostrarAdvertencia("Seleccione un libro para agregar a la venta.");
            return;
        }
        int cantidadNueva = spCantidad.getValue();
        int cantidadAcumulada = 0;

        DetalleVenta itemExistente = null;
        for (DetalleVenta item : lineasVenta) {
            if (item.getIsbn().equals(libro.getIsbn())) {
                itemExistente = item;
                cantidadAcumulada = item.getCantidad();
                break;
            }
        }
        if (libro.getStock() < (cantidadAcumulada + cantidadNueva)) {
            mostrarAdvertencia("Stock insuficiente. Disponible: " + libro.getStock() + ".");
            return;
        }
        if (itemExistente != null) {
            itemExistente.setCantidad(cantidadAcumulada + cantidadNueva);
            tablaLineas.refresh();
        } else {
            DetalleVenta nuevoItem = new DetalleVenta(
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getPrecio(),
                cantidadNueva
            );
            lineasVenta.add(nuevoItem);
        }

        calcularTotal();
        lblMensaje.setText("");
        cmbLibro.setValue(null);
        spCantidad.getValueFactory().setValue(1);
    }

    @FXML
    private void handleQuitarLinea() {
        DetalleVenta seleccion = tablaLineas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarAdvertencia("Seleccione una línea de la tabla para quitar.");
            return;
        }
        lineasVenta.remove(seleccion);
        calcularTotal();
    }

    @FXML
    private void handleVaciar() {
        lineasVenta.clear();
        calcularTotal();
        lblMensaje.setText("");
    }

    @FXML  
    private void handleRegistrarVenta() {
        try {
            Usuario usuarioActual = SesionUsuario.getInstancia().getUsuarioActual();
            if (usuarioActual == null) {
                throw new ValidacionException("No hay una sesión de usuario activa. Inicie sesión nuevamente.");
            }
            Cliente clienteSeleccionado = cmbCliente.getValue();
            if (clienteSeleccionado == null) {
                throw new ValidacionException("Seleccione el cliente de la venta.");
            }
            if (lineasVenta.isEmpty()) {
                throw new ValidacionException("Agregue al menos un libro a la venta.");
            }
            
       if (lineasVenta.isEmpty()) {
            throw new ValidacionException("Agregue al menos un libro a la venta.");
        }

        int idUsuario = usuarioActual.getIdUsuario();
        long cuiCliente = cmbCliente.getValue().getCui();
        double totalCalculado = calcularTotal();

        Venta nuevaVenta = new Venta();
        nuevaVenta.setSubTotal(String.valueOf(totalCalculado));
        nuevaVenta.setDescuento(0.00);
        nuevaVenta.setTotalVenta(totalCalculado);
        nuevaVenta.setCuiCliente(cuiCliente);
        nuevaVenta.setId_usuario(idUsuario);
        boolean exito = ventaService.procesarVenta(nuevaVenta, lineasVenta);

        if (!exito) {
            mostrarError("No se pudo registrar la venta. Verifique el stock.");
            return;
        }
        int idVentaGenerada = nuevaVenta.getIdVenta();
        FacturaController.setNoVentaSeleccionada(idVentaGenerada);
        limpiarVenta();
        cargarCombos();
        Main.cambiarVista("/org/lsa/view/FacturaImpresaView.fxml");

    } catch (ValidacionException e) {
        mostrarAdvertencia(e.getMessage());
        lblMensaje.setText(e.getMessage());
    } catch (Exception e) {
        mostrarError("Error al registrar la venta y redirigir: " + e.getMessage());
    }
}
    private void limpiarVenta() {
        lineasVenta.clear();
        cmbCliente.setValue(null);
        cmbLibro.setValue(null);
        spCantidad.getValueFactory().setValue(1);
        calcularTotal();
    }

    @FXML
    public void handleVolver(ActionEvent event) {
        try {
            Main.cambiarVista("/org/lsa/view/DashboardCajeroView.fxml");
        } catch (Exception e) {
            mostrarError("Error al volver al menú: " + e.getMessage());
        }
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