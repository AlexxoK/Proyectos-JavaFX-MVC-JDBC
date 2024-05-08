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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.model.Cargo;
import org.diegomonterroso.model.Empleado;
import org.diegomonterroso.model.Encargado;
import org.diegomonterroso.system.Main;

public class MenuEmpleadoController implements Initializable {
    Main stage;
    
    private Connection conexion = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    
    @FXML
    TextField tfEmpleadoId, tfNombre, tfApellido, tfSueldo, tfHoraEntrada, tfHoraSalida;
        
    @FXML
    ComboBox cmbCargo, cmbEncargado;
    
    @FXML
    TableView tblEmpleados;
    
    @FXML
    TableColumn colEmpleadoId, colNombre, colApellido, colSueldo, colHoraEntrada, colHoraSalida, colCargo, colEncargadoId;
    
    @FXML
    Button btnGuardar, btnVaciar, btnRegresar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnRegresar){
            stage.menuPrincipalView();
        }else if(event.getSource() == btnGuardar){
            if(tfEmpleadoId.getText().equals("")){
                agregarEmpleado();
                cargarDatos();
            }else{
                editarEmpleado();
                cargarDatos();
            }
        }else if(event.getSource() == btnVaciar){
            vaciarForm();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCargo.setItems(listarCargos());
    }    
    
    public void cargarDatos(){
        tblEmpleados.setItems(listarEmpleados());
        colEmpleadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("empleadoId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpleado"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("sueldo"));
        colHoraEntrada.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaEntrada"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<Empleado, Time>("horaSalida"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleado, String>("cargo"));
        colEncargadoId.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("encargadoId"));
    }
    
    public void vaciarForm(){
        tfEmpleadoId.clear();
        tfNombre.clear();
        tfApellido.clear();
        tfSueldo.clear();
        tfHoraEntrada.clear();
        tfHoraSalida.clear();
        cmbCargo.getSelectionModel().clearSelection();
        cmbEncargado.getSelectionModel().clearSelection();
    }
    
    @FXML
    public void cargarForm(){
        Empleado e = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
        Time horaEntrada = e.getHoraEntrada();
        Time horaSalida = e.getHoraSalida();
        if(e != null){
            tfEmpleadoId.setText(Integer.toString(e.getEmpleadoId()));
            tfNombre.setText(e.getNombreEmpleado());
            tfApellido.setText(e.getApellidoEmpleado());
            tfSueldo.setText(Double.toString(e.getSueldo()));
            tfHoraEntrada.setText(horaEntrada.toString());
            tfHoraSalida.setText(horaSalida.toString());
            cmbCargo.getSelectionModel().select(obtenerIndexCargo());
            cmbEncargado.getSelectionModel().select(obtenerIndexEncargado());
        }
    }
    
    public int obtenerIndexCargo(){
        int index = 0;
        String cargoTbl = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCargo();
        for(int i = 0 ; i <= cmbCargo.getItems().size() ; i++){
            String cargoCmb = cmbCargo.getItems().get(i).toString();
            
            if(cargoTbl.equals(cargoCmb)){
                index = i;
                break;
            }
        }
        
        return index;
    }
    
    public int obtenerIndexEncargado(){
        int index = 0;
        String encargadoTbl = ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getEncargado();
        for(int i = 0 ; i <= cmbEncargado.getItems().size() ; i++){
            String encargadoCmb = cmbEncargado.getItems().get(i).toString();
            
            if(encargadoTbl.equals(encargadoCmb)){
                index = i;
                break;
            }
        }
        
        return index;
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
    
    public ObservableList<Cargo> listarCargos(){
        ArrayList<Cargo> cargos = new ArrayList<>();
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_listarCargos()";
            statement = conexion.prepareStatement(sql);
            resultSet = statement.executeQuery();
            
            while(resultSet.next()){
                int cargoId = resultSet.getInt("cargoId");
                String nombreCargo = resultSet.getString("nombreCargo");  
                String descripcionCargo = resultSet.getString("descripcionCargo");
                
                cargos.add(new Cargo(cargoId, nombreCargo, descripcionCargo));
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
        
        return FXCollections.observableList(cargos);
    }
    
    public void agregarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_agregarEmpleado(?, ?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfNombre.getText());
            statement.setString(2, tfApellido.getText());
            statement.setString(3, tfSueldo.getText());
            statement.setTime(4, Time.valueOf(tfHoraEntrada.getText()));
            statement.setTime(5, Time.valueOf(tfHoraSalida.getText()));
            statement.setInt(6, ((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCargoId());
            statement.setInt(6, ((Encargado)cmbEncargado.getSelectionModel().getSelectedItem()).getEncargadoId());
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
    
    public void editarEmpleado(){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_editarEmpleado(?, ?, ?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(tfEmpleadoId.getText()));
            statement.setString(2, tfNombre.getText());
            statement.setString(3, tfApellido.getText());
            statement.setString(4, tfSueldo.getText());
            statement.setTime(5, Time.valueOf(tfHoraEntrada.getText()));
            statement.setTime(6, Time.valueOf(tfHoraSalida.getText()));
            statement.setInt(7, ((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCargoId());
            statement.setInt(8, ((Encargado)cmbEncargado.getSelectionModel().getSelectedItem()).getEncargadoId());
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

    public Main getStage() {
        return stage;
    }

    public void setStage(Main stage) {
        this.stage = stage;
    }
    
}
