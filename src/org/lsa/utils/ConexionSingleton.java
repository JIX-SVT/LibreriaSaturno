package org.lsa.utils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Gregory Jerónimo
 */
public class ConexionSingleton {

    private static ConexionSingleton instancia;
    private Connection conexion;

    private ConexionSingleton() {
        System.out.println("Conexión inicializada con éxito.");
    }

    public static synchronized ConexionSingleton getInstancia() {
        if (instancia == null) {
            instancia = new ConexionSingleton();
        }
        return instancia;
    }

    public Connection conectar() throws SQLException {
        this.conexion = new Conexion().conectar();
        return this.conexion;
    }

    public Connection getConexion() {
        return this.conexion;
    }
}