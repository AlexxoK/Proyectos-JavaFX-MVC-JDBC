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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.model.Producto;
import org.diegomonterroso.model.Promocion;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuPromocionController implements Initializable {
    private Main stage;
    
    private int op;
    
    private Connection conexion = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    
    @FXML
    TextField tfPromocionId, tfPrecio, tfFechaInicio, tfFechaFinalizacion, tfPromocionBuscarId;
    
    @FXML
    TextArea taDescripcion;
    @FXML
    ComboBox cmbProducto;
    
    @FXML
    TableView tblPromociones;
    
    @FXML
    TableColumn colPromocionId, colPrecio, colDescripcion, colFechaInicio, colFechaFinalizacion, colProducto;
    
    @FXML
    Button btnGuardar, btnVaciar, btnRegresar, btnEliminar, btnBuscar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfPromocionId.getText().equals("")){
                agregarPromocion();
                cargarDatos();
            }else{
                editarPromocion();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarForm();
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarPromocion(((Promocion)tblPromociones.getSelectionModel().getSelectedItem()).getPromocionId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblPromociones.getItems().clear();
            
            if(tfPromocionBuscarId.getText().equals("")){
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
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tblPromociones.getItems().add(buscarPromocion());
            op = 0;
        }else{
        tblPromociones.setItems(listarPromociones());
            colPromocionId.setCellValueFactory(new PropertyValueFactory<Promocion, Integer>("promocionId"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<Promocion, Double>("precioPromocion"));
            colDescripcion.setCellValueFactory(new PropertyValueFactory<Promocion, String>("descripcionPromocion"));
            colFechaInicio.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaInicio"));
            colFechaFinalizacion.setCellValueFactory(new PropertyValueFactory<Promocion, Date>("fechaFinalizacion"));
            colProducto.setCellValueFactory(new PropertyValueFactory<Promocion, String>("producto"));
        }

    }
    
    public void vaciarForm(){
        tfPromocionId.clear();
        tfPrecio.clear();
        taDescripcion.clear();
        tfFechaInicio.clear();
        tfFechaFinalizacion.clear();
        cmbProducto.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void cargarForm(){
        Promocion pr = (Promocion)tblPromociones.getSelectionModel().getSelectedItem();
        Date fechaI = pr.getFechaInicio();
        Date fechaF = pr.getFechaFinalizacion();
        if(pr != null){
            tfPromocionId.setText(Integer.toString(pr.getPromocionId()));
            tfPrecio.setText(Double.toString(pr.getPrecioPromocion()));
            taDescripcion.setText(pr.getDescripcionPromocion());
            tfFechaInicio.setText(fechaI.toString());
            tfFechaFinalizacion.setText(fechaF.toString());
            cmbProducto.getSelectionModel().select(obtenerIndexProducto());
        }
    }
    
    public int obtenerIndexProducto(){
        int index = 0;
        String productoTbl = ((Promocion)tblPromociones.getSelectionModel().getSelectedItem()).getProducto();
        for(int i = 0 ; i <= cmbProducto.getItems().size() ; i++){
            String productoCmb = cmbProducto.getItems().get(i).toString();
            
            if(productoTbl.equals(productoCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public ObservableList<Promocion> listarPromociones(){
        ArrayList<Promocion> promociones = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarPromociones()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                Date fechaInicio = resultSet.getDate("fechaInicio");
                Date fechaFinalizacion = resultSet.getDate("fechaFinalizacion");
                String producto = resultSet.getString("producto");
                
                promociones.add(new Promocion(promocionId, precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, producto));
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
        
        return FXCollections.observableList(promociones);
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
    
    public void agregarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarPromocion(?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(tfPrecio.getText()));
            statement.setString(2, taDescripcion.getText());
            statement.setDate(3, Date.valueOf(tfFechaInicio.getText()));
            statement.setDate(4, Date.valueOf(tfFechaFinalizacion.getText()));
            statement.setInt(5, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public void editarPromocion(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarFactura(?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfPromocionId.getText()));
            statement.setDouble(2, Double.parseDouble(tfPrecio.getText()));
            statement.setString(3, taDescripcion.getText());
            statement.setDate(4, Date.valueOf(tfFechaInicio.getText()));
            statement.setDate(5, Date.valueOf(tfFechaFinalizacion.getText()));
            statement.setInt(6, ((Producto)cmbProducto.getSelectionModel().getSelectedItem()).getProductoId());
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
    
    public void eliminarPromocion(int promId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarPromocion(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, promId);
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
    
    public Promocion buscarPromocion(){
        Promocion promocion = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarPromocion(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfPromocionBuscarId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int promocionId = resultSet.getInt("promocionId");
                double precioPromocion = resultSet.getDouble("precioPromocion");
                String descripcionPromocion = resultSet.getString("descripcionPromocion");
                Date fechaInicio = resultSet.getDate("fechaInicio");
                Date fechaFinalizacion = resultSet.getDate("fechaFinalizacion");
                String productoId = resultSet.getString("productoId");
                
                promocion = new Promocion(promocionId, precioPromocion, descripcionPromocion, fechaInicio, fechaFinalizacion, productoId);
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
     
        return promocion;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
