package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.lsa.dao.IMPL.UsuariosDAOIMPL;
import org.lsa.model.Usuarios;

public class ListaUsuariosController implements Initializable {

@FXML private TableView<Usuarios> tblUsuarios;
    @FXML private TableColumn<Usuarios, Integer> colId;
    @FXML private TableColumn<Usuarios, String> colNombre;
    @FXML private TableColumn<Usuarios, String> colUsuario;
    @FXML private TableColumn<Usuarios, String> colRol;
    @FXML private TableColumn<Usuarios, String> colEstado;

    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblUsuariosActivos;
    @FXML private Label lblUsuariosInactivos;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cmbFiltroRol;
    @FXML private ComboBox<String> cmbFiltroEstado;
    @FXML private Button btnAplicarFiltro;
    @FXML private Button btnNuevoUsuario;
    @FXML private Button btnEditarUsuario;
    @FXML private Button btnDesactivarUsuario;
    
    private final UsuariosDAOIMPL usuarioDAO = new UsuariosDAOIMPL();
    private ObservableList<Usuarios> listaUsuariosMaster;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarFiltros();
        cargarDatosTabla();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }
    
    private void configurarFiltros() {
        if (cmbFiltroRol != null) {
            cmbFiltroRol.getItems().addAll("Todos los Roles", "Admin", "Cajero", "Bodega");
            cmbFiltroRol.getSelectionModel().select("Todos los Roles");
        }
        if (cmbFiltroEstado != null) {
            cmbFiltroEstado.getItems().addAll("Todos los Estados", "Activo", "Inactivo");
            cmbFiltroEstado.getSelectionModel().select("Todos los Estados");
        }
    }
    public void cargarDatosTabla() {
        List<Usuarios> usuariosObtenidos = usuarioDAO.listar();
        listaUsuariosMaster = FXCollections.observableArrayList(usuariosObtenidos);
        tblUsuarios.setItems(listaUsuariosMaster);
        actualizarMetricas();
    }

    private void actualizarMetricas() {
        if (listaUsuariosMaster == null) return;
        long total = listaUsuariosMaster.size();
        long activos = listaUsuariosMaster.stream()
                .filter(u -> "Activo".equalsIgnoreCase(u.getEstado()))
                .count();
        long inactivos = total - activos;

        if (lblTotalUsuarios != null) lblTotalUsuarios.setText(String.valueOf(total));
        if (lblUsuariosActivos != null) lblUsuariosActivos.setText(String.valueOf(activos));
        if (lblUsuariosInactivos != null) lblUsuariosInactivos.setText(String.valueOf(inactivos));
    }
    @FXML
    private void handleAplicarFiltros() {
        if (listaUsuariosMaster == null) return;

        String busquedaTexto = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase().trim() : "";
        String rolSeleccionado = cmbFiltroRol.getValue();
        String estadoSeleccionado = cmbFiltroEstado.getValue();

        List<Usuarios> listaFiltrada = listaUsuariosMaster.stream().filter(usuario -> {
            boolean coincideTexto = busquedaTexto.isEmpty()
                    || (usuario.getNombre() != null && usuario.getNombre().toLowerCase().contains(busquedaTexto))
                    || (usuario.getUsuario() != null && usuario.getUsuario().toLowerCase().contains(busquedaTexto))
                    || (usuario.getApellido() != null && usuario.getApellido().toLowerCase().contains(busquedaTexto));

            boolean coincideRol = rolSeleccionado == null 
                    || "Todos los Roles".equals(rolSeleccionado) 
                    || rolSeleccionado.equalsIgnoreCase(usuario.getRol());

            boolean coincideEstado = estadoSeleccionado == null 
                    || "Todos los Estados".equals(estadoSeleccionado) 
                    || estadoSeleccionado.equalsIgnoreCase(usuario.getEstado());

            return coincideTexto && coincideRol && coincideEstado;
        }).collect(Collectors.toList());

        tblUsuarios.setItems(FXCollections.observableArrayList(listaFiltrada));
    }
    @FXML
    private void handleNuevoUsuario() {
        abrirFormulario(null);
    }
    @FXML
    private void handleEditarUsuario() {
        Usuarios usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            abrirFormulario(usuarioSeleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un usuario de la tabla para editar.");
        }
    }
    @FXML
    private void handleDesactivarUsuario() {
        Usuarios usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un usuario para desactivar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea desactivar al usuario " + usuarioSeleccionado.getUsuario() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            usuarioSeleccionado.setEstado("Inactivo");
            boolean exito = usuarioDAO.actualizar(usuarioSeleccionado);
            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario desactivado correctamente.");
                cargarDatosTabla();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo desactivar el usuario.");
            }
        }
    }
    private void abrirFormulario(Usuarios usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/FormularioUsuarioView.fxml"));
            Parent root = loader.load();

            FormularioUsuarioController controller = loader.getController();
            controller.setListaController(this);
            if (usuario != null) {
                controller.cargarDatos(usuario);
            }
            Stage stage = new Stage();
            stage.setTitle(usuario == null ? "Nuevo Usuario" : "Editar Usuario");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error de interfaz", "No se pudo cargar el formulario.");
        }
    }
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}