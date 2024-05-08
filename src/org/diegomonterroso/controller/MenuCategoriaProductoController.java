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
import org.diegomonterroso.dto.CategoriaProductoDTO;
import org.diegomonterroso.model.CategoriaProducto;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuCategoriaProductoController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    @FXML
    TableView tblCategoriaProductos;
    
    @FXML
    TableColumn colCategoriaProductoId, colNombre, colDescripcion;
    
    @FXML
    Button btnRegresar, btnAgregar, btnEditar, btnEliminar, btnBuscar;
    
    @FXML
    TextField tfCategoriaProductoId;
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnAgregar){
            stage.formCategoriaProductoView(1);
        }else if(event.getSource() == btnEditar){
            CategoriaProductoDTO.getCategoriaProductoDTO().setCategoriaProducto((CategoriaProducto)tblCategoriaProductos.getSelectionModel().getSelectedItem());
            stage.formCategoriaProductoView(2);
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarCategoriaProducto(((CategoriaProducto)tblCategoriaProductos.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblCategoriaProductos.getItems().clear();
            
            if(tfCategoriaProductoId.getText().equals("")){
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
            tblCategoriaProductos.getItems().add(buscarCategoriaProducto());
            op = 0;
        }else{
            tblCategoriaProductos.setItems(listarCategoriaProductos());
            colCategoriaProductoId.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, Integer>("categoriaProductoId"));
            colNombre.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("nombreCategoria"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<CategoriaProducto, String>("descripcionCategoria"));
        }
    }

    public ObservableList<CategoriaProducto> listarCategoriaProductos(){
        ArrayList<CategoriaProducto> categoriaProductos = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCategoriaProductos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int categoriaProductoId = resultSet.getInt("categoriaProductoId");
                String nombreCategoria = resultSet.getString("nombreCategoria");  
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                
                categoriaProductos.add(new CategoriaProducto(categoriaProductoId, nombreCategoria, descripcionCategoria));
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
        
        return FXCollections.observableList(categoriaProductos);
    }
    
    public void eliminarCategoriaProducto(int capId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCategoriaProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, capId);
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
    
    public CategoriaProducto buscarCategoriaProducto(){
        CategoriaProducto categoriaProducto = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarCategoriaProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCategoriaProductoId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int categoriaProductoId = resultSet.getInt("categoriaProductoId");
                String nombreCategoria = resultSet.getString("nombreCategoria");
                String descripcionCategoria = resultSet.getString("descripcionCategoria");
                
                categoriaProducto = new CategoriaProducto(categoriaProductoId, nombreCategoria, descripcionCategoria);
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
     
        return categoriaProducto;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
