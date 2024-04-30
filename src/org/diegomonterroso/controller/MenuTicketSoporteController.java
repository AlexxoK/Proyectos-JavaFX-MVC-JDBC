package org.diegomonterroso.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.diegomonterroso.system.Main;

public class MenuTicketSoporteController implements Initializable {
    Main stage;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }    

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
