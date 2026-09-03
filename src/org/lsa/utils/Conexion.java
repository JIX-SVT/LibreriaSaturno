package org.lsa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static Conexion instancia;
    private Connection conexion;

    private Conexion() {
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection conectar() {
        Properties propiedades = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("Error: No se encontró el archivo db.properties en la raíz del classpath.");
                return null;
            }

            propiedades.load(input);

            String url = propiedades.getProperty("url"); 
            String user = propiedades.getProperty("user");
            String password = propiedades.getProperty("password");

            if (url == null || url.trim().isEmpty()) {
                throw new SQLException("La URL de conexión es nula. Verifica que la clave 'url' exista en db.properties");
            }

            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión inicializada con éxito.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de propiedades: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
        }
        
        return conexion;
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}