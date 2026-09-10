package org.lsa.controller;
 
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.lsa.utils.GeneradorTicket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lsa.daoimpl.CompraDAOImpl;
import org.lsa.model.DetalleCompra.VentaDTO;
 
public class DetalleVentaController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(DetalleVentaController.class.getName());
 
    @FXML private TableView<VentaDTO> tblVentas;
    @FXML private TableColumn<VentaDTO, String> colIdVenta;
    @FXML private TableColumn<VentaDTO, String> colCliente;
    @FXML private TableColumn<VentaDTO, String> colCajero;
    @FXML private TableColumn<VentaDTO, Double> colTotal;
 
    @FXML private Label lblFacturaNumero;
    @FXML private Label lblClienteDetalle;
    @FXML private Label lblEmpleadoDetalle;
    @FXML private Label lblTotalDetalle;
    @FXML private TextArea txtAreaFacturaVisual;
    
    private ObservableList<VentaDTO> listaVentas = FXCollections.observableArrayList(
        
    );
 
        @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colCajero.setCellValueFactory(new PropertyValueFactory<>("cajero"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
 
        // =============== AÑADE ESTA LÍNEA AQUÍ ===============
        cargarHistorialBaseDatos(); 
        // =====================================================
 
        tblVentas.setItems(listaVentas);
 
        txtAreaFacturaVisual.setText("Seleccione una venta para ver el ticket...");
 
        tblVentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleEnPanelLateral(newSelection);
            }
        });
 
        LOGGER.info("Vista Maestro-Detalle con ticket visual cargada correctamente.");
    }

    //Metodo para mostrar ventas
public void recibirNuevaFactura(VentaDTO nuevaVenta) {
        if (nuevaVenta != null) {
            this.listaVentas.add(nuevaVenta);
            this.tblVentas.getSelectionModel().select(nuevaVenta);
            mostrarDetalleEnPanelLateral(nuevaVenta);
            LOGGER.info("Nueva factura recibida del cajero e inyectada en reportes: " + nuevaVenta.getIdVenta());
        }
    }

     private void cargarHistorialBaseDatos() {
        listaVentas.clear();
        
        // 1. Instanciamos el DAO de manera local para conectarnos a la base de datos
        org.lsa.daoimpl.CompraDAOImpl compraDAO = new org.lsa.daoimpl.CompraDAOImpl();
        
        // 2. Extraemos el historial y lo agregamos a la lista de la interfaz
        listaVentas.addAll(compraDAO.listarHistorialCompras());
        tblVentas.setItems(listaVentas);
    }

 
    private void mostrarDetalleEnPanelLateral(VentaDTO venta) {
        lblFacturaNumero.setText("Factura: " + venta.getIdVenta());
        lblClienteDetalle.setText("Cliente: " + venta.getCliente());
        lblEmpleadoDetalle.setText("Empleado: " + venta.getCajero());
        lblTotalDetalle.setText(String.format("Total: Q %.2f", venta.getTotal()));
 
        String textoTicket = GeneradorTicket.generarFormatoTicket(venta.getIdVenta(), venta.getCajero(), venta.getTotal());
        txtAreaFacturaVisual.setText(textoTicket);
    }
    @FXML
    public void handleRegresarCajero(ActionEvent event) {
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardCajeroview.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Reportes de Ventas");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar la vista de reportes de ventas.");
        }
    }
        private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    int obtenerCantidadVentas() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}