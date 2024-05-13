package org.diegomonterroso.model;

public class Distribuidor {
    private int distribuidorId;
    private String nombreDistribuidor;
    private String telefonoDistribuidor;
    private String nitDistribuidor;
    private String direccionDistribuidor;
    private String web;

    public Distribuidor() {
        
    }

    public Distribuidor(int distribuidorId, String nombreDistribuidor, String telefonoDistribuidor, String nitDistribuidor, String direccionDistribuidor, String web) {
        this.distribuidorId = distribuidorId;
        this.nombreDistribuidor = nombreDistribuidor;
        this.telefonoDistribuidor = telefonoDistribuidor;
        this.nitDistribuidor = nitDistribuidor;
        this.direccionDistribuidor = direccionDistribuidor;
        this.web = web;
    }

    public int getDistribuidorId() {
        return distribuidorId;
    }

    public void setDisctribuidorId(int distribuidorId) {
        this.distribuidorId = distribuidorId;
    }

    public String getNombreDistribuidor() {
        return nombreDistribuidor;
    }

    public void setNombreDistribuidor(String nombreDistribuidor) {
        this.nombreDistribuidor = nombreDistribuidor;
    }

    public String getTelefonoDistribuidor() {
        return telefonoDistribuidor;
    }

    public void setTelefonoDistribuidor(String telefonoDistribuidor) {
        this.telefonoDistribuidor = telefonoDistribuidor;
    }

    public String getNitDistribuidor() {
        return nitDistribuidor;
    }

    public void setNitDistribuidor(String nitDistribuidor) {
        this.nitDistribuidor = nitDistribuidor;
    }

    public String getDireccionDistribuidor() {
        return direccionDistribuidor;
    }

    public void setDireccionDistribuidor(String direccionDistribuidor) {
        this.direccionDistribuidor = direccionDistribuidor;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public String toString() {
        return "Id: " + distribuidorId + " | " + nombreDistribuidor;
    }
    
    
}
