package org.diegomonterroso.dto;

import org.diegomonterroso.model.Factura;

public class FacturaDTO {
    private static FacturaDTO instance;
    private Factura factura;
    
    private FacturaDTO(){
    
    }
    
    public static FacturaDTO getFacturaDTO(){
        if(instance == null){
            instance = new FacturaDTO();
        }
        return instance;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
