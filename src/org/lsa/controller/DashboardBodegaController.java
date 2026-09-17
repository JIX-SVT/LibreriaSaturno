package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.lsa.dao.LibroDAO;
import org.lsa.daoimpl.LibroDAOImpl;
import org.lsa.model.Libro;
import org.lsa.utils.SesionUsuario;

public class DashboardBodegaController implements Initializable {

    private static final Logger log = Logger.getLogger(DashboardBodegaController.class.getName());

    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

    @FXML private TableView<Libro> tblLibros; 
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colNitEditorial;
    @FXML private TableColumn<Libro, Date> colFechaPublicacion;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colIdCategoria, colStock;

    @FXML private TextField txtIsbn, txtTitulo, txtPrecio, txtIdCategoria, txtNitEditorial, txtCantidadMovimiento;
    @FXML private DatePicker dpFechaPublicacion;
    @FXML private Label lblAlertaBajoStock;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Inicializando DashboardBodegaController...");
        configurarTabla();
        cargarLibros();
        verificarAlertasStock();

        // Listener para autocompletar campos desde la tabla seleccionada
        tblLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtIsbn.setText(newSelection.getIsbn());
                txtTitulo.setText(newSelection.getTitulo());
                
                if (newSelection.getFechaPublicacion() != null) {
                    dpFechaPublicacion.setValue(((java.sql.Date) newSelection.getFechaPublicacion()).toLocalDate());
                } else {
                    dpFechaPublicacion.setValue(null);
                }
                
                txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                txtIdCategoria.setText(String.valueOf(newSelection.getIdCategoria()));
                txtNitEditorial.setText(newSelection.getNitEditorial());
            }
        });
    }

    private void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaPublicacion.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNitEditorial.setCellValueFactory(new PropertyValueFactory<>("nitEditorial"));
        if (colStock != null) {
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        }
        tblLibros.setItems(listaLibros);
    }

    private void cargarLibros() {
        listaLibros.clear();
        List<Libro> libros = libroDAO.listar();        
        if (libros != null) {
            listaLibros.addAll(libros);
        }
    }

    private void verificarAlertasStock() {
        List<Libro> librosCriticos = libroDAO.obtenerLibrosStockCritico();

        if (librosCriticos != null && !librosCriticos.isEmpty()) {
            if (lblAlertaBajoStock != null) {
                lblAlertaBajoStock.setText("⚠️ Alerta: " + librosCriticos.size() + " libro(s) en stock crítico");
                lblAlertaBajoStock.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-padding: 5px; -fx-background-radius: 4px; -fx-font-weight: bold;");
                lblAlertaBajoStock.setVisible(true);
            }

            StringBuilder detalle = new StringBuilder("Los siguientes libros tienen 10 o menos unidades disponibles:\n\n");
            for (Libro libro : librosCriticos) {
                detalle.append("• ").append(libro.getTitulo())
                       .append(" (ISBN: ").append(libro.getIsbn()).append(") ")
                       .append("- Stock: ").append(libro.getStock()).append("\n");
            }

            mostrarAlerta("⚠️ Alerta de Stock Crítico", detalle.toString(), Alert.AlertType.WARNING);
        } else {
            if (lblAlertaBajoStock != null) {
                lblAlertaBajoStock.setVisible(false);
            }
        }
    }

   @FXML
public void handleGuardarLibro(ActionEvent event) {
    log.info("Ejecutando proceso de guardado de libro.");

    if (txtIsbn.getText().trim().isEmpty() || txtTitulo.getText().trim().isEmpty() ||
        dpFechaPublicacion.getValue() == null || txtPrecio.getText().trim().isEmpty() ||
        txtIdCategoria.getText().trim().isEmpty() || txtNitEditorial.getText().trim().isEmpty()) {
        mostrarAlerta("Campos Incompletos", "Por favor complete todos los campos requeridos.", Alert.AlertType.WARNING);
        return;
    }

    try {
        String isbn = txtIsbn.getText().trim();
        String titulo = txtTitulo.getText().trim();
        Date fecha = Date.valueOf(dpFechaPublicacion.getValue());
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        int idCategoria = Integer.parseInt(txtIdCategoria.getText().trim());
        String nitEditorial = txtNitEditorial.getText().trim();

        Libro libro = new Libro(isbn, titulo, fecha, precio, idCategoria, nitEditorial, 0);

        boolean exito;
        Libro libroExistente = libroDAO.buscarPorIsbn(isbn);
        if (libroExistente != null) {
            exito = libroDAO.actualizar(libro);
        } else {
            exito = libroDAO.agregar(libro);
        }

        if (exito) {
            mostrarAlerta("Éxito", "Libro guardado correctamente en la base de datos.", Alert.AlertType.INFORMATION);
            limpiarCampos();
            cargarLibros();
            verificarAlertasStock();
        } else {
            mostrarAlerta("Error", "No se pudo guardar el libro. Verifique que la Categoría y el NIT de la Editorial existan.", Alert.AlertType.ERROR);
        }
    } catch (NumberFormatException e) {
        mostrarAlerta("Error de Formato", "Asegúrese de ingresar números válidos para Precio e ID Categoría.", Alert.AlertType.ERROR);
    } catch (Exception e) {
        log.log(Level.SEVERE, "Error al guardar el libro", e);
        mostrarAlerta("Error de Referencia", "El NIT de la Editorial o el ID de la Categoría no existen en la base de datos.", Alert.AlertType.ERROR);
    }
}

    @FXML
    public void handleRegistrarIngreso(ActionEvent event) {
        log.info("Registrando ingreso de inventario.");
        procesarMovimientoInventario(true);
    }

    @FXML
    public void handleRegistrarSalida(ActionEvent event) {
        log.info("Registrando salida de inventario.");
        procesarMovimientoInventario(false);
    }

    private void procesarMovimientoInventario(boolean esIngreso) {
        String isbn = txtIsbn.getText().trim();
        String cantStr = txtCantidadMovimiento.getText().trim();

        if (isbn.isEmpty() || cantStr.isEmpty()) {
            mostrarAlerta("Advertencia", "Seleccione un libro e ingrese una cantidad válida.", Alert.AlertType.WARNING);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantStr);
            if (cantidad <= 0) {
                mostrarAlerta("Advertencia", "La cantidad debe ser mayor a cero.", Alert.AlertType.WARNING);
                return;
            }

            if (!esIngreso) {
                cantidad = -cantidad;
            }

            boolean exito = libroDAO.actualizarStock(isbn, cantidad);
            if (exito) {
                mostrarAlerta("Éxito", "Stock actualizado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
                cargarLibros();
                verificarAlertasStock();
            } else {
                mostrarAlerta("Error", "No se pudo actualizar el stock en la base de datos.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Ingrese una cantidad entera válida.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleLimpiarCampos(ActionEvent event) {
        log.info("Solicitud de limpieza manual de campos del formulario de bodega.");
        limpiarCampos();
    }

    @FXML
    public void handleVolverMenu(ActionEvent event) {
        log.info("Navegando de regreso al menú principal.");
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/DashboardMenuView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Menú");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al intentar cargar la vista del menú principal", e);
            mostrarAlerta("Error", "No se pudo cargar la vista del menú.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        log.info("Cerrando sesión del usuario de bodega.");
        SesionUsuario.getInstancia().cerrarSesion();
        try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno - Inicio de Sesión");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al intentar redirigir a la pantalla de Inicio de Sesión", e);
            mostrarAlerta("Error", "No se pudo regresar a la pantalla de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        log.info("Limpiando los campos de entrada de datos.");
        txtIsbn.clear();
        txtTitulo.clear();
        dpFechaPublicacion.setValue(null);
        txtPrecio.clear();
        txtIdCategoria.clear();
        txtNitEditorial.clear();
        txtCantidadMovimiento.clear();
        tblLibros.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}