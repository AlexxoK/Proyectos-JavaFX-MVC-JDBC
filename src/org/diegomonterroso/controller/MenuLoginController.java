package org.diegomonterroso.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.diegomonterroso.dao.Conexion;
import org.diegomonterroso.model.Usuario;
import org.diegomonterroso.system.Main;
import org.diegomonterroso.utils.PasswordUtils;
import org.diegomonterroso.utils.SuperKinalAlert;

public class MenuLoginController implements Initializable {
    private Main stage;
    
    private int op = 0;
    
    private static Connection conexion = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;
    
    @FXML
    TextField tfUser;

    @FXML
    PasswordField pfPassword;
    
    @FXML
    Button btnIniciar, btnRegistrar;
    
    @FXML
    public void handleButtonAction(ActionEvent event){
        if(event.getSource() == btnIniciar){
            Usuario usuario = buscarUsuario();
            if(op == 0){
                if(usuario != null){
                    if(PasswordUtils.getInstance().checkPassword(pfPassword.getText(), usuario.getContrasenia())){
                        SuperKinalAlert.getInstance().alertaSaludo(usuario.getUser());
                        if(usuario.getNivelAccesoId() == 1){
                            btnRegistrar.setDisable(false);
                            btnIniciar.setText("Ir al Menu");
                            op = 1;
                        }else if(usuario.getNivelAccesoId() == 2){
                            stage.menuPrincipalView();
                        }
                    }else{
                        SuperKinalAlert.getInstance().mostrarAlertaInformacion(005);
                    }
                }else{
                    SuperKinalAlert.getInstance().mostrarAlertaInformacion(602);
                }
            }else{
                stage.menuPrincipalView();
            }
        }else if(event.getSource() == btnRegistrar){
            stage.formUsuarioView();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
    
    public Usuario buscarUsuario(){
        Usuario usuario = null;
        
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            String sql = "call sp_buscarUsuario(?)";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, tfUser.getText());
            resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                int usuarioId = resultSet.getInt("usuarioId");
                String user = resultSet.getString("usuario");
                String contrasenia = resultSet.getString("contrasenia");
                int nivelAccesoId = resultSet.getInt("nivelAccesoId");
                int empleadoId = resultSet.getInt("empleadoId");
                
                usuario = new Usuario(usuarioId, user, contrasenia, nivelAccesoId, empleadoId);
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
        return usuario;
    }
    
    public void setStage(Main stage){
        this.stage = stage;
    }
}
