package org.diegomonterroso.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.model.Compra;
import org.diegomonterroso.model.Producto;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class FormDetalleCompraController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfDetalleCompraId, tfCantidad;
    
    @FXML
    ComboBox cmbProducto, cmbCompra;
    
    @FXML
    Button btnGuardar, btnRegresarForm;
    
    @FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnRegresarForm) {
            stage.menuCompraView();
        }else if (event.getSource() == btnGuardar) {
            if (op == 1) {
                agregarDetalleCompra();
                SuperKinalAlert.getInstance().mostrarAlertaInformacion(400);
                stage.menuCompraView();
            }
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbCompra.setItems(listarCompras());
        cmbProducto.setItems(listarProductos());
    }
    
    public void agregarDetalleCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDetalleCompra(?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfCantidad.getText());
            statement.setInt(2,((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(3,((Compra)cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
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
    
    public ObservableList<Producto> listarProductos(){
        ArrayList<Producto> productos = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarProductos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int productoId = resultSet.getInt("productoId");
                String nombreProducto = resultSet.getString("nombreProducto");
                String descripcionProducto = resultSet.getString("descripcionProducto");
                int cantidadStock = resultSet.getInt("cantidadStock");
                double precioVentaUnitario = resultSet.getDouble("precioVentaUnitario");
                double precioVentaMayor = resultSet.getDouble("precioVentaMayor");
                double precioCompra = resultSet.getDouble("precioCompra");
                String distribuidor = resultSet.getString("Distribuidor");
                String categoriaProducto = resultSet.getString("Categoria");
                
                productos.add(new Producto(productoId, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, distribuidor, categoriaProducto));
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
        return FXCollections.observableList(productos);
    }
    
    public ObservableList<Compra> listarCompras(){
        ArrayList<Compra> compras = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCompras()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int compraId = resultSet.getInt("compraId");
                Date fechaCompra = resultSet.getDate("fechaCompra");  
                double totalCompra = resultSet.getDouble("totalCompra");
                
                compras.add(new Compra(compraId, fechaCompra, totalCompra));
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
        
        return FXCollections.observableList(compras);
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
