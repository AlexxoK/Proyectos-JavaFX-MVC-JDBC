package org.diegomonterroso.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.dto.DistribuidorDTO;
import org.diegomonterroso.model.Distribuidor;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuDistribuidorController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    @FXML
    TableView tblDistribuidores;
    
    @FXML
    TableColumn colDistribuidorId, colNombre, colTelefono, colNit, colDireccion, colWeb;
    
    @FXML
    Button btnRegresar, btnAgregar, btnEditar, btnEliminar, btnBuscar;
    
    @FXML
    TextField tfDistribuidorId;
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnAgregar){
            stage.formDistribuidorView(1);
        }else if(event.getSource() == btnEditar){
            DistribuidorDTO.getDistribuidorDTO().setDistribuidor((Distribuidor)tblDistribuidores.getSelectionModel().getSelectedItem());
            stage.formDistribuidorView(2);
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarDistribuidor(((Distribuidor)tblDistribuidores.getSelectionModel().getSelectedItem()).getDistribuidorId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblDistribuidores.getItems().clear();
            
            if(tfDistribuidorId.getText().equals("")){
                cargarDatos();
            }else{
                op = 3;
                cargarDatos();
            }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tblDistribuidores.getItems().add(buscarDistribuidor());
            op = 0;
        }else{
            tblDistribuidores.setItems(listarDistribuidores());
            colDistribuidorId.setCellValueFactory(new PropertyValueFactory<Distribuidor, Integer>("distribuidorId"));
            colNombre.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("nombreDistribuidor"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("telefonoDistribuidor"));
            colNit.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("nitDistribuidor"));
            colDireccion.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("direccionDistribuidor"));
            colWeb.setCellValueFactory(new PropertyValueFactory<Distribuidor, String>("webDistribuidor"));
        }
    }

    public ObservableList<Distribuidor> listarDistribuidores(){
        ArrayList<Distribuidor> distribuidores = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDistribuidores()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");  
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String webDistribuidor = resultSet.getString("webDistribuidor");
                
                distribuidores.add(new Distribuidor(distribuidorId, nombreDistribuidor, telefonoDistribuidor, nitDistribuidor, direccionDistribuidor, webDistribuidor));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
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
        
        return FXCollections.observableList(distribuidores);
    }
    
    public void eliminarDistribuidor(int disId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarDistribuidor(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, disId);
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
    
    public Distribuidor buscarDistribuidor(){
        Distribuidor distribuidor = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDistribuidor(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDistribuidorId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int distribuidorId = resultSet.getInt("distribuidorId");
                String nombreDistribuidor = resultSet.getString("nombreDistribuidor");  
                String telefonoDistribuidor = resultSet.getString("telefonoDistribuidor");
                String nitDistribuidor = resultSet.getString("nitDistribuidor");
                String direccionDistribuidor = resultSet.getString("direccionDistribuidor");
                String webDistribuidor = resultSet.getString("webDistribuidor");
                
                distribuidor = new Distribuidor(distribuidorId, nombreDistribuidor, telefonoDistribuidor, nitDistribuidor, direccionDistribuidor, webDistribuidor);
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(resultSet != null){
                    resultSet.close();
                }
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
     
        return distribuidor;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
