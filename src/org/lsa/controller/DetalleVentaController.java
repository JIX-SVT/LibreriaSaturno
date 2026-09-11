package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.lsa.daoimpl.VentaDAOImpl;
import org.lsa.model.Venta;
import org.lsa.utils.GeneradorTicket;

public class DetalleVentaController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DetalleVentaController.class.getName());

    @FXML private TableView<Venta> tblVentas;
    @FXML private TableColumn<Venta, Integer> colIdVenta;
    @FXML private TableColumn<Venta, Long> colCliente;
    @FXML private TableColumn<Venta, Integer> colCajero;
    @FXML private TableColumn<Venta, Double> colTotal;

    @FXML private Label lblFacturaNumero;
    @FXML private Label lblClienteDetalle;
    @FXML private Label lblEmpleadoDetalle;
    @FXML private Label lblTotalDetalle;
    @FXML private TextArea txtAreaFacturaVisual;
    
    private ObservableList<Venta> listaVentas = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cuiCliente"));
        colCajero.setCellValueFactory(new PropertyValueFactory<>("id_usuario"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalVenta"));

        cargarHistorialBaseDatos(); 

        tblVentas.setItems(listaVentas);
        txtAreaFacturaVisual.setText("Seleccione una venta para ver el ticket...");

        tblVentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleEnPanelLateral(newSelection);
            }
        });

        LOGGER.info("Vista Maestro-Detalle cargada correctamente.");
    }

    public void recibirNuevaFactura(Venta nuevaVenta) {
        if (nuevaVenta != null) {
            this.listaVentas.add(nuevaVenta);
            this.tblVentas.getSelectionModel().select(nuevaVenta);
            mostrarDetalleEnPanelLateral(nuevaVenta);
            LOGGER.info("Nueva factura recibida del cajero e inyectada en reportes: " + nuevaVenta.getIdVenta());
        }
    }

    private void cargarHistorialBaseDatos() {
        listaVentas.clear();
        
        VentaDAOImpl ventaDAO = new VentaDAOImpl();
        listaVentas.addAll(ventaDAO.listar());
        tblVentas.setItems(listaVentas);
    }

    private void mostrarDetalleEnPanelLateral(Venta venta) {
        lblFacturaNumero.setText("Factura: " + venta.getIdVenta());
        lblClienteDetalle.setText("CUI Cliente: " + venta.getCuiCliente());
        lblEmpleadoDetalle.setText("Usuario: " + venta.getId_usuario());
        lblTotalDetalle.setText(String.format("Total: Q %.2f", venta.getTotalVenta()));

        String textoTicket = GeneradorTicket.generarFormatoTicket(
            venta.getIdVenta(), 
            String.valueOf(venta.getId_usuario()), 
            venta.getTotalVenta()
        );
        txtAreaFacturaVisual.setText(textoTicket);
    }

    @FXML
    public void handleRegresarCajero(ActionEvent event) {
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardCajeroview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Dashboard Cajero");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            LOGGER.severe("Error de interfaz: " + e.getMessage());
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de cajero.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public int obtenerCantidadVentas() {
        return listaVentas.size();
    }
}