package org.lsa.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSingleton {

    private static ConexionSingleton instancia;
    private Connection conexion;

    private final String URL = "jdbc:mysql://localhost:3306/libreriadb_in4cm";
    private final String USUARIO = "root"; 
    private final String CLAVE = "BEBE"; 

    private ConexionSingleton() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexión inicializada con éxito.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la Base de Datos: " + e.getMessage());
        }
    }
    public static synchronized ConexionSingleton getInstance() {
        try {
            if (instancia == null || instancia.getConexion().isClosed()) {
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
