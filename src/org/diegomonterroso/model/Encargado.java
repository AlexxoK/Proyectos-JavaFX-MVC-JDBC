package org.diegomonterroso.model;

public class Encargado {
    private int encargadoId;
    
    public Encargado() {
    }

    public Encargado(int encargadoId) {
        this.encargadoId = encargadoId;
    }

    public int getEncargadoId() {
        return encargadoId;
    }

    public void setEncargadoId(int encargadoId) {
        this.encargadoId = encargadoId;
    }

    @Override
    public String toString() {
        return "Id: " + encargadoId;
    }

    
}
