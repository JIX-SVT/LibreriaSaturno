   package org.lsa.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class GeneradorTicket {
    private static final Logger LOG = Logger.getLogger(GeneradorTicket.class.getName());

    public static String generarFormatoTicket(String idVenta, String nombreCajero, double totalVenta) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        
        sb.append("========================================\n");
        sb.append("           LIBRERÍA SATURNO             \n");
        sb.append("========================================\n");
        sb.append("Transacción: ").append(idVenta != null ? idVenta : "N/D").append("\n");
        sb.append("Cajero: ").append(nombreCajero != null ? nombreCajero : "General").append("\n");
        sb.append("Fecha: ").append(dtf.format(LocalDateTime.now())).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-22s %-6s %-8s\n", "PRODUCTO", "CANT", "PRECIO"));
        sb.append("----------------------------------------\n");
        
        sb.append(String.format("%-22s %-6d Q%-7.2f\n", "Libro de Ejemplo", 1, totalVenta));
        
        sb.append("----------------------------------------\n");
        sb.append(String.format("TOTAL A PAGAR: Q%.2f\n", totalVenta));
        sb.append("========================================\n");
        sb.append("       ¡GRACIAS POR SU COMPRA!          \n");
        sb.append("=======================================\n");

        LOG.info("Ticket estructurado en texto para la venta: " + idVenta);
        return sb.toString();
    }
}
