package org.lsa.model;

public class DetalleCompra {

    public static class VentaDTO {
        private String idVenta;
        private String cliente;
        private String cajero;
        private double total;

        public VentaDTO(String idVenta, String cliente, String cajero, double total) {
            this.idVenta = idVenta;
            this.cliente = cliente;
            this.cajero = cajero;
            this.total = total;
        }

        public String getIdVenta() {
            return idVenta;
        }

        public void setIdVenta(String idVenta) {
            this.idVenta = idVenta;
        }

        public String getCliente() {
            return cliente;
        }

        public void setCliente(String cliente) {
            this.cliente = cliente;
        }

        public String getCajero() {
            return cajero;
        }

        public void setCajero(String cajero) {
            this.cajero = cajero;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

    }
    
    private int idDetalleCompra;
    private int noCompra;
    private String isbn;

    public DetalleCompra() {
    }

    public DetalleCompra(int idDetalleCompra, int noCompra, String isbn) {
        this.idDetalleCompra = idDetalleCompra;
        this.noCompra = noCompra;
        this.isbn = isbn;
    }

    public int getIdDetalleCompra() {
        return idDetalleCompra;
    }

    public void setIdDetalleCompra(int idDetalleCompra) {
        this.idDetalleCompra = idDetalleCompra;
    }

    public int getNoCompra() {
        return noCompra;
    }

    public void setNoCompra(int noCompra) {
        this.noCompra = noCompra;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "DetalleCompra{" + 
               "idDetalleCompra=" + idDetalleCompra + 
               ", noCompra=" + noCompra + 
               ", isbn='" + isbn + '\'' + 
               '}';
    }
}