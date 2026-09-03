package org.rocka.system;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage escenarioPrincipal;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/LoginView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        escenarioPrincipal.setTitle("Rocka Librería");
        escenarioPrincipal.setScene(scene);
        escenarioPrincipal.show();
        Main.escenarioPrincipal = escenarioPrincipal;     
        cambiarEscena("/org/lsa/view/InicioSesionView.fxml");
    }

    public static void cambiarVista(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
        Parent root = loader.load();
        escenarioPrincipal.setScene(new Scene(root));
    }
    
    public static void cambiarEscena(String rutaFXML) throws IOException {
        //Parent raiz = FXMLLoader.load(getClass().getResource(rutaFXML));
        Parent raiz = FXMLLoader.load(Main.class.getResource(rutaFXML));                
        Scene escena = new Scene(raiz); 
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        escenarioPrincipal.centerOnScreen();
        escenarioPrincipal.show();        
    }
 
   
    public static void main(String[] args) {
        launch(args);
    }

}