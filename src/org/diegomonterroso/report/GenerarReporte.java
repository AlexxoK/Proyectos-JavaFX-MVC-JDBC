package org.diegomonterroso.report;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.diegomonterroso.dao.Conexion;
import win.zqxu.jrviewer.JRViewerFX;

public class GenerarReporte {
    private static GenerarReporte instance;

    private static Connection conexion = null;
    
    private GenerarReporte() {
    }
    
    public static GenerarReporte getInstance() {
        if(instance == null){
            instance = new GenerarReporte();
        }
        return instance;
    }
    
    public void generarFactura(int facId){
        try{
            // Paso 1: Abrir la conexión a la base de datos
            conexion = Conexion.getInstance().obtenerConexion();
            // Paso 2: Enviar los parametros al reporte por medio de un map
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("facId", facId);
            // Paso 3: Crear un stage nuevo
            Stage reportStage = new Stage();
            // Paso 4: Generar el reporte
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReporte.class.getResourceAsStream("/org/diegomonterroso/report/Factura.jasper"), parametros, conexion);
            // Paso 5: Montar el reporte en el stage
            JRViewerFX reportView = new JRViewerFX(reporte);
            // Paso 6: Mostrar el stage
            Pane root = new Pane();
            root.getChildren().add(reportView);
            
            reportView.setPrefSize(1000, 800);
            
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Factura");
            reportStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarProducto(int proId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("proId", proId);
            
            Stage reportStage = new Stage();
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReporte.class.getResourceAsStream("/org/diegomonterroso/report/Producto.jasper"), parametros, conexion);
            JRViewerFX reportView = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            root.getChildren().add(reportView);
            reportView.setPrefSize(1000, 800);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Producto");
            reportStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void generarCliente(int cliId){
        try{
            conexion = Conexion.getInstance().obtenerConexion();
            
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("cliId", cliId);
            
            Stage reportStage = new Stage();
            JasperPrint reporte = JasperFillManager.fillReport(GenerarReporte.class.getResourceAsStream("/org/diegomonterroso/report/Cliente.jasper"), parametros, conexion);
            JRViewerFX reportView = new JRViewerFX(reporte);
            
            Pane root = new Pane();
            root.getChildren().add(reportView);
            reportView.setPrefSize(1000, 800);
            Scene scene = new Scene(root);
            reportStage.setScene(scene);
            reportStage.setTitle("Cliente");
            reportStage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
