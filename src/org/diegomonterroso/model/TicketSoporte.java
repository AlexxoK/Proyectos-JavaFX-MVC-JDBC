package org.diegomonterroso.model;

public class TicketSoporte {
    private int ticketSoporteId;
    private String descripcion;
    private String estatus;
    
    //Llaves foráneas con Join :)))))
    private int clienteId;
    private String cliente;
    
    //Si no se usa de ninguna manera (Objeto o Join), solamente se deja como un Id
    private int facturaId;
    private String factura;
    
    public TicketSoporte(){
    
    }

    public TicketSoporte(int ticketSoporteId, String descripcion, String estatus, String cliente, String factura) {
        this.ticketSoporteId = ticketSoporteId;
        this.descripcion = descripcion;
        this.estatus = estatus;
        this.cliente = cliente;
        this.factura = factura;
    }

    public int getTicketSoporteId() {
        return ticketSoporteId;
    }

    public void setTicketSoporteId(int ticketSoporteId) {
        this.ticketSoporteId = ticketSoporteId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    @Override
    public String toString() {
        return "TicketSoporte{" + "ticketSoporteId=" + ticketSoporteId + ", descripcion=" + descripcion + ", estatus=" + estatus + ", clienteId=" + clienteId + ", cliente=" + cliente + ", facturaId=" + facturaId + '}';
    }
    
    
}
