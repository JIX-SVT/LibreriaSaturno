package org.lsa.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lsa.utils.ConexionSingleton;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ConexionSingleton miConexion = ConexionSingleton.getInstancia();
            miConexion.conectar();
            System.out.println("Bienvenido a Librería Saturno");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Librería Saturno - Gestión de Usuarios");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}