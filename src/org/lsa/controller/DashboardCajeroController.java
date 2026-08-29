
package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.lsa.dao.LibroDAO;
import org.lsa.model.Libro;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;

public class DashboardCajeroController implements Initializable {

    @FXML private TextField txtBusqueda;
    @FXML private TableView<Libro> tblResultados;
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colAutor;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colStock;
    @FXML private Label lblVentasHoy;

    private final LibroDAO libroDAO = new LibroDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        lblVentasHoy.setText("Q0.00");
    }

    private void configurarTabla() {
        colIsbn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("autor"));
        colPrecio.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("stockActual"));
    }

    @FXML
    public void handleBuscar(ActionEvent event) {
        String criterio = txtBusqueda.getText();
        if (criterio != null && !criterio.trim().isEmpty()) {
            tblResultados.getItems().setAll(libroDAO.listarLibros());
        }
    }

    @FXML
    public void handleNuevaVenta(ActionEvent event) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Punto de Venta");
        alerta.setHeaderText(null);
        alerta.setContentText("Módulo de caja listo para procesar una nueva venta.");
        alerta.showAndWait();
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/Login.fxml", "Inicio de Sesión");
    }
}