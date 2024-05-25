package org.diegomonterroso.dto;

import org.diegomonterroso.model.DetalleCompra;

public class DetalleCompraDTO {
    private static DetalleCompraDTO instance;
    private DetalleCompra detalleCompra;
    
    private DetalleCompraDTO(){
    
    }
    
    public static DetalleCompraDTO getDetalleCompraDTO(){
        if(instance == null){
            instance = new DetalleCompraDTO();
        }
        return instance;
    }

    public DetalleCompra getDetalleCompra() {
        return detalleCompra;
    }

    public void setDetalleCompra(DetalleCompra detalleCompra) {
        this.detalleCompra = detalleCompra;
    }
}
