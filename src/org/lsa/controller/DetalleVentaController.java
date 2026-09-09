package org.lsa.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
    @FXML private Button btnImprimirLateral;

    private ObservableList<VentaDTO> listaVentas = FXCollections.observableArrayList(
        new VentaDTO("F-001/2026", "Renta Sol S.L.", "Admin", 1235.00),
        new VentaDTO("F-002/2026", "Librería Central", "Cajero1", 450.50),
        new VentaDTO("F-003/2026", "Juan Pérez", "Admin", 89.00)
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("idVenta"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colCajero.setCellValueFactory(new PropertyValueFactory<>("cajero"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        tblVentas.setItems(listaVentas);

        tblVentas.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetalleEnPanelLateral(newSelection);
            }
        });

        LOGGER.info("Vista Maestro-Detalle limpia cargada correctamente.");
    }

    private void mostrarDetalleEnPanelLateral(VentaDTO venta) {
        lblFacturaNumero.setText("Factura: " + venta.getIdVenta());
        lblClienteDetalle.setText("Cliente: " + venta.getCliente());
        lblEmpleadoDetalle.setText("Empleado: " + venta.getCajero());
        lblTotalDetalle.setText(String.format("Total: Q %.2f", venta.getTotal()));
    }

    @FXML
    private void handleImprimirFactura(ActionEvent event) {
        VentaDTO seleccionada = tblVentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            LOGGER.warning("Debe seleccionar una factura de la tabla para imprimir.");
            return;
        }
        LOGGER.info("Imprimiendo comprobante de la factura: " + seleccionada.getIdVenta());
    }

    public static class VentaDTO {
        private String idVenta;
        private String cliente;
        private String cajero;
        private double total;

        public VentaDTO(String idVenta, String cliente, String cajero, double total) {
            this.idVenta = idVenta;
            this.cliente = cliente;
            this.cajero = cajero;
            this.total = total;
        }

        public String getIdVenta() { return idVenta; }
        public String getCliente() { return cliente; }
        public String getCajero() { return cajero; }
        public double getTotal() { return total; }
    }
}