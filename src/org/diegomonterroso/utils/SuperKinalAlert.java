package org.diegomonterroso.utils;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SuperKinalAlert {
    private static SuperKinalAlert instance;
    
    
    private SuperKinalAlert(){
    
    }
    
    public static SuperKinalAlert getInstance(){
        if(instance == null){
            instance = new SuperKinalAlert();
        }
        return instance;
    }
    
    public void mostrarAlertaInformacion(int code){
        if(code == 400){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmacion Registro");
            alert.setHeaderText("Confirmacion Registro");
            alert.setContentText("Registro realizado con exito!");
            alert.showAndWait();
        }else if(code == 500){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Edicion Registro");
            alert.setHeaderText("Edicion Registro");
            alert.setContentText("Edicion realizado con exito!");
            alert.showAndWait();
        }else if(code == 600){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Campos Pendientes");
            alert.setHeaderText("Campos Pendientes");
            alert.setContentText("Llena los campos necesarios!!");
            alert.showAndWait();
        }else if(code == 602){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Usuario Incorrecto");
            alert.setHeaderText("Usuario Incorrecto");
            alert.setContentText("Verifique el Usuario!!");
            alert.showAndWait();
        }else if(code == 005){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Contrasenia Incorrecta");
            alert.setHeaderText("Contraseña Incorrecta");
            alert.setContentText("Verifique su Contraseña!!");
            alert.showAndWait();
        }
    }
    
    public Optional<ButtonType> mostrarAlertaConfirmacion(int code){
        Optional<ButtonType> action = null;
        
        if(code == 404){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminacion Registro");
            alert.setHeaderText("Eliminacion Registro");
            alert.setContentText("Desea confirmar al eliminacion del registro?");
            action = alert.showAndWait();
        }else if(code == 505){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Edicion Registro");
            alert.setHeaderText("Edicion Registro");
            alert.setContentText("Desea confirmar al edicion del registro?");
            action = alert.showAndWait();
        }
        return action;
    }
    
    public void alertaSaludo(String usuario){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("BIENVENIDO!!");
        alert.setHeaderText("Bienvenido seas " + usuario);
        alert.showAndWait();
    }
}
