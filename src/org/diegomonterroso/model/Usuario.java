package org.diegomonterroso.model;

public class Usuario {
    private int usuarioId;
    private String user;
    private String contrasenia;
    private int nivelAccesoId;
    private int empleadoId;
    
    public Usuario() {
    }

    public Usuario(int usuarioId, String user, String contrasenia, int nivelAccesoId, int empleadoId) {
        this.usuarioId = usuarioId;
        this.user = user;
        this.contrasenia = contrasenia;
        this.nivelAccesoId = nivelAccesoId;
        this.empleadoId = empleadoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getNivelAccesoId() {
        return nivelAccesoId;
    }

    public void setNivelAccesoId(int nivelAccesoId) {
        this.nivelAccesoId = nivelAccesoId;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    @Override
    public String toString() {
        return "Id: " + usuarioId + " | " + user;
    }
    
    
}
