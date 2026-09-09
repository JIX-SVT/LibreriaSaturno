package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import org.lsa.model.Carrito;
import org.lsa.model.Libro;
import org.lsa.utils.SesionUsuario;

public class DashboardCajeroController implements Initializable {

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
        configurarTabla();
        cargarTabla();
        tblLibros.setItems(librosFiltrados);
        configurarBusqueda();
    }

    private void configurarTabla() {
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
    @FXML
    public void handleBuscar(ActionEvent event) {
        filtrarLibros();
    }
    @FXML
public void cargarDatosTabla() {
    List<Libro> librosObtenidos = libroDAO.listarTodos();
    listaLibros.clear();
    listaLibros.addAll(librosObtenidos);
    tblLibros.setItems(listaLibros); 
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
    @FXML
    public void handleAgregarAlCarrito(ActionEvent event) {
        Libro libroSeleccionado = tblLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado == null) {
            System.out.println("Debe seleccionar un libro primero.");
            return;
        }
        boolean encontrado = false;
        for (Carrito item : listaCarrito) {
            if (item.getIsbn().equals(libroSeleccionado.getIsbn())) {
                item.setStock(item.getStock() + 1);
                tblCarrito.refresh();
                encontrado = true;
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
        }

        actualizarTotal();
    }
    @FXML
    public void handleQuitarDelCarrito(ActionEvent event) {
        Carrito seleccionado = tblCarrito.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            listaCarrito.remove(seleccionado);
            actualizarTotal();
        }
    }
    private void actualizarTotal() {
        double total = 0.0;
        for (Carrito item : listaCarrito) {
            total += item.getSubtotal();
        }
        lblTotalPagar.setText(String.format("Total: Q%.2f", total));
    }

    @FXML
    public void handleProcesarPago(ActionEvent event) {
        if (listaCarrito.isEmpty()) {
            System.out.println("El carrito está vacío.");
            return;
        }

        System.out.println("Pago procesado correctamente.");
        listaCarrito.clear();
        actualizarTotal();
    }
}
