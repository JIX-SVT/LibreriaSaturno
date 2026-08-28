
package org.lsa.controller;

import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.lsa.dao.IMPL.UsuariosDAOIMPL;
import org.lsa.dao.UsuariosDAO;
import org.lsa.model.Usuarios;

public class ListaUsuariosController implements Initializable {
    
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtContraseña;
    @FXML
    private TextField txtconfirmarContraseña;
    @FXML  
    
    @Override 
    public void initialize(URL url, ResourceBundle rb) {
    

    }    
    private final UsuariosDAO usuariosDAO = new UsuariosDAOIMPL();
    private final ObservableList<Usuarios> listaUsuarios = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        configurarTabla();
        cargarTabla();
        seleccionarFila();
    }    
    private void configurarTabla(){
        
    }
}
