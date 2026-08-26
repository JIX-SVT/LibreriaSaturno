
package org.lsa.system;
import org.lsa.utils.ConexionSingleton;

public class Main {


    public static void main(String[] args) {
         ConexionSingleton miConexion = ConexionSingleton.getInstancia();
        miConexion.conectar();
        System.out.println("Bienvenido a libreria Saturno");
    }
    
    }
    

