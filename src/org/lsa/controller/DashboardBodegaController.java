package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import org.lsa.model.Autor;
import org.lsa.model.Categoria;
import org.lsa.model.Editorial;
import org.lsa.model.Libro;
import org.lsa.utils.SesionUsuario;

public class DashboardBodegaController implements Initializable {

    private static final Logger log = Logger.getLogger(DashboardBodegaController.class.getName());

    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();

    @FXML private TableView<Libro> tblLibros; 
    @FXML private TableColumn<Libro, String> colIsbn, colTitulo, colNitEditorial, colAutor;
    @FXML private TableColumn<Libro, Date> colFechaPublicacion;
    @FXML private TableColumn<Libro, Double> colPrecio;
    @FXML private TableColumn<Libro, Integer> colIdCategoria, colStock;

    @FXML private TextField txtBusqueda;
    @FXML private TextField txtIsbn, txtTitulo, txtPrecio, txtCantidadMovimiento;
    
    @FXML private ComboBox<Autor> cmbAutor;
    @FXML private ComboBox<Categoria> cmbCategoria;
    @FXML private ComboBox<Editorial> cmbEditorial;
    
    @FXML private DatePicker dpFechaPublicacion;
    @FXML private Label lblAlertaBajoStock;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        log.info("Inicializando DashboardBodegaController...");
        configurarTabla();
        cargarOpcionesCombos();
        cargarLibros();
        configurarBusqueda();
        verificarAlertasStock();

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

                // Seleccionar Autor
                if (cmbAutor != null) {
                    cmbAutor.getSelectionModel().clearSelection();
                    for (Autor aut : cmbAutor.getItems()) {
                        if ((newSelection.getIdAutor() != 0 && aut.getIdAutor() == newSelection.getIdAutor()) ||
                            (newSelection.getAutor() != null && aut.toString().toLowerCase().contains(newSelection.getAutor().toLowerCase()))) {
                            cmbAutor.setValue(aut);
                            break;
                        }
                    }
                }

                // Seleccionar Categoría
                if (cmbCategoria != null) {
                    for (Categoria cat : cmbCategoria.getItems()) {
                        if (cat.getIdCategoria() == newSelection.getIdCategoria()) {
                            cmbCategoria.setValue(cat);
                            break;
                        }
                    }
                }

                // Seleccionar Editorial
                if (cmbEditorial != null) {
                    for (Editorial ed : cmbEditorial.getItems()) {
                        if (ed.getNit() != null && ed.getNit().equalsIgnoreCase(newSelection.getNitEditorial())) {
                            cmbEditorial.setValue(ed);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colFechaPublicacion.setCellValueFactory(new PropertyValueFactory<>("fechaPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        
        // Muestra la representación en texto del autor si está presente
        if (colAutor != null) {
            colAutor.setCellValueFactory(cellData -> {
                String nombreAutor = cellData.getValue().getAutor();
                if (nombreAutor != null && !nombreAutor.trim().isEmpty()) {
                    return new SimpleStringProperty(nombreAutor);
                }
                return new SimpleStringProperty(String.valueOf(cellData.getValue().getIdAutor()));
            });
        }
        
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<>("idCategoria"));
        colNitEditorial.setCellValueFactory(new PropertyValueFactory<>("nitEditorial"));
        
        if (colStock != null) {
            colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        }
    }

    private void cargarOpcionesCombos() {
        List<Autor> autoresBD = libroDAO.listarAutores();
        if (cmbAutor != null && autoresBD != null) {
            cmbAutor.setItems(FXCollections.observableArrayList(autoresBD));
        }

        List<Categoria> categoriasBD = libroDAO.listarCategorias();
        if (cmbCategoria != null && categoriasBD != null) {
            cmbCategoria.setItems(FXCollections.observableArrayList(categoriasBD));
        }

        List<Editorial> editorialesBD = libroDAO.listarEditoriales();
        if (cmbEditorial != null && editorialesBD != null) {
            cmbEditorial.setItems(FXCollections.observableArrayList(editorialesBD));
        }
    }

    private void cargarLibros() {
        listaLibros.clear();
        List<Libro> libros = libroDAO.listar();        
        if (libros != null) {
            listaLibros.addAll(libros);
        }
    }

    private void configurarBusqueda() {
        FilteredList<Libro> filteredData = new FilteredList<>(listaLibros, p -> true);

        txtBusqueda.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(libro -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase().trim();

                if (libro.getTitulo() != null && libro.getTitulo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (libro.getIsbn() != null && libro.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Libro> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblLibros.comparatorProperty());
        tblLibros.setItems(sortedData);
    }

    private void verificarAlertasStock() {
        List<Libro> librosCriticos = libroDAO.obtenerLibrosStockCritico();

        if (librosCriticos != null && !librosCriticos.isEmpty()) {
            if (lblAlertaBajoStock != null) {
                lblAlertaBajoStock.setText(" Alerta: " + librosCriticos.size() + " libro(s) en stock crítico");
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
            cmbAutor.getValue() == null || cmbCategoria.getValue() == null || cmbEditorial.getValue() == null) {
            mostrarAlerta("Campos Incompletos", "Por favor complete todos los campos requeridos (ISBN, Título, Fecha, Precio, Autor, Categoría y Editorial).", Alert.AlertType.WARNING);
            return;
        }

        try {
            String isbn = txtIsbn.getText().trim();
            String titulo = txtTitulo.getText().trim();
            Date fecha = Date.valueOf(dpFechaPublicacion.getValue());
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            
            int idAutor = cmbAutor.getValue().getIdAutor();
            String nombreAutor = cmbAutor.getValue().toString();
            int idCategoria = cmbCategoria.getValue().getIdCategoria();
            String nitEditorial = cmbEditorial.getValue().getNit();

            Libro libro = new Libro(isbn, titulo, fecha, precio, idAutor, nombreAutor, idCategoria, nitEditorial, 0);

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
                mostrarAlerta("Error", "No se pudo guardar el libro en la base de datos.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Formato", "Asegúrese de ingresar un precio numérico válido.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error al guardar el libro", e);
            mostrarAlerta("Error", "Ocurrió un error inesperado al procesar la solicitud.", Alert.AlertType.ERROR);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/MenuBodegaView.fxml"));
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
        if (cmbAutor != null) {
            cmbAutor.getSelectionModel().clearSelection();
        }
        if (cmbCategoria != null) {
            cmbCategoria.getSelectionModel().clearSelection();
        }
        if (cmbEditorial != null) {
            cmbEditorial.getSelectionModel().clearSelection();
        }
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