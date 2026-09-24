package org.lsa.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.lsa.model.Usuario;
import org.lsa.utils.Navegador;
import org.lsa.utils.SesionUsuario;

public class DashboardAdminController implements Initializable {

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private Button btnCerrarSesion;
    @FXML private Circle avatarCircle;

    @FXML private Button btnUsuario;
    @FXML private Button btnLibro;
    @FXML private Button btnAutor;
    @FXML private Button btnCategoria;
    @FXML private Button btnEditorial;
    @FXML private Button btnVentas;
    @FXML private Button btnAutorLibro;
    @FXML private Button btnDetalleVenta;

    @FXML private VBox cardNuevoLibro;
    @FXML private VBox cardAgregarVenta;
    @FXML private VBox cardVerInventario;
    @FXML private VBox cardGestionarUsuarios;
    @FXML private VBox cardReportes;
    @FXML private VBox cardConfiguracion;

    private Usuario usuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (SesionUsuario.getInstancia() != null) {
            usuarioActual = SesionUsuario.getInstancia().getUsuarioActual();
        }

        actualizarInformacionUsuario(usuarioActual);
    }

    private void actualizarInformacionUsuario(Usuario usuario) {
        if (usuario != null && usuario.getNombreUsuario() != null) {
            lblBienvenida.setText(usuario.getNombreUsuario());
            String iniciales = usuario.getNombreUsuario()
                    .substring(0, Math.min(2, usuario.getNombreUsuario().length()))
                    .toUpperCase();
            String rolTexto = usuario.getRol() != null ? usuario.getRol() : "Usuario";
            lblRol.setText(iniciales + " · " + capitalize(rolTexto));
        } else {
            lblBienvenida.setText("Invitado");
            lblRol.setText("?? · Sin sesión");
        }
    }

    private String capitalize(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    @FXML
    public void cerrarSesion(ActionEvent evento) {
        SesionUsuario.getInstancia().cerrarSesion();
        Stage stage = (Stage) ((Node) evento.getSource()).getScene().getWindow();
        Navegador.cargarVista(stage, "/org/lsa/view/LoginView.fxml", "Inicio de Sesión");
    }

    @FXML
    public void irAUsuario(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/ListaUsuariosView.fxml", "Gestión de Usuarios");
    }

    @FXML
    public void irALibro(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/InventarioView.fxml", "Gestión de Libros");
    }

    @FXML
    public void irAAutor(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/AutorView.fxml", "Gestión de Autores");
    }

    @FXML
    public void irACategoria(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/CategoriaView.fxml", "Gestión de Categorías");
    }

    @FXML
    public void irAEditorial(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/EditorialView.fxml", "Gestión de Editoriales");
    }

    @FXML
    public void irAVentas(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/ListaVentasView.fxml", "Lista de Ventas");
    }

    @FXML
    public void irAAutorLibro(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/AutorView.fxml", "Autores y Libros");
    }

    @FXML
    public void irADetalleVenta(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/DetalleVentaview.fxml", "Detalle de Venta");
    }

    @FXML
    public void irAClientes(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/ClienteView.fxml", "Gestión de Clientes");
    }

    @FXML
    public void nuevoLibro(MouseEvent evento) {
        navegarCard(evento, "/org/lsa/view/InventarioView.fxml", "Nuevo Libro");
    }

    @FXML
    public void agregarVenta(MouseEvent evento) {
        navegarCard(evento, "/org/lsa/view/NuevaVentaView.fxml", "Nueva Venta");
    }

    @FXML
    public void verInventario(MouseEvent evento) {
        navegarCard(evento, "/org/lsa/view/InventarioView.fxml", "Inventario");
    }

    @FXML
    public void gestionarUsuarios(MouseEvent evento) {
        navegarCard(evento, "/org/lsa/view/ListaUsuariosView.fxml", "Gestión de Usuarios");
    }

    @FXML
    public void reportes(MouseEvent evento) {
        navegarCard(evento, "/org/lsa/view/ResumenDelDiaView.fxml", "Reportes y Facturas");
    }

    @FXML
    public void configuracion(MouseEvent evento) {
        navegarCard(evento, "/org/lsa/view/CambioContrasenaView.fxml", "Configuración");
    }

    private void navegar(ActionEvent evento, String fxmlPath, String titulo) {
        try {
            Stage stage = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            Navegador.cargarVista(stage, fxmlPath, titulo);
        } catch (Exception e) {
            mostrarAlertaEnConstruccion();
        }
    }

    private void navegarCard(MouseEvent evento, String fxmlPath, String titulo) {
        try {
            Stage stage = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            Navegador.cargarVista(stage, fxmlPath, titulo);
        } catch (Exception e) {
            mostrarAlertaEnConstruccion();
        }
    }

    private void mostrarAlertaEnConstruccion() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION,
                "Esta sección estará disponible próximamente.", ButtonType.OK);
        alerta.setTitle("En construcción");
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    public void iniciarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        actualizarInformacionUsuario(usuario);
    }
}