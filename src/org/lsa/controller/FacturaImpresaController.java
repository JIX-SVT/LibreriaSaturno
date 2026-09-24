package org.lsa.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.lsa.dao.FacturaDAO;
import org.lsa.daoimpl.FacturaDAOImpl;
import org.lsa.exception.DaoException;
import org.lsa.model.Factura;
import org.lsa.system.Main;

public class FacturaImpresaController implements Initializable {

    @FXML
    private TextArea txtAreaFactura; 

    private final FacturaDAO facturaDAO = new FacturaDAOImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (txtAreaFactura != null) {
            txtAreaFactura.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");
        }
        cargarFacturaFormateada();
    }

    private void cargarFacturaFormateada() {
        try {
            int idVenta = FacturaController.getNoVentaSeleccionada();
            List<Factura> lineas = facturaDAO.buscarFactura(idVenta);

            if (lineas == null || lineas.isEmpty()) {
                mostrarError("No se encontraron los datos de la factura.");
                return;
            }

            Factura encabezado = lineas.get(0);

            StringBuilder sb = new StringBuilder();
            sb.append("========================================\n");
            sb.append("            LIBRERÍA SATURNO            \n");
            sb.append("========================================\n");
            sb.append("Factura No.  : # ").append(encabezado.getNumeroFactura()).append("\n");
            sb.append("Fecha        : ").append(encabezado.getFechaEmision()).append("\n");
            sb.append("Cliente      : ").append(encabezado.getNombreCliente()).append("\n");
            sb.append("CUI          : ").append(encabezado.getCuiCliente()).append("\n");
            sb.append("Correo       : ").append(encabezado.getCorreoCliente()).append("\n");
            sb.append("Atendido por : ").append(encabezado.getUsuarioAtendio()).append("\n");
            sb.append("----------------------------------------\n");
            sb.append(String.format("%-18s %-5s %-10s %-8s\n", "Libro", "Cant", "P.Unit", "Subtotal"));
            sb.append("----------------------------------------\n");

            for (Factura item : lineas) {
                String titulo = item.getTituloLibro();
                if (titulo != null && titulo.length() > 18) {
                    titulo = titulo.substring(0, 15) + "...";
                }
                sb.append(String.format("%-18s %-5d Q%-9.2f Q%-7.2f\n",
                        titulo != null ? titulo : "",
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getSubtotal()));
            }

            sb.append("----------------------------------------\n");
            sb.append("TOTAL A PAGAR: Q ").append(String.format("%.2f", encabezado.getGranTotal())).append("\n");
            sb.append("========================================\n");
            sb.append("        ¡GRACIAS POR SU COMPRA!         \n");
            sb.append("========================================\n");

            txtAreaFactura.setText(sb.toString());

        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void handleVolver() {
        try {
            Main.cambiarEscena("/org/lsa/view/FacturaView.fxml");
        } catch (Exception e) {
            mostrarError("Error al volver: " + e.getMessage());
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