package org.diegomonterroso.model;

public class NivelAcceso {
    private int nivelAccesoId;
    private String nivelAcceso;

    public NivelAcceso() {
    }

    public NivelAcceso(int nivelAccesoId, String nivelAcceso) {
        this.nivelAccesoId = nivelAccesoId;
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelAccesoId() {
        return nivelAccesoId;
    }

    public void setNivelAccesoId(int nivelAccesoId) {
        this.nivelAccesoId = nivelAccesoId;
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    @Override
    public String toString() {
        return "Id: " + nivelAccesoId + " | " + nivelAcceso;
    }
    
    
}
