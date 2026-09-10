package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
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
import org.lsa.model.Libro;
import org.lsa.utils.SesionUsuario;

public class DashboardCajeroController implements Initializable {

    private static final Logger log = Logger.getLogger(DashboardCajeroController.class.getName());

    @FXML private TextField txtBusqueda;
    @FXML private TableView<Libro> tblLibros; 
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, String> colStock;
    @FXML private Label lblVentasHoy;

    @FXML private TableView<Carrito> tblCarrito;
    @FXML private TableColumn<Carrito, String> colCarritoIsbn;
    @FXML private TableColumn<Carrito, String> colCarritoTitulo;
    @FXML private TableColumn<Carrito, Double> colCarritoPrecio;
    @FXML private TableColumn<Carrito, Integer> colCarritoStock;
    @FXML private TableColumn<Carrito, Double> colCarritoSubtotal;
    
    @FXML private Label lblTotalPagar;
    private final LibroDAO libroDAO = new LibroDAOImpl();

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

        colCarritoIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colCarritoTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colCarritoPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colCarritoStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCarritoSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tblCarrito.setItems(listaCarrito);
        tblLibros.setItems(FXCollections.observableArrayList(libroDAO.listarTodos()));
        cargarDatosTabla();
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
        log.info("Actualizando listaLibros y tblLibros desde el DAO.");
        List<Libro> librosObtenidos = libroDAO.listarTodos();
        listaLibros.clear();
        listaLibros.addAll(librosObtenidos);
        tblLibros.setItems(listaLibros); 
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
        log.info("Intentando regresar a la vista del menú principal.");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Menú Principal");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al intentar volver al menú principal", e);
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la vista del menú.", ButtonType.OK);
            alerta.showAndWait();
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
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo regresar al login.", ButtonType.OK);
            alerta.showAndWait();
        }
    }

    @FXML
    public void handleAgregarAlCarrito(ActionEvent event) {
        Libro libroSeleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            log.warning("Intento de agregar al carrito sin seleccionar un libro.");
            System.out.println("Debe seleccionar un libro primero.");
            return;
        }

        log.info("Agregando libro al carrito. ISBN: " + libroSeleccionado.getIsbn() + ", Título: " + libroSeleccionado.getTitulo());
        boolean encontrado = false;
        for (Carrito item : listaCarrito) {
            if (item.getIsbn().equals(libroSeleccionado.getIsbn())) {
                item.setStock(item.getStock() + 1);
                tblCarrito.refresh();
                encontrado = true;
                log.info("Incrementada cantidad en carrito para ISBN: " + item.getIsbn() + ". Nueva cantidad: " + item.getStock());
                break;
            }
        }
        if (!encontrado) {
            Carrito nuevoItem = new Carrito(
                libroSeleccionado.getIsbn(),
                libroSeleccionado.getTitulo(),
                libroSeleccionado.getPrecio(),
                1
            );
            listaCarrito.add(nuevoItem);
            log.info("Nuevo ítem agregado al carrito: " + libroSeleccionado.getTitulo());
        }

        actualizarTotal();
    }

    @FXML
    public void handleQuitarDelCarrito(ActionEvent event) {
        Carrito seleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            log.info("Removiendo ítem del carrito. ISBN: " + seleccionado.getIsbn() + ", Título: " + seleccionado.getTitulo());
            listaCarrito.remove(seleccionado);
            actualizarTotal();
        } else {
            log.warning("Intento de quitar ítem del carrito sin seleccionar ninguno.");
        }
    }

    private void actualizarTotal() {
        double total = 0.0;
        for (Carrito item : listaCarrito) {
            total += item.getSubtotal();
        }
        log.info("Total del carrito actualizado a: Q" + total);
        lblTotalPagar.setText(String.format("Total: Q%.2f", total));
    }

    @FXML
    public void handleProcesarPago(ActionEvent event) {
        log.info("Procesando pago con " + listaCarrito.size() + " elementos en el carrito.");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DetalleVentaview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Reportes de Ventas");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error de I/O al cargar la vista DetalleVentaview.fxml en procesar pago", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de reportes de ventas.");
        }
        listaCarrito.clear();
        actualizarTotal();
    }

    @FXML
    public void handleResumenDia(ActionEvent event) {
        log.info("Solicitando vista de resumen del día (DetalleVentaview.fxml).");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DetalleVentaview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Reportes de Ventas");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error de I/O al cargar la vista DetalleVentaview.fxml en resumen del día", e);
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de reportes de ventas.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        log.info("Mostrando alerta en pantalla. Tipo: " + tipo + ", Título: " + titulo);
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}