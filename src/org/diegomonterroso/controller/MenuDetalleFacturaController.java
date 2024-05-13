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
import org.diegomonterroso.model.DetalleFactura;
import org.diegomonterroso.model.Factura;
import org.diegomonterroso.model.Producto;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuDetalleFacturaController implements Initializable {
    private Main stage;
    
    private int op;
    
    private Connection conexion = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    
    @FXML
    TextField tfDetalleFacturaId, tfDetalleFacturaBuscarId;
    
    @FXML
    ComboBox cmbFactura, cmbProducto;
    
    @FXML
    TableView tblDetalleFacturas;
    
    @FXML
    TableColumn colDetalleFacturaId, colFactura, colProducto;
    
    @FXML
    Button btnGuardar, btnVaciar, btnRegresar, btnEliminar, btnBuscar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfDetalleFacturaId.getText().equals("")){
                agregarDetalleFactura();
                cargarDatos();
            }else{
                editarDetalleFactura();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarForm();
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarDetalleFactura(((DetalleFactura)tblDetalleFacturas.getSelectionModel().getSelectedItem()).getDetalleFacturaId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblDetalleFacturas.getItems().clear();
            
            if(tfDetalleFacturaBuscarId.getText().equals("")){
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
        cmbFactura.setItems(listarFacturas());
        cmbProducto.setItems(listarProductos());
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tblDetalleFacturas.getItems().add(buscarDetalleFactura());
            op = 0;
        }else{
        tblDetalleFacturas.setItems(listarDetalleFactura());
            colDetalleFacturaId.setCellValueFactory(new PropertyValueFactory<DetalleFactura, Integer>("detalleFacturaId"));
            colFactura.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("factura"));
            colProducto.setCellValueFactory(new PropertyValueFactory<DetalleFactura, String>("producto"));
        }

    }
    
    public void vaciarForm(){
        tfDetalleFacturaId.clear();
        cmbFactura.getSelectionModel().clearSelection();
        cmbProducto.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void cargarForm(){
        DetalleFactura df = (DetalleFactura)tblDetalleFacturas.getSelectionModel().getSelectedItem();
        if(df != null){
            tfDetalleFacturaId.setText(Integer.toString(df.getDetalleFacturaId()));
            cmbFactura.getSelectionModel().select(obtenerIndexFactura());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexFactura(){
        int index = 0;
        String facturaTbl = ((DetalleFactura)tblDetalleFacturas.getSelectionModel().getSelectedItem()).getFactura();
        for(int i = 0 ; i <= cmbFactura.getItems().size() ; i++){
            String facturaCmb = cmbFactura.getItems().get(i).toString();
            
            if(facturaTbl.equals(facturaCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        String productoTbl = ((DetalleFactura)tblDetalleFacturas.getSelectionModel().getSelectedItem()).getProducto();
        for(int i = 0 ; i <= cmbProducto.getItems().size() ; i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            
            if(productoTbl.equals(productoCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public ObservableList<DetalleFactura> listarDetalleFactura(){
        ArrayList<DetalleFactura> detalleFacturas = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarDetalleFactura()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int detalleFacturaId = resultSet.getInt("detalleFacturaId");
                String factura = resultSet.getString("factura");
                String producto = resultSet.getString("producto");
                
                detalleFacturas.add(new DetalleFactura(detalleFacturaId, factura, producto));
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
        
        return FXCollections.observableList(detalleFacturas);
    }
    
    public ObservableList<Factura> listarFacturas(){
        ArrayList<Factura> facturas = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarFacturas()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int facturaId = resultSet.getInt("facturaId");
                Date fecha = resultSet.getDate("fecha");
                Time hora = resultSet.getTime("hora");
                String cliente = resultSet.getString("cliente");
                String empleado = resultSet.getString("empleado");
                double total = resultSet.getDouble("total");
                facturas.add(new Factura(facturaId, fecha, hora, cliente, empleado, total));
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
        
        return FXCollections.observableList(facturas);
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
    
    public void agregarDetalleFactura(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarDetalleFactura(?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, ((Factura)cmbFactura.getSelectionModel().getSelectedItem()).getFacturaId());
            statement.setInt(2, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public void editarDetalleFactura(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarFactura(?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDetalleFacturaId.getText()));
            statement.setInt(2, ((Factura)cmbFactura.getSelectionModel().getSelectedItem()).getFacturaId());
            statement.setInt(3, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public void eliminarDetalleFactura(int detFactId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarDetalleFactura(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, detFactId);
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
    
    public DetalleFactura buscarDetalleFactura(){
        DetalleFactura detalleFactura = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarDetalleFactura(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfDetalleFacturaBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int detalleFacturaId = resultSet.getInt("detalleFacturaId");
                String facturaId = resultSet.getString("facturaId");
                String productoId = resultSet.getString("productoId");
                
                detalleFactura = new DetalleFactura(detalleFacturaId, facturaId, productoId);
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
     
        return detalleFactura;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
}
