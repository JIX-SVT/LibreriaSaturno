package org.lsa.utils;

/**
 *
 * @author Gregory Jerónimo
 */
public class ConexionSingleton {
    
    private static ConexionSingleton instancia;
    
    private ConexionSingleton() {
        System.out.println("Conexión inicializada con éxito.");
    }
  public static synchronized ConexionSingleton getInstancia() {
    if (instancia == null) {
        instancia = new ConexionSingleton();
    } 
    return instancia;
}
    
    public void conectar() {
        System.out.println("Usando la base de datos de manera segura.");
    }
}


  
    

