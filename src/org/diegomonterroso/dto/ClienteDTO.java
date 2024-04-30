package org.diegomonterroso.dto;

import org.diegomonterroso.model.Cliente;

public class ClienteDTO {
    // Inicio Singleton
    private static ClienteDTO instance;
    private Cliente cliente;
    
    private ClienteDTO(){
    
    }
    
    public static ClienteDTO getClienteDTO(){ // Se quita getInstance y se cambia por getClienteDTO
        if(instance == null){
            instance = new ClienteDTO();
        }
        return instance;
    }
    // Fin Singleton

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    
}
