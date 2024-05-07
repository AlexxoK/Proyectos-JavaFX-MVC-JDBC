package org.diegomonterroso.model;

public class Cargo {
    private int cargoId;
    private String nombreCargo;
    private String descripcionCargo;
    
    public Cargo(){
        
    }

    public Cargo(int cargoId, String nombreCargo, String descripcionCargo) {
        this.cargoId = cargoId;
        this.nombreCargo = nombreCargo;
        this.descripcionCargo = descripcionCargo;
    }

    public int getCargoId() {
        return cargoId;
    }

    public void setCargoId(int cargoId) {
        this.cargoId = cargoId;
    }

    public String getNombreCargo() {
        return nombreCargo;
    }

    public void setNombreCargo(String nombreCargo) {
        this.nombreCargo = nombreCargo;
    }

    public String getDescripcionCargo() {
        return descripcionCargo;
    }

    public void setDescripcionCargo(String descripcionCargo) {
        this.descripcionCargo = descripcionCargo;
    }

    @Override
    public String toString() {
        return "Cargo{" + "cargoId=" + cargoId + ", nombreCargo=" + nombreCargo + ", descripcionCargo=" + descripcionCargo + '}';
    }
    
    
}
