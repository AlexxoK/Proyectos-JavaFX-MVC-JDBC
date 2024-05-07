package org.diegomonterroso.dto;

import org.diegomonterroso.model.Compra;

public class CompraDTO {
    private static CompraDTO instance;
    private Compra compra;
    
    private CompraDTO(){
    
    }
    
    public static CompraDTO getCompraDTO(){
        if(instance == null){
            instance = new CompraDTO();
        }
        return instance;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
    
    
}
