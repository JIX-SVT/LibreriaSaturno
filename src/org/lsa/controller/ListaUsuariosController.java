package org.lsa.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.lsa.dao.UsuarioDAO;
import org.lsa.daoimpl.UsuarioDAOImpl;
import org.lsa.model.Usuario;
import org.lsa.utils.Navegador;

public class ListaUsuariosController implements Initializable {

    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colEstado;

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
    
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private ObservableList<Usuario> listaUsuariosMaster;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        configurarFiltros();
        cargarDatosTabla();
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colEstado.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isActivo() ? "Activo" : "Inactivo")
        );
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
        List<Usuario> usuariosObtenidos = usuarioDAO.listarTodos();
        listaUsuariosMaster = FXCollections.observableArrayList(usuariosObtenidos);
        tblUsuarios.setItems(listaUsuariosMaster);
        actualizarMetricas();
    }

    private void actualizarMetricas() {
        if (listaUsuariosMaster == null) return;
        long total = listaUsuariosMaster.size();
        long activos = listaUsuariosMaster.stream()
                .filter(Usuario::isActivo)
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

        List<Usuario> listaFiltrada = listaUsuariosMaster.stream().filter(usuario -> {
            boolean coincideTexto = busquedaTexto.isEmpty()
                    || (usuario.getNombreUsuario() != null && usuario.getNombreUsuario().toLowerCase().contains(busquedaTexto))
                    || (usuario.getCorreo() != null && usuario.getCorreo().toLowerCase().contains(busquedaTexto));

            boolean coincideRol = rolSeleccionado == null 
                    || "Todos los Roles".equals(rolSeleccionado) 
                    || rolSeleccionado.equalsIgnoreCase(usuario.getRol());

            String estadoUsuario = usuario.isActivo() ? "Activo" : "Inactivo";
            boolean coincideEstado = estadoSeleccionado == null 
                    || "Todos los Estados".equals(estadoSeleccionado) 
                    || estadoSeleccionado.equalsIgnoreCase(estadoUsuario);

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
        Usuario usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            abrirFormulario(usuarioSeleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un usuario de la tabla para editar.");
        }
    }

    @FXML
    private void handleDesactivarUsuario() {
        Usuario usuarioSeleccionado = tblUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Por favor, seleccione un usuario para desactivar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de que desea desactivar al usuario " + usuarioSeleccionado.getNombreUsuario() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            usuarioSeleccionado.setActivo(false);
            boolean exito = usuarioDAO.actualizar(usuarioSeleccionado);
            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario desactivado correctamente.");
                cargarDatosTabla();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo desactivar el usuario.");
            }
        }
    }

    private void abrirFormulario(Usuario usuario) {
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