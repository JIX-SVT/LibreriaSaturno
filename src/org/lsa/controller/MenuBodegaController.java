package org.lsa.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import org.lsa.model.Usuario;
import org.lsa.utils.SesionUsuario;

public class MenuBodegaController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(MenuBodegaController.class.getName());

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private Button btnCerrarSesion;
    @FXML private Circle avatarCircle;

    @FXML private Button btnInventario;
    @FXML private Button btnLibro;
    @FXML private Button btnAutor;
    @FXML private Button btnCategoria;
    @FXML private Button btnEditorial;
    @FXML private Button btnClientes;

    @FXML private VBox cardVerInventario;
    @FXML private VBox cardNuevoLibro;
    @FXML private VBox cardNuevoAutor;
    @FXML private VBox cardNuevaCategoria;
    @FXML private VBox cardNuevaEditorial;
    @FXML private VBox cardNuevoCliente;

    private Usuario usuarioActual;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.info("Inicializando MenuBodegaController...");
        usuarioActual = SesionUsuario.getInstancia().getUsuarioActual();
        
        if (usuarioActual != null) {
            lblBienvenida.setText(usuarioActual.getNombreUsuario());
            String iniciales = usuarioActual.getNombreUsuario()
                    .substring(0, Math.min(2, usuarioActual.getNombreUsuario().length()))
                    .toUpperCase();
            lblRol.setText(iniciales + " · " + capitalize(usuarioActual.getRol()));
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
        LOGGER.info("Cerrando sesión desde el menú de bodega.");
        SesionUsuario.getInstancia().cerrarSesion();
        navegar(evento, "/org/lsa/view/LoginView.fxml", "Librería Saturno - Inicio de Sesión");
    }

    @FXML
    public void irAInventario(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/InventarioView.fxml", "Librería Saturno - Panel Bodega");
    }

    @FXML
    public void irALibro(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/DashboardBodegaView.fxml", "Librería Saturno - Gestión de Libros");
    }

    @FXML
    public void irAAutor(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/AutorView.fxml", "Librería Saturno - Autores");
    }

    @FXML
    public void irACategoria(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/CategoriaView.fxml", "Librería Saturno - Categorías");
    }

    @FXML
    public void irAEditorial(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/EditorialView.fxml", "Librería Saturno - Editoriales");
    }

    @FXML
    public void irAClientes(ActionEvent evento) {
        navegar(evento, "/org/lsa/view/ClienteView.fxml", "Librería Saturno - Clientes");
    }

    @FXML
    public void verInventario(MouseEvent evento) {
        navegarMouseEvent(evento, "/org/lsa/view/InventarioView.fxml", "Librería Saturno - Panel Bodega");
    }

    @FXML
    public void nuevoLibro(MouseEvent evento) {
        navegarMouseEvent(evento, "/org/lsa/view/DashboardBodegaView.fxml", "Librería Saturno - Gestión de Libros");
    }

    @FXML
    public void nuevoAutor(MouseEvent evento) {
        navegarMouseEvent(evento, "/org/lsa/view/AutorView.fxml", "Librería Saturno - Autores");
    }

    @FXML
    public void nuevaCategoria(MouseEvent evento) {
        navegarMouseEvent(evento, "/org/lsa/view/CategoriaView.fxml", "Librería Saturno - Categorías");
    }

    @FXML
    public void nuevaEditorial(MouseEvent evento) {
        navegarMouseEvent(evento, "/org/lsa/view/EditorialView.fxml", "Librería Saturno - Editoriales");
    }

    @FXML
    public void nuevoCliente(MouseEvent evento) {
        navegarMouseEvent(evento, "/org/lsa/view/ClienteView.fxml", "Librería Saturno - Clientes");
    }

    private void navegar(ActionEvent evento, String ruta, String titulo) {
        try {
            Stage escenarioPrincipal = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle(titulo);
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException | NullPointerException e) {
            LOGGER.log(Level.WARNING, "Error al cargar la ruta: " + ruta, e);
            mostrarAlertaConstruccion();
        }
    }

    private void navegarMouseEvent(MouseEvent evento, String ruta, String titulo) {
        try {
            Stage escenarioPrincipal = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            escenarioPrincipal.setTitle(titulo);
            escenarioPrincipal.setScene(scene);
            escenarioPrincipal.show();
        } catch (IOException | NullPointerException e) {
            LOGGER.log(Level.WARNING, "Error al cargar la ruta: " + ruta, e);
            mostrarAlertaConstruccion();
        }
    }

    private void mostrarAlertaConstruccion() {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION,
                "Esta sección estará disponible próximamente.", ButtonType.OK);
        alerta.setTitle("En construcción");
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}