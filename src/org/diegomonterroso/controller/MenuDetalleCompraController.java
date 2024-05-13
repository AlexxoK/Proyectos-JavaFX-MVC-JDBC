package org.diegomonterroso.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.model.Compra;
import org.diegomonterroso.model.DetalleCompra;
import org.diegomonterroso.model.DetalleFactura;
import org.diegomonterroso.model.Factura;
import org.diegomonterroso.model.Producto;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuDetalleCompraController implements Initializable {
    private Main stage;
    
    private int op;
    
    private Connection conexion = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    
    @FXML
    TextField tfDetalleCompraId, tfCantidad, tfDetalleCompraBuscarId;
    
    @FXML
    ComboBox cmbProducto, cmbCompra;
    
    @FXML
    TableView tblDetalleCompras;
    
    @FXML
    TableColumn colDetalleCompraId, colCantidad, colProducto, colCompra;
    
    @FXML
    Button btnGuardar, btnVaciar, btnRegresar, btnEliminar, btnBuscar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfDetalleCompraId.getText().equals("")){
                agregarDetalleCompra();
                cargarDatos();
            }else{
                editarDetalleCompra();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarForm();
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarDetalleCompra(((DetalleCompra)tblDetalleCompras.getSelectionModel().getSelectedItem()).getDetalleCompraId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblDetalleCompras.getItems().clear();
            
            if(tfDetalleCompraBuscarId.getText().equals("")){
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
        cmbProducto.setItems(listarProductos());
        cmbCompra.setItems(listarCompras());
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tblDetalleCompras.getItems().add(buscarDetalleCompra());
            op = 0;
        }else{
        tblDetalleCompras.setItems(listarDetalleCompra());
            colDetalleCompraId.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("detalleCompraId"));
            colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidadCompra"));
            colProducto.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("producto"));
            colCompra.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("compra"));
        }

    }
    
    public void vaciarForm(){
        tfDetalleCompraId.clear();
        tfCantidad.clear();
        cmbProducto.getSelectionModel().clearSelection();
        cmbCompra.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void cargarForm(){
        DetalleCompra dc = (DetalleCompra)tblDetalleCompras.getSelectionModel().getSelectedItem();
        if(dc != null){
            tfDetalleCompraId.setText(Integer.toString(dc.getDetalleCompraId()));
            tfCantidad.setText(Integer.toString(dc.getCantidadCompra()));
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
            cmbCompra.getSelectionModel().select(obtenerIndexCompra());
        }
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        String productoTbl = ((DetalleCompra)tblDetalleCompras.getSelectionModel().getSelectedItem()).getProducto();
        for(int i = 0 ; i <= cmbProducto.getItems().size() ; i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            
            if(productoTbl.equals(productoCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public int obtenerIndexCompra(){
        int index = 0;
        String compraTbl = ((DetalleCompra)tblDetalleCompras.getSelectionModel().getSelectedItem()).getCompra();
        for(int i = 0 ; i <= cmbCompra.getItems().size() ; i++){
            String compraCmb = cmbCompra.getItems().get(i).toString();
            
            if(compraTbl.equals(compraCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public ObservableList<DetalleCompra> listarDetalleCompra(){
        ArrayList<DetalleCompra> detalleCompras = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDetalleCompra()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int detalleCompraId = resultSet.getInt("detalleCompraId");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String producto = resultSet.getString("producto");
                String compra = resultSet.getString("compra");
                
                detalleCompras.add(new DetalleCompra(detalleCompraId, cantidadCompra, producto, compra));
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
        
        return FXCollections.observableList(detalleCompras);
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
    
    public void agregarDetalleCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDetalleCompra(?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfCantidad.getText()));
            statement.setInt(2, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(3, ((Compra)cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
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
    
    public void editarDetalleCompra(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarFactura(?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDetalleCompraId.getText()));
            statement.setInt(2, Integer.parseInt(tfCantidad.getText()));
            statement.setInt(3, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
            statement.setInt(4, ((Compra)cmbCompra.getSelectionModel().getSelectedItem()).getCompraId());
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
    
    public void eliminarDetalleCompra(int detComId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarDetalleCompra(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, detComId);
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
    
    public DetalleCompra buscarDetalleCompra(){
        DetalleCompra detalleCompra = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDetalleFactura(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDetalleCompraBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int detalleCompraId = resultSet.getInt("detalleCompraId");
                int cantidadCompra = resultSet.getInt("cantidadCompra");
                String productoId = resultSet.getString("productoId");
                String compraId = resultSet.getString("compraId");
                
                detalleCompra = new DetalleCompra(detalleCompraId, cantidadCompra, productoId, compraId);
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
     
        return detalleCompra;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
