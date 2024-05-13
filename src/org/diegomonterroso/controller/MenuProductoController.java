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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.model.CategoriaProducto;
import org.diegomonterroso.model.Distribuidor;
import org.diegomonterroso.model.Producto;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuProductoController implements Initializable {
    private Main stage;
    
    private int op;
    
    private Connection conexion = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    
    @FXML
    TextField tfProductoId, tfNombre, tfCantidadStock, tfPrecioVentaU, tfPrecioVentaM, tfPrecioCompra, tfProductoBuscarId;
        
    @FXML
    TextArea taDescripcion;
    
    @FXML
    ComboBox cmbDistribuidor, cmbCategoriaProducto;
    
    @FXML
    TableView tblProductos;
    
    @FXML
    TableColumn colProductoId, colNombre, colDescripcion, colCantidadStock, colPrecioVentaU, colPrecioVentaM, colPrecioCompra, colDistribuidor, colCategoria;
    
    @FXML
    Button btnGuardar, btnVaciar, btnRegresar, btnEliminar, btnBuscar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfProductoId.getText().equals("")){
                agregarProducto();
                cargarDatos();
            }else{
                editarProducto();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarForm();
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarProducto(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getProductoId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblProductos.getItems().clear();
            
            if(tfProductoBuscarId.getText().equals("")){
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
        cmbDistribuidor.setItems(listarDistribuidores());
        cmbCategoriaProducto.setItems(listarCategoriaProductos());
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tblProductos.getItems().add(buscarProducto());
            op = 0;
        }else{
        tblProductos.setItems(listarProductos());
            colProductoId.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("productoId"));
            colNombre.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<Producto, String>("descripcionProducto"));
            colCantidadStock.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidadStock"));
            colPrecioVentaU.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaUnitario"));
            colPrecioVentaM.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioVentaMayor"));
            colPrecioCompra.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioCompra"));
            colDistribuidor.setCellValueFactory(new PropertyValueFactory<Producto, String>("distribuidor"));
            colCategoria.setCellValueFactory(new PropertyValueFactory<Producto, String>("categoriaProducto"));
        }

    }
    
    public void vaciarForm(){
        tfProductoId.clear();
        tfNombre.clear();
        taDescripcion.clear();
        tfCantidadStock.clear();
        tfPrecioVentaU.clear();
        tfPrecioVentaM.clear();
        tfPrecioCompra.clear();
        cmbDistribuidor.getSelectionModel().clearSelection();
        cmbCategoriaProducto.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void cargarForm(){
        Producto p = (Producto)tblProductos.getSelectionModel().getSelectedItem();
        if(p != null){
            tfProductoId.setText(Integer.toString(p.getProductoId()));
            tfNombre.setText(p.getNombreProducto());
            taDescripcion.setText(p.getDescripcionProducto());
            tfCantidadStock.setText(Integer.toString(p.getCantidadStock()));
            tfPrecioVentaU.setText(Double.toString(p.getPrecioVentaUnitario()));
            tfPrecioVentaM.setText(Double.toString(p.getPrecioVentaMayor()));
            tfPrecioCompra.setText(Double.toString(p.getPrecioCompra()));
            cmbDistribuidor.getSelectionModel().select(obtenerIndexDistribuidor());
            cmbCategoriaProducto.getSelectionModel().select(obtenerIndexCategoriaProducto());
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
    
    public int obtenerIndexDistribuidor(){
        int index = 0;
        String distribuidorTbl = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDistribuidor();
        for(int i = 0 ; i <= cmbDistribuidor.getItems().size() ; i++){
            String distribuidorCmb = cmbDistribuidor.getItems().get(i).toString();
            
            if(distribuidorTbl.equals(distribuidorCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public int obtenerIndexCategoriaProducto(){
        int index = 0;
        String categoriaProductoTbl = ((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCategoriaProducto();
        for(int i = 0 ; i <= cmbCategoriaProducto.getItems().size() ; i++){
            String categoriaProductoCmb = cmbCategoriaProducto.getItems().get(i).toString();
            
            if(categoriaProductoTbl.equals(categoriaProductoCmb)){
                index = i;
                break;
            }
        }
        
        return index;
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
                String web = resultSet.getString("web");
                
                distribuidores.add(new Distribuidor(distribuidorId, nombreDistribuidor, telefonoDistribuidor, nitDistribuidor, direccionDistribuidor, web));
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
    
    public void agregarProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarProducto(?, ?, ?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombre.getText());
            statement.setString(2, taDescripcion.getText());
            statement.setInt(3, Integer.parseInt(tfCantidadStock.getText()));
            statement.setDouble(4, Double.parseDouble(tfPrecioVentaU.getText()));
            statement.setDouble(5, Double.parseDouble(tfPrecioVentaM.getText()));
            statement.setDouble(6, Double.parseDouble(tfPrecioCompra.getText()));
            statement.setInt(7, ((Distribuidor)cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(8, ((CategoriaProducto)cmbCategoriaProducto.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
            statement.execute();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(statement!= null){
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
    
    public void editarProducto(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarProducto(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfProductoId.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, taDescripcion.getText());
            statement.setInt(4, Integer.parseInt(tfCantidadStock.getText()));
            statement.setDouble(5, Double.parseDouble(tfPrecioVentaU.getText()));
            statement.setDouble(6, Double.parseDouble(tfPrecioVentaM.getText()));
            statement.setDouble(7, Double.parseDouble(tfPrecioCompra.getText()));
            statement.setInt(8, ((Distribuidor)cmbDistribuidor.getSelectionModel().getSelectedItem()).getDistribuidorId());
            statement.setInt(9, ((CategoriaProducto)cmbCategoriaProducto.getSelectionModel().getSelectedItem()).getCategoriaProductoId());
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
    
    public void eliminarProducto(int proId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, proId);
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
    
    public Producto buscarProducto(){
        Producto producto = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarProducto(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfProductoBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int productoId = resultSet.getInt("productoId");
                String nombreProducto = resultSet.getString("nombreProducto");
                String descripcionProducto = resultSet.getString("descripcionProducto");
                int cantidadStock = resultSet.getInt("cantidadStock");
                double precioVentaUnitario = resultSet.getDouble("precioVentaUnitario");
                double precioVentaMayor = resultSet.getDouble("precioVentaMayor");
                double precioCompra = resultSet.getDouble("precioCompra");
                String distribuidorId = resultSet.getString("distribuidorId");
                String categoriaProductoId = resultSet.getString("categoriaProductoId");
                
                producto = new Producto(productoId, nombreProducto, descripcionProducto, cantidadStock, precioVentaUnitario, precioVentaMayor, precioCompra, distribuidorId, categoriaProductoId);
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
     
        return producto;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
