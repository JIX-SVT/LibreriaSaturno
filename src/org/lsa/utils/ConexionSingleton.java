package org.lsa.utils;
 
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
 

public class ConexionSingleton {
 
    private static ConexionSingleton instancia;
    private Connection conexion;
 
    private ConexionSingleton() {
        // Constructor privado para el patrón Singleton
    }
 
    public static synchronized ConexionSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ConexionSingleton();
        }
        return instancia;
    }
 
    public Connection conectar() throws SQLException {
        if (this.conexion == null || this.conexion.isClosed()) {
            Properties props = new Properties();
            try (InputStream input = ConexionSingleton.class.getResourceAsStream("/db.properties")) {
                if (input == null) {
                    System.err.println("Error: No se encontró el archivo db.properties en la raíz del classpath.");
                    throw new SQLException("No se encontró el archivo db.properties");
                }
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");
 
                this.conexion = DriverManager.getConnection(url, user, pass);
                System.out.println("Conexión inicializada con éxito.");
            } catch (Exception e) {
                System.err.println("Error al cargar la configuración de la base de datos: " + e.getMessage());
                throw new SQLException(e);
            }
        }
        return this.conexion;
    }
 
    public Connection getConexion() {
        try {
            return this.conectar();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }   
}
