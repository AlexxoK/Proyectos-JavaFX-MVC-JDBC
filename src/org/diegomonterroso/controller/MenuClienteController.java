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
import org.diegomonterroso.dto.ClienteDTO;
import org.diegomonterroso.model.Cliente;
import org.diegomonterroso.report.GenerarReporte;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuClienteController implements Initializable {
    private Main stage;
    
    private int op;
    
    private static Connection conexion;
    private static PreparedStatement statement;
    private static ResultSet resultSet;
    
    @FXML
    TableView tblClientes;
    
    @FXML
    TableColumn colClienteId, colNombre, colApellido, colTelefono, colNit, colDireccion;
    
    @FXML
    Button btnRegresar, btnAgregar, btnEditar, btnEliminar, btnBuscar, btnVerCliente;
    
    @FXML
    TextField tfClienteId;
    
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnAgregar){
            stage.formClienteView(1);
        }else if(event.getSource() == btnEditar){
            ClienteDTO.getClienteDTO().setCliente((Cliente)tblClientes.getSelectionModel().getSelectedItem());
            stage.formClienteView(2);
        }else if(event.getSource() == btnEliminar){
            if(SuperKinalAlert.getInstance().mostrarAlertaConfirmacion(404).get() == ButtonType.OK){
                eliminarCliente(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getClienteId());
                cargarDatos();
            }
        }else if(event.getSource() == btnBuscar){
            tblClientes.getItems().clear();
            
            if(tfClienteId.getText().equals("")){
                cargarDatos();
            }else{
                op = 3;
                cargarDatos();
            }
        }else if(event.getSource() == btnVerCliente){
                GenerarReporte.getInstance().generarCliente(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getClienteId());
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        if(op == 3){
            tblClientes.getItems().add(buscarCliente());
            op = 0;
        }else{
            tblClientes.setItems(listarClientes());
            colClienteId.setCellValueFactory(new PropertyValueFactory<Cliente, Integer>("clienteId"));
            colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
            colApellido.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellido"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, String>("telefono"));
            colNit.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nit"));
            colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
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
    
    public void eliminarCliente(int cliId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_eliminarCliente(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, cliId);
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
    
    public Cliente buscarCliente(){
        Cliente cliente = null;
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarCliente(?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfClienteId.getText()));
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int clienteId = resultSet.getInt("clienteId");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String nit = resultSet.getString("nit");
                String direccion = resultSet.getString("direccion");
                
                cliente = new Cliente(clienteId, nombre, apellido, telefono, nit, direccion);
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
     
        return cliente;
    }

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
    
}
