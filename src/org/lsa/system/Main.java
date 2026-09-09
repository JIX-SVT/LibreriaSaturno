package org.lsa.system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) {
        try {
            
            Parent root = FXMLLoader.load(getClass().getResource("/org/lsa/view/LoginView.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setTitle("Librería Saturno - Módulo de Gestión de Ventas y Facturación");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

            LOGGER.info("Aplicación iniciada mostrando el módulo Maestro-Detalle de Ventas.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista principal de ventas", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}