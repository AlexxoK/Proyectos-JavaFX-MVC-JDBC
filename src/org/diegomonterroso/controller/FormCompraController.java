package org.diegomonterroso.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.dto.CompraDTO;
import org.diegomonterroso.model.Compra;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

import java.text.SimpleDateFormat;

public class FormCompraController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    
    @FXML
    TextField tfCompraId, tfFecha;
    
    @FXML
    Button btnGuardar, btnCancelar;
    
    @FXML
    public void handleButtonAction(ActionEvent event) {
    if (event.getSource() == btnCancelar) {
        CompraDTO.getCompraDTO().setCompra(null);
        stage.menuCompraView();
    } else if (event.getSource() == btnGuardar) {
        
            stage.menuCompraView();
        if (op == 2) {
            if (SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK) {
                editarCompra();
                CompraDTO.getCompraDTO().setCompra(null);
                SuperKinalAlert.getInstance().mostrarAlertaInformacion(500);
                stage.menuCompraView();
            } else {
                stage.menuCompraView();
            }
        }
    }
}
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(CompraDTO.getCompraDTO().getCompra() != null){
            cargarDatos(CompraDTO.getCompraDTO().getCompra());
        }
    }    
    
    public void cargarDatos(Compra compra){
        tfCompraId.setText(Integer.toString(compra.getCompraId()));

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
        tfFecha.setText(formatoFecha.format(compra.getFechaCompra()));
    }
    
    public void editarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_EditarCompra(?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCompraId.getText()));
            statement.setString(2,tfFecha.getText());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement != null){
                    statement.close();
                }
                
                if(conexion != null){
                    conexion.close();
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }

    public void setOp(int op) {
        this.op = op;
    }
    
}
