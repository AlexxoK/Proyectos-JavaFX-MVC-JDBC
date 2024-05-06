package org.diegomonterroso.model;

public class Cliente {
    private int clienteId;
    private String nombre;
    private String apellido;
    private String telefono;
    private String nit;
    private String direccion;

    public Cliente() {
    }

    public Cliente(int clienteId, String nombre, String apellido, String telefono, String nit, String direccion) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.nit = nit;
        this.direccion = direccion;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "id: " + clienteId + " | " + nombre + " " + apellido;
    }
    
    
}
