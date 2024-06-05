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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.dto.FacturaDTO;
import org.diegomonterroso.model.Factura;
import org.diegomonterroso.report.GenerarReporte;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuFacturaController implements Initializable {
    private Main stage;
    
    private int op;
    
    private Connection conexion = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
               
    @FXML
    TableView tblFacturas;
    
    @FXML
    TextField tfFacturaId;
    
    @FXML
    TableColumn colFacturaId, colFecha, colHora, colCliente, colEmpleado, colTotal;
    
    @FXML
    Button btnAgregar, btnEditar, btnEliminar, btnDetalleF, btnBuscar, btnRegresar, btnFinalizarFactura;
    
    @FXML
    public void handleButtonAction(ActionEvent event) {
    
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnAgregar){
            stage.formFacturaView(1);
        }else if(event.getSource() == btnEditar){
            FacturaDTO.getFacturaDTO().setFactura((Factura)tblFacturas.getSelectionModel().getSelectedItem());
            stage.formFacturaView(2);
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarFactura(((Factura)tblFacturas.getSelectionModel().getSelectedItem()).getFacturaId());
                cargarDatos();
            }
        }else if (event.getSource() == btnBuscar){
            tblFacturas.getItems().clear();
            if(tfFacturaId.getText().equals("")){
                cargarDatos();
            
            }else{
                op = 3;
                cargarDatos();
            }
        }else if(event.getSource() == btnDetalleF){
            stage.formDetalleFacturaView(1);
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }    
    
    public void cargarDatos(){
        if(op == 3){
            tblFacturas.getItems().add(buscarFactura());
            op = 0;
        }else{
        tblFacturas.setItems(listarFacturas());
            colFacturaId.setCellValueFactory(new PropertyValueFactory<Factura, Integer>("facturaId"));
            colFecha.setCellValueFactory(new PropertyValueFactory<Factura, Date>("fecha"));
            colHora.setCellValueFactory(new PropertyValueFactory<Factura, Time>("hora"));
            colCliente.setCellValueFactory(new PropertyValueFactory<Factura, String>("cliente"));
            colEmpleado.setCellValueFactory(new PropertyValueFactory<Factura, String>("empleado"));
            colTotal.setCellValueFactory(new PropertyValueFactory<Factura, Double>("total"));
        }
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
    
    public void eliminarFactura(int facId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarFactura(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, facId);
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
    
    public Factura buscarFactura(){
        Factura factura = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarFactura(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfFacturaId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int facturaId = resultSet.getInt("facturaId");
                Date fecha = resultSet.getDate("fecha");
                Time hora = resultSet.getTime("hora");
                String clienteId = resultSet.getString("clienteId");
                String empleadoId = resultSet.getString("empleadoId");
                double total = resultSet.getDouble("total");
                
                factura = new Factura(facturaId, fecha, hora, clienteId, empleadoId, total);
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
     
        return factura;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
