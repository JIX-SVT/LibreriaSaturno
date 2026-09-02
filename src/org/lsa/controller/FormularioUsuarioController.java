package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.lsa.dao.UsuarioDAO;
import org.lsa.daoimpl.UsuarioDAOImpl;
import org.lsa.model.Usuario;

public class FormularioUsuarioController implements Initializable {

    @FXML private TextField txtID;
    @FXML private TextField txtNombre;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtApellido;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;
    @FXML private ComboBox<String> cbxRol;
    @FXML private ComboBox<String> cbxEstado;
    @FXML private Label lblFechaCreacion;
    @FXML private Label lblFechaActualizacion;

    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private ListaUsuariosController listaController;
    private Usuario usuarioEdicion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbxRol.getItems().addAll("Admin", "Cajero", "Bodega");
        cbxEstado.getItems().addAll("Activo", "Inactivo");
        cbxEstado.getSelectionModel().select("Activo");
    }

    public void setListaController(ListaUsuariosController controller) {
        this.listaController = controller;
    }

    public void cargarDatos(Usuario usuario) {
        this.usuarioEdicion = usuario;
        
        if (txtID != null) {
            txtID.setText(usuario.getIdUsuario() > 0 ? String.valueOf(usuario.getIdUsuario()) : "");
        }
        
        txtNombre.setText(usuario.getNombreUsuario() != null ? usuario.getNombreUsuario() : "");
        
        if (txtCorreo != null) {
            txtCorreo.setText(usuario.getCorreo() != null ? usuario.getCorreo() : "");
        }

        txtPassword.setText(""); 
        cbxRol.setValue(usuario.getRol());
        cbxEstado.setValue(usuario.isActivo() ? "Activo" : "Inactivo");
    }

    @FXML
    private void handleGuardar() {
        if (!validarCampos()) {
            return;
        }

        if (usuarioEdicion == null) {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombreUsuario(txtNombre.getText().trim());
            if (txtCorreo != null) nuevoUsuario.setCorreo(txtCorreo.getText().trim());
            nuevoUsuario.setContrasena(txtPassword.getText());
            nuevoUsuario.setRol(cbxRol.getValue());
            nuevoUsuario.setActivo("Activo".equalsIgnoreCase(cbxEstado.getValue()));

            if (usuarioDAO.insertar(nuevoUsuario)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario creado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo registrar el usuario.");
            }
        } else {
            usuarioEdicion.setNombreUsuario(txtNombre.getText().trim());
            if (txtCorreo != null) usuarioEdicion.setCorreo(txtCorreo.getText().trim());
            
            if (!txtPassword.getText().isEmpty()) {
                usuarioEdicion.setContrasena(txtPassword.getText());
            }
            usuarioEdicion.setRol(cbxRol.getValue());
            usuarioEdicion.setActivo("Activo".equalsIgnoreCase(cbxEstado.getValue()));

            if (usuarioDAO.actualizar(usuarioEdicion)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Usuario actualizado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el usuario.");
            }
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtNombre.getText() == null || txtNombre.getText().trim().isEmpty()) {
            errores.append("- El nombre de usuario es obligatorio.\n");
        }
        if (usuarioEdicion == null) {
            if (txtPassword.getText() == null || txtPassword.getText().length() < 6) {
                errores.append("- La contraseña debe tener al menos 6 caracteres.\n");
            }
        } else {
            if (!txtPassword.getText().isEmpty() && txtPassword.getText().length() < 6) {
                errores.append("- Si modifica la contraseña, debe tener al menos 6 caracteres.\n");
            }
        }
        if (cbxRol.getValue() == null) {
            errores.append("- Debe seleccionar un rol.\n");
        }
        if (cbxEstado.getValue() == null) {
            errores.append("- Debe seleccionar un estado.\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos inválidos", errores.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancelar() {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        if (listaController != null) {
            listaController.cargarDatosTabla();
        }
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}