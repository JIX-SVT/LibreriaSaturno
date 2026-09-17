package org.lsa.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import org.lsa.model.Cliente;
import org.lsa.model.DetalleVenta;
import org.lsa.model.Libro;
import org.lsa.utils.Conexion;

public class NuevaVentaController implements Initializable {

    @FXML private ComboBox<Cliente> cmbCliente;  
    @FXML private ComboBox<Libro> cmbLibro; 
    @FXML private Spinner<Integer> spnCantidad;
    @FXML private Label lblTotal;

    @FXML private TableView<DetalleVenta> tblUsuarios;
    @FXML private TableColumn<DetalleVenta, String> colId;
    @FXML private TableColumn<DetalleVenta, String> colUsuario;
    @FXML private TableColumn<DetalleVenta, Double> colNombre;
    @FXML private TableColumn<DetalleVenta, Integer> colApellido;
    @FXML private TableColumn<DetalleVenta, Double> colCorreo;

    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private ObservableList<DetalleVenta> listaDetalles = FXCollections.observableArrayList();

    private double totalVenta = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        configurarSpinner();
        cargarClientes();
        cargarLibros();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("subTotalDetalle"));
        tblUsuarios.setItems(listaDetalles);
    }

    private void configurarSpinner() {
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spnCantidad.setValueFactory(valueFactory);
    }

    private void cargarClientes() {
        String sql = "SELECT cui, nombre_cliente, apellido_cliente, correo_electronico FROM clientes";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            listaClientes.clear();
            while (rs.next()) {
                listaClientes.add(new Cliente(
                    rs.getLong("cui"),
                    rs.getString("nombre_cliente"),
                    rs.getString("apellido_cliente"),
                    rs.getString("correo_electronico")
                ));
            }
            cmbCliente.setItems(listaClientes);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los clientes.");
        }
    }      

    private void cargarLibros() {
        String sql = "SELECT isbn, titulo, fecha_publicacion, precio, id_categoria, nit_editorial, stock FROM libros";
        try (Connection conn = Conexion.getInstancia().conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            listaLibros.clear();
            while (rs.next()) {
                listaLibros.add(new Libro(
                    rs.getString("isbn"),
                    rs.getString("titulo"),
                    rs.getDate("fecha_publicacion"),
                    rs.getDouble("precio"),
                    rs.getInt("id_categoria"),
                    rs.getString("nit_editorial"),
                    rs.getInt("stock")
                ));
            }
            cmbLibro.setItems(listaLibros);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron cargar los libros.");
        }
    }

    @FXML
    public void handleAgregarTabla(ActionEvent event) {
        Libro libroSeleccionado = cmbLibro.getValue();
        Integer cantidad = spnCantidad.getValue();

        if (libroSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un libro.");
            return;
        }

        if (cantidad == null || cantidad <= 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Ingrese una cantidad válida.");
            return;
        }

        if (cantidad > libroSeleccionado.getStock()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Stock Insuficiente", 
                "El libro seleccionado solo cuenta con " + libroSeleccionado.getStock() + " unidades en inventario.");
            return;
        }

        boolean existe = false;
        for (DetalleVenta d : listaDetalles) {
            if (d.getIsbn().equals(libroSeleccionado.getIsbn())) {
                int nuevaCantidad = d.getCantidad() + cantidad;
                
                if (nuevaCantidad > libroSeleccionado.getStock()) {
                    mostrarAlerta(Alert.AlertType.WARNING, "Stock Insuficiente", 
                        "La cantidad acumulada (" + nuevaCantidad + ") supera el stock disponible (" + libroSeleccionado.getStock() + ").");
                    return;
                }
                
                d.setCantidad(nuevaCantidad);
                existe = true;
                break;
            }
        }

        if (!existe) {
            listaDetalles.add(new DetalleVenta(
                libroSeleccionado.getIsbn(),
                libroSeleccionado.getTitulo(),
                libroSeleccionado.getPrecio(),
                cantidad
            ));
        }

        tblUsuarios.refresh();
        calcularTotal();
    }

    private void calcularTotal() {
        totalVenta = 0.0;
        for (DetalleVenta detalle : listaDetalles) {
            totalVenta += detalle.getSubTotalDetalle();
        }
        lblTotal.setText(String.format("Total: Q%.2f", totalVenta));
    }

    @FXML
    public void handleRegistrarventa(ActionEvent event) {
        Cliente cliente = cmbCliente.getValue();
        if (cliente == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un cliente.");
            return;
        }
        if (listaDetalles.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Advertencia", "La lista de venta está vacía.");
            return;
        }

        String sqlVenta = "INSERT INTO Venta (id_cliente, total, fecha) VALUES (?, ?, NOW())";
        String sqlDetalle = "INSERT INTO DetalleVenta (id_venta, isbn, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE libros SET stock = stock - ? WHERE isbn = ?";

        Connection conn = null;
        try {
            conn = Conexion.getInstancia().conectar();
            conn.setAutoCommit(false);

            PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            stmtVenta.setLong(1, cliente.getCui());
            stmtVenta.setDouble(2, totalVenta);
            stmtVenta.executeUpdate();

            ResultSet rsKeys = stmtVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rsKeys.next()) {
                idVenta = rsKeys.getInt(1);
            }

            PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle);
            PreparedStatement stmtStock = conn.prepareStatement(sqlUpdateStock);

            for (DetalleVenta d : listaDetalles) {
                stmtDetalle.setInt(1, idVenta);
                stmtDetalle.setString(2, d.getIsbn());
                stmtDetalle.setInt(3, d.getCantidad());
                stmtDetalle.setDouble(4, d.getPrecioUnitario());
                stmtDetalle.addBatch();

                stmtStock.setInt(1, d.getCantidad());
                stmtStock.setString(2, d.getIsbn());
                stmtStock.addBatch();
            }

            stmtDetalle.executeBatch();
            stmtStock.executeBatch();

            conn.commit();

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Venta registrada correctamente.");
            limpiarFormulario();
            cargarLibros();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Falló el registro de la venta.");
        }
    }

    private void limpiarFormulario() {
        listaDetalles.clear();
        cmbCliente.getSelectionModel().clearSelection();
        cmbLibro.getSelectionModel().clearSelection();
        spnCantidad.getValueFactory().setValue(1);
        calcularTotal();
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}