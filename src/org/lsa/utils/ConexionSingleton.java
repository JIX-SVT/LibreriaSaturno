package org.lsa.utils;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionSingleton {

    private static ConexionSingleton instancia;
    private Connection conexion;

    private ConexionSingleton() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getResourceAsStream("/sql.properties")) {
            if (input == null) {
                System.err.println("No se pudo encontrar el archivo sql.properties en el classpath.");
                return;
            }
            properties.load(input);

            String url = properties.getProperty("db.url");
            String usuario = properties.getProperty("db.user");
            String clave = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

            Class.forName(driver);
            this.conexion = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conexión inicializada con éxito desde sql.properties.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo sql.properties: " + e.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la Base de Datos: " + e.getMessage());
        }
    }

    public static synchronized ConexionSingleton getInstance() {
        try {
            if (instancia == null || instancia.getConexion() == null || instancia.getConexion().isClosed()) {
                instancia = new ConexionSingleton();
            }
        } catch (SQLException e) {
            instancia = new ConexionSingleton();
        }
        return instancia;
    }

    public static synchronized ConexionSingleton getInstancia() {
        return getInstance();
    }

    public Connection getConexion() {
        return conexion;
    }
}