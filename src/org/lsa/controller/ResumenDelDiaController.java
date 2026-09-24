package org.lsa.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.lsa.dao.FacturaDAO;
import org.lsa.daoimpl.FacturaDAOImpl;
import org.lsa.exception.DaoException;
import org.lsa.model.Factura;
import org.lsa.system.Main;

public class ResumenDelDiaController implements Initializable {

    private final FacturaDAO facturaDAO = new FacturaDAOImpl();
    private final ObservableList<Factura> listaVentasDia = FXCollections.observableArrayList();

    // Etiquetas superiores para los indicadores rápidos 
    @FXML private Label lblVentaTotalDia;
    @FXML private Label lblTotalFacturas;
    
    // Componentes de la tabla 
    @FXML private TableView<Factura> tablaResumen;
    @FXML private TableColumn<Factura, Integer> colNoFactura;
    @FXML private TableColumn<Factura, String> colFecha;
    @FXML private TableColumn<Factura, String> colCliente;
    @FXML private TableColumn<Factura, Double> colTotal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarResumenDelDia();
    }

    private void configurarTabla() {
        colNoFactura.setCellValueFactory(new PropertyValueFactory<>("numeroFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaEmision"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("granTotal"));
    }

    private void cargarResumenDelDia() {
        try {
            ArrayList<Factura> resultados = facturaDAO.obtenerVentasDelDia();
            listaVentasDia.setAll(resultados);
            tablaResumen.setItems(listaVentasDia);
            
            double ventaTotal = resultados.stream()
                    .mapToDouble(Factura::getGranTotal)
                    .distinct()
                    .sum();

            // Contamos cuántas facturas únicas se emitieron hoy
            long totalFacturasUnicas = resultados.stream()
                    .map(Factura::getNumeroFactura)
                    .distinct()
                    .count();

            lblVentaTotalDia.setText(String.format("Q %.2f", ventaTotal));
            lblTotalFacturas.setText(String.valueOf(totalFacturasUnicas));

        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void handleVolver() {
        try {
            Main.cambiarEscena("/org/lsa/view/DashboardCajeroView.fxml"); 
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
}