package org.lsa.system;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.lsa.utils.Conexion;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws SQLException {
        try {
            Conexion miConexion = Conexion.getInstancia();
            miConexion.conectar();
            System.out.println("Bienvenido a Librería Saturno");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/lsa/view/LoginView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setTitle("Librería Saturno");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Error al cargar la vista FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}