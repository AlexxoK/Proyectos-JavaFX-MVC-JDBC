package org.diegomonterroso.model;

import java.sql.Time;

public class Empleado {
    private int empleadoId;
    private String nombreEmpleado;
    private String apellidoEmpleado;
    private double sueldo;
    private Time horaEntrada;
    private Time horaSalida;
    
    private int cargoId;
    private String cargo;
    
    private int encargadoId;
    private String encargado;

    public Empleado() {
    }

    public Empleado(int empleadoId, String nombreEmpleado, String apellidoEmpleado, double sueldo, Time horaEntrada, Time horaSalida, String cargo, String encargado) {
        this.empleadoId = empleadoId;
        this.nombreEmpleado = nombreEmpleado;
        this.apellidoEmpleado = apellidoEmpleado;
        this.sueldo = sueldo;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.cargo = cargo;
        this.encargado = encargado;
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getApellidoEmpleado() {
        return apellidoEmpleado;
    }

    public void setApellidoEmpleado(String apellidoEmpleado) {
        this.apellidoEmpleado = apellidoEmpleado;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public Time getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(Time horaEntrada) {
        this.horaEntrada = horaEntrada;
    }
    
    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }
    
    public int getEncargadoId() {
        return encargadoId;
    }

    public void setEncargadoId(int encargadoId) {
        this.encargadoId = encargadoId;
    }

    @Override
    public String toString() {
        return "Empleado{" + "empleadoId=" + empleadoId + ", nombreEmpleado=" + nombreEmpleado + ", apellidoEmpleado=" + apellidoEmpleado + ", sueldo=" + sueldo + ", horaEntrada=" + horaEntrada + ", horaSalida=" + horaSalida + ", cargoId=" + cargoId + ", cargo=" + cargo + ", encargadoId=" + encargadoId + ", encargado=" + encargado + '}';
    }

    
}
