package org.lsa.utils;

import java.sql.*;

public class GeneradorTicket {

    public static String generarFormatoTicket(String idVenta, String cajero, double total) {
        StringBuilder ticket = new StringBuilder();
        
        // Estructura visual de la cabecera del Ticket
        ticket.append("========================================\n");
        ticket.append("            LIBRERÍA SATURNO            \n");
        ticket.append("========================================\n");
        ticket.append("Transacción: ").append(idVenta).append("\n");
        ticket.append("Cajero: ").append(cajero).append("\n");
        ticket.append("Fecha: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date())).append("\n");
        ticket.append("----------------------------------------\n");
        ticket.append(String.format("%-22s %-4s %-10s\n", "PRODUCTO", "CANT", "PRECIO"));
        ticket.append("----------------------------------------\n");

        // ⚠️ CONSULTA CORREGIDA: Se adaptó a 'detalle_compra' y 'libros' agrupando por título
        String query = "SELECT l.titulo, COUNT(dc.isbn) as cantidad, l.precio " +
                       "FROM detalle_compra dc " +
                       "INNER JOIN libros l ON dc.isbn = l.isbn " +
                       "WHERE CAST(dc.no_compra AS CHAR) = ? " +
                       "GROUP BY l.titulo, l.precio";

        // Extraemos solo la parte numérica si el ID viene con formato "FAC-6"
        String soloIdNumerico = idVenta.replace("FAC-", "").trim();

        try (Connection conn = ConexionSingleton.getInstancia().getConexion();
             PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setString(1, soloIdNumerico);
            
            try (ResultSet rs = ps.executeQuery()) {
                boolean tieneRegistros = false;
                while (rs.next()) {
                    tieneRegistros = true;
                    String titulo = rs.getString("titulo");
                    int cant = rs.getInt("cantidad");
                    double precioUnitario = rs.getDouble("precio");
                    
                    // Ajuste de longitud de texto para mantener alineadas las columnas
                    if (titulo.length() > 20) {
                        titulo = titulo.substring(0, 17) + "...";
                    }
                    
                    ticket.append(String.format("%-22s %-4d Q%-10.2f\n", titulo, cant, precioUnitario));
                }
                
                if (!tieneRegistros) {
                    ticket.append(" [No se encontraron productos] \n");
                }
            }
        } catch (SQLException e) {
            ticket.append(" [Error al cargar artículos de la BD] \n");
            e.printStackTrace();
        }

        // Cierre y totales del Ticket informativo
        ticket.append("----------------------------------------\n");
        ticket.append(String.format("TOTAL A PAGAR: Q %.2f\n", total));
        ticket.append("========================================\n");
        ticket.append("         ¡GRACIAS POR SU COMPRA!        \n");
        ticket.append("========================================\n");

        return ticket.toString();
    }
}
