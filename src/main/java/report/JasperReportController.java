/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
import controller.DashboardController;
import javax.swing.UIManager;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import model.db;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import view.BuscarItemView;

/**
 *
 * @author metallica
 */
public class JasperReportController {
    JasperReport report;
    String path="/Report/";
    InputStream input;
    Connection conection;
    String title="Imprimir";
    
    public JasperReportController(String nameReport,Connection c) {
        this.path+=nameReport+".jasper";
        conection=c;
    }
    
    public void setTitle(String text){
        this.title=text;
    }
    
    public void getReport(Map<String,Object>parameters){
        input=getClass().getResourceAsStream(path);
        try{
            conection=db.getConection();
            report=(JasperReport) JRLoader.loadObject(input);
            JasperPrint jprint=JasperFillManager.fillReport(report,parameters,conection);
            //JasperViewer jview=new JasperViewer(jprint,false);
            JRViewer viewer=new JRViewer(jprint);
            viewer.setZoomRatio((float) 0.8);
            JDialog dialog=new JDialog(DashboardController.getFrame(),title,true);
            dialog.setSize(800,600);
            dialog.setLocationRelativeTo(DashboardController.getFrame());
            dialog.add(viewer);
            dialog.setVisible(true);
            
            //jview.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            //jview.setVisible(true);
        }catch(JRException e){
            System.out.println("error de reporte: "+e);
        }        
    }
}
