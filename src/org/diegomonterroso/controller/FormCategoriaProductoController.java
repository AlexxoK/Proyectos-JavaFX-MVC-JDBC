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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.dto.CategoriaProductoDTO;
import org.diegomonterroso.model.CategoriaProducto;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class FormCategoriaProductoController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    
    @FXML
    TextField tfCategoriaProductoId, tfNombre;
    
    @FXML
    TextArea taDescripcion;
    @FXML
    Button btnGuardar, btnCancelar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnCancelar){
            stage.menuCategoriaProductoView();
            CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
        }else if(event.getSource() == btnGuardar){
            if(op == 1){
                if(!tfNombre.getText().equals("") && !taDescripcion.getText().equals("")){
                    agregarCategoriaProducto();
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(400);
                    stage.menuCategoriaProductoView();
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(600);
                    tfNombre.requestFocus();
                }
            }else if(op == 2){
                if(!tfNombre.getText().equals("") && !taDescripcion.getText().equals("")){
                    if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK){
                        editarCategoriaProducto();
                        CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto(null);
                        SuperKinalAlert.getInstance().mostrarAlertaInformacion(500);
                        stage.menuCategoriaProductoView();
                    }else{
                        stage.menuCategoriaProductoView();
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(600);
                    tfNombre.requestFocus();
                }
            }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto() != null){
            cargarDatos(CategoriaProductoDTO.getCategoriaProductoDTO().getCategoriaProducto());
        }
    }
    
    public void cargarDatos(CategoriaProducto categoriaProducto){
        tfCategoriaProductoId.setText(Integer.toString(categoriaProducto.getCategoriaProductoId()));
        tfNombre.setText(categoriaProducto.getNombreCategoria());
        taDescripcion.setText(categoriaProducto.getDescripcionCategoria());
    }
    
    public void agregarCategoriaProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarCategoriaProducto(?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombre.getText());
            statement.setString(2, taDescripcion.getText());
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
    
    public void editarCategoriaProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarCategoriaProducto(?, ?, ?)";
            statement = conexion.prepareCall(sql);
            statement.setInt(1, Integer.parseInt(tfCategoriaProductoId.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, taDescripcion.getText());
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
