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
import java.util.Date;
import java.sql.Time;

public class FormCompraController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    
    @FXML
    TextField tfCompraId, tfFecha, tfTotal;
    
    @FXML
    Button btnGuardar, btnCancelar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnCancelar){
            stage.menuCompraView();
            CompraDTO.getCompraDTO().setCompra(null);
        }else if(event.getSource() == btnGuardar){
            if(op == 1){
                if(!tfFecha.getText().equals("") && !tfTotal.getText().equals("")){
                    agregarCompra();
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(400);
                    stage.menuCompraView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(600);
                    tfFecha.requestFocus();
                }
            }else if(op == 2){
                if(!tfFecha.getText().equals("") && !tfTotal.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        editarCompra();
                        CompraDTO.getCompraDTO().setCompra(null);
                        SuperKinalAlert.getInstance().mostrarAlertaInformacion(500);
                        stage.menuCompraView();
                    }else{
                        stage.menuCompraView();
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(600);
                    tfFecha.requestFocus();
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
        Date fechaCompra = compra.getFechaCompra();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fechaCompraStr = dateFormat.format(fechaCompra);
        tfCompraId.setText(Integer.toString(compra.getCompraId()));
        tfFecha.setText(fechaCompraStr);
        tfTotal.setText(Double.toString(compra.getTotalCompra()));
    }
    
    public void agregarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCompra(?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfFecha.getText());
            statement.setString(2, tfTotal.getText());
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
    
    public void editarCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCompra(?, ?, ?)";
            statement = conexion.prepareCall(sql);
            statement.setInt(1, Integer.parseInt(tfCompraId.getText()));
            statement.setString(2, tfFecha.getText());
            statement.setString(3, tfTotal.getText());
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
