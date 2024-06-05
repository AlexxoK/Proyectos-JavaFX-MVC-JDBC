package org.diegomonterroso.controller;

import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.TextField;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.dto.FacturaDTO;
import org.diegomonterroso.model.Cliente;
import org.diegomonterroso.model.Empleado;
import org.diegomonterroso.model.Factura;
import org.diegomonterroso.report.GenerarReporte;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class FormFacturaController implements Initializable {
    private Main stage;
    private int op;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    Button btnGuardar, btnCancelar, btnFinalizarFactura;
   
    @FXML
    TextField tfFacturaId;
   
    @FXML
    ComboBox cmbCliente,cmbEmpleado;
   
    @FXML
    public void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btnCancelar) {
            FacturaDTO.getFacturaDTO().setFactura(null);
            stage.menuFacturaView();
        }else if (event.getSource() == btnGuardar) {
            if (op == 1) {
                agregarFactura();
                SuperKinalAlert.getInstance().mostrarAlertaInformacion(400);
                stage.menuFacturaView();
        }else if (op == 2) {
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(505).get() == ButtonType.OK) {
                editarFactura();
                FacturaDTO.getFacturaDTO().setFactura(null);
                SuperKinalAlert.getInstance().mostrarAlertaInformacion(500);
                stage.menuFacturaView();
                }else{
                    stage.menuFacturaView();
                }
            }
        }else if(event.getSource() == btnFinalizarFactura){
            GenerarReporte.getInstance().generarFactura(Integer.parseInt(tfFacturaId.getText()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(FacturaDTO.getFacturaDTO().getFactura() != null){
            cargarDatos(FacturaDTO.getFacturaDTO().getFactura());
        }
        cmbCliente.setItems(listarClientes());
        cmbEmpleado.setItems(listarEmpleados());
    }      
    
    public void cargarDatos(Factura factura){
        tfFacturaId.setText(Integer.toString(factura.getFacturaId()));
    }
    
    public void agregarFactura(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarFactura(?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1,((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(2,((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
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
    
    public void editarFactura(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_EditarFactura(?,?,?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfFacturaId.getText()));
            statement.setInt(2,((Cliente)cmbCliente.getSelectionModel().getSelectedItem()).getClienteId());
            statement.setInt(3,((Empleado)cmbEmpleado.getSelectionModel().getSelectedItem()).getEmpleadoId());
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
    
    public ObservableList<Cliente> listarClientes(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_ListarClientes()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nombre = resultSet.getString("nombre");  
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String nit = resultSet.getString("nit");
                String direccion = resultSet.getString("direccion");
                
                clientes.add(new Cliente(clienteId, nombre, apellido, telefono, nit, direccion));
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
        
        return FXCollections.observableList(clientes);
    }
    
    public ObservableList<Empleado> listarEmpleados(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarEmpleados()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int empleadoId = resultSet.getInt("empleadoId");
                String nombreEmpleado = resultSet.getString("nombreEmpleado");
                String apellidoEmpleado = resultSet.getString("apellidoEmpleado");
                double sueldo = resultSet.getDouble("sueldo");
                Time horaEntrada = resultSet.getTime("horaEntrada");
                Time horaSalida = resultSet.getTime("horaSalida");
                String cargo = resultSet.getString("cargo");
                String encargado = resultSet.getString("encargado");
                
                empleados.add(new Empleado(empleadoId, nombreEmpleado, apellidoEmpleado, sueldo, horaEntrada, horaSalida, cargo, encargado));
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
        return FXCollections.observableList(empleados);
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
