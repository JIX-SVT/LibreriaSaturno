package org.lsa.controller;
 
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.lsa.daoimpl.UsuarioDAOImpl;
import org.lsa.model.Usuario;
 
public class ListaUsuariosController implements Initializable {
 
    @FXML private TextField txtID;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContraseña;
 
    @FXML private ComboBox<String> cmbRol;
    @FXML private ComboBox<String> cmbEstado;
 
    @FXML private TableView<Usuario> tblUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colUsuario;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colCorreo;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, Object> colEstado;
 
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();
    private ObservableList<Usuario> listaUsuarios;
    private Usuario usuarioSeleccionado;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla();
        configurarSeleccionTabla();
    }
 
private void configurarTabla() {
    colId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
    colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
    colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    cmbRol.setItems(FXCollections.observableArrayList("Admin", "Cajero", "Empleado"));
    cmbEstado.setItems(FXCollections.observableArrayList("Activo", "Inactivo"));
}
 
    private void configurarSeleccionTabla() {
        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usuarioSeleccionado = newSelection;
                cargarDatos(usuarioSeleccionado);
            }
        });
    }
 
    @FXML
    public void cargarDatosTabla() {
        List<Usuario> usuariosObtenidos = usuarioDAO.listarTodos();
        listaUsuarios = FXCollections.observableArrayList(usuariosObtenidos);
        tblUsuarios.setItems(listaUsuarios);
    }
 
    public void cargarDatos(Usuario usuario) {
        if (txtID != null) {
            txtID.setText(String.valueOf(usuario.getIdUsuario()));
        }
        txtUsuario.setText(usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "");
        txtNombre.setText(usuario.getNombre() != null ? usuario.getNombre() : "");
        txtApellido.setText(usuario.getApellido() != null ? usuario.getApellido() : "");
        if (txtCorreo != null) {
            txtCorreo.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
        }
        if (usuario.getRol() != null) {
            cmbRol.setValue(usuario.getRol());
        }
        if (usuario.isEstado()) {
            cmbEstado.setValue("Activo");
        } else {
            cmbEstado.setValue("Inactivo");
        }
        txtContraseña.clear();
    }
 
    @FXML
    private void handleGuardarUsuario() {
        boolean EstadoActivo = "Activo".equalsIgnoreCase(cmbEstado.getValue());
        if (usuarioSeleccionado == null) {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(txtUsuario.getText().trim());
            nuevoUsuario.setNombre(txtNombre.getText().trim());
            nuevoUsuario.setApellido(txtApellido.getText().trim());
            nuevoUsuario.setCorreo(txtCorreo.getText().trim());
            nuevoUsuario.setContraseña(txtContraseña.getText());
            nuevoUsuario.setRol(cmbRol.getValue());
            nuevoUsuario.setEstado(EstadoActivo);
            if (usuarioDAO.insertar(nuevoUsuario)) {
                mostrarAlerta("Usuario creado correctamente.");
                handleLimpiarUsuario();
                cargarDatosTabla();
            } else {
                mostrarError("No se pudo registrar el usuario.");
            }
        } else {
            usuarioSeleccionado.setNombreUsuario(txtUsuario.getText().trim());
            usuarioSeleccionado.setNombre(txtNombre.getText().trim());
            usuarioSeleccionado.setApellido(txtApellido.getText().trim());
            usuarioSeleccionado.setCorreo(txtCorreo.getText().trim());
            usuarioSeleccionado.setRol(cmbRol.getValue());
            usuarioSeleccionado.setEstado(EstadoActivo);
            if (!txtContraseña.getText().isEmpty()) {
                usuarioSeleccionado.setContraseña(txtContraseña.getText());
            }
 
            if (usuarioDAO.actualizar(usuarioSeleccionado)) {
                mostrarAlerta("Usuario actualizado correctamente.");
                handleLimpiarUsuario();
                cargarDatosTabla();
            } else {
                mostrarError("No se pudo actualizar el usuario.");
            }
        }
    }
        @FXML
    private void handleEditarUsuario() {
        boolean EstadoActivo = "Activo".equalsIgnoreCase(cmbEstado.getValue());
            usuarioSeleccionado.setNombreUsuario(txtUsuario.getText().trim());
            usuarioSeleccionado.setNombre(txtNombre.getText().trim());
            usuarioSeleccionado.setApellido(txtApellido.getText().trim());
            usuarioSeleccionado.setCorreo(txtCorreo.getText().trim());
            usuarioSeleccionado.setRol(cmbRol.getValue());
            usuarioSeleccionado.setEstado(EstadoActivo);
            if (!txtContraseña.getText().isEmpty()) {
                usuarioSeleccionado.setContraseña(txtContraseña.getText());
            }
            if (usuarioDAO.actualizar(usuarioSeleccionado)) {
                mostrarAlerta("Usuario actualizado correctamente.");
                handleLimpiarUsuario();
                cargarDatosTabla();
            } else {
                mostrarError("No se pudo actualizar el usuario.");
            }
        }
 
    @FXML
    private void handleLimpiarUsuario() {
        if (txtID != null) txtID.clear();
        txtUsuario.clear();
        txtNombre.clear();
        txtApellido.clear();
        if (txtCorreo != null) txtCorreo.clear();
        txtContraseña.clear();
        cmbRol.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
 
        usuarioSeleccionado = null;
        tblUsuarios.getSelectionModel().clearSelection();
    }
 
    @FXML
    private void handleDesactivarUsuario() {
        if (usuarioSeleccionado == null) {
            mostrarError("Por favor, seleccione un usuario de la tabla para desactivar.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Está seguro de desactivar al usuario " + usuarioSeleccionado.getNombreUsuario() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
 
        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            usuarioSeleccionado.setEstado(false);
            if (usuarioDAO.actualizar(usuarioSeleccionado)) {
                mostrarAlerta("Usuario desactivado correctamente.");
                handleLimpiarUsuario();
                cargarDatosTabla();
            } else {
                mostrarError("No se pudo desactivar el usuario.");
            }
        }
    }
 
    @FXML
    private void handleVolverMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/lsa/view/DashboardAdminView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo regresar al menú principal.");
        }
    }
 
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
 
        if (txtUsuario.getText() == null || txtUsuario.getText().trim().isEmpty()) {
            errores.append("- El nombre de usuario es obligatorio.\n");
        }
        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio.\n");
        }
        if (txtApellido.getText() == null || txtApellido.getText().trim().isEmpty()) {
            errores.append("- El apellido es obligatorio.\n");
        }
        if (cmbRol.getValue() == null) {
            errores.append("- Debe seleccionar un Rol.\n");
        }
        if (cmbEstado.getValue() == null) {
            errores.append("- Debe seleccionar un Estado.\n");
        }
 
        if (usuarioSeleccionado == null) {
            if (txtContraseña.getText() == null || txtContraseña.getText().length() < 6) {
                errores.append("- La contraseña debe tener al menos 6 caracteres.\n");
            }
        } else {
            if (!txtContraseña.getText().isEmpty() && txtContraseña.getText().length() < 6) {
                errores.append("- Si modifica la contraseña, debe tener al menos 6 caracteres.\n");
            }
        }
 
        if (errores.length() > 0) {
            mostrarError(errores.toString());
            return false;
        }
        return true;
    }
    public void handleCambiarContraseña(ActionEvent event) {
         try {
            Stage escenarioPrincipal = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/CambioContraseñaView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle("Librería Saturno");
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la vista del menú.", ButtonType.OK);
            alerta.showAndWait();
        }
    }
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
 
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}