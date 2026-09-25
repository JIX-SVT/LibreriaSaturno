package org.lsa.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.lsa.dao.FacturaDAO;
import org.lsa.daoimpl.FacturaDAOImpl;
import org.lsa.exception.DaoException;
import org.lsa.model.Factura;
import org.lsa.system.Main;

public class FacturaCompraController implements Initializable {
    private static int noVentaSeleccionada;

    public static void setNoVentaSeleccionada(int noVenta) {
        noVentaSeleccionada = noVenta;
    }



    private final FacturaDAO facturaDAO = new FacturaDAOImpl();

    @FXML private Label lblNoFactura;
    @FXML private Label lblFecha;
    @FXML private Label lblCliente;
    @FXML private Label lblCui;
    @FXML private Label lblCorreo;
    @FXML private Label lblUsuario;
    @FXML private Label lblTotal;
    
    @FXML private Button btnImprimir;
    @FXML private Button btnOk; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarFactura();
    }

    private void cargarFactura() {
        try {
            List<Factura> resultado = facturaDAO.buscarFactura(noVentaSeleccionada);
            if (resultado == null || resultado.isEmpty()) {
                mostrarError("No se encontró la factura de la venta " + noVentaSeleccionada + ".");
                return;
            }
            
            Factura encabezado = resultado.get(0);
            lblNoFactura.setText("# " + encabezado.getNumeroFactura());
            lblFecha.setText(encabezado.getFechaEmision());
            lblCliente.setText(encabezado.getNombreCliente());
            lblCui.setText(String.valueOf(encabezado.getCuiCliente()));
            lblCorreo.setText(encabezado.getCorreoCliente());
            lblUsuario.setText(encabezado.getUsuarioAtendio());
            lblTotal.setText(String.format("Q %.2f", encabezado.getGranTotal()));
            
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void handleOk() {
        try {
            Stage stage = (Stage) btnOk.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            mostrarError("Error al cerrar la ventana: " + e.getMessage());
        }
    }

@FXML
private void handleImprimirFactura() {
    try {
        FacturaImpresaCajaController.setNoVentaSeleccionada(noVentaSeleccionada);

        Main.cambiarEscena("/org/lsa/view/FacturaImpresaCajaView.fxml");
        
    } catch (Exception e) {
        mostrarError("Error al abrir la vista de impresión: " + e.getMessage());
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