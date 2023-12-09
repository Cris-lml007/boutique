/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import model.Estado;
import model.HistorialItem;
import model.TableJPA;
import model.db;
import persistent.Control;
import report.JasperReportController;
import view.administracion.HistorialCambiosView;

/**
 *
 * @author metallica
 */
public class HistorialCambiosController {
    Control control=new Control();
    TableJPA<HistorialItem>modelTable;
    HistorialCambiosView view;
    String column[]={"Producto","Empleado","Accion","Fecha"};
    String atrib[]={"itemCod","empleadoCI","accionName","fecha"};
    Boolean edit[]={false,false,false,false};
    DefaultComboBoxModel<Estado>modelEstado;
    Map<String,Object> parameters;

    public HistorialCambiosController(HistorialCambiosView v) {
        this.view=v;
        modelTable=new TableJPA(column,atrib,edit);
        modelTable.loadMethod(HistorialItem.class);
        view.tbHistorial.setModel(modelTable);
        view.dtFechaA.setDate(new Date());
        view.dtFechaB.setDate(new Date());
        initAction();
        modelEstado=new DefaultComboBoxModel(new Object[]{"Todos",Estado.CREADO,Estado.MODIFICADO,Estado.ELIMINADO});
        view.cbAccion.setModel(modelEstado);
        view.btnBuscarR.doClick();
    }
    
    public void initAction(){
        view.btnBuscarR.addActionListener((ae) -> {
            Long da=view.dtFechaA.getDate().getTime();
            Long db=view.dtFechaB.getDate().getTime();
            java.sql.Date dta=new java.sql.Date(da);
            java.sql.Date dtb=new java.sql.Date(db);
            List<HistorialItem> l=control.historialItem.findHistorialDate(dta, dtb);
            modelTable.setData(l);
            parameters=new HashMap();
            parameters.put("CI",LoginController.getCurrentEmpleado().getCi());
            parameters.put("dtA", dta);
            parameters.put("dtB", dtb);
        });
        
        view.btnBuscar.addActionListener((ae) -> {
            parameters=new HashMap();
            parameters.put("CI",LoginController.getCurrentEmpleado().getCi());
            Map<String,Object>parameter=new HashMap<>();
            String initSQL="SELECT u.* FROM HISTORIAL_ITEM u WHERE ";
            boolean v[]={false,false,false};
            if(!view.txtProducto.getText().equals("")){
                initSQL+="u.ITEM = ?c ";
                v[0]=true;
                parameter.put("c", Integer.valueOf(view.txtProducto.getText()));
                parameters.put("Item", Integer.valueOf(view.txtProducto.getText()));
            }
            if(!view.txtEmpleado.getText().equals("")){
                if(v[0]) initSQL+="AND ";
                initSQL+="u.EMPLEADO = ?p ";
                v[1]=true;
                parameter.put("p", view.txtEmpleado.getText());
                parameters.put("Emp", Integer.valueOf(view.txtEmpleado.getText()));
            }
            if(view.dtFecha.getDate()!=null){
                if(v[1] || v[0]) initSQL+="AND ";
                v[2]=true;
                initSQL+="DATE( u.FECHA ) = DATE( ?f ) ";
                Long lg=view.dtFecha.getDate().getTime();
                java.sql.Date date=new java.sql.Date(lg);
                parameter.put("f", date);
                parameters.put("dtA", date);
                parameters.put("dtB", date);
            }
            if(view.cbAccion.getSelectedIndex()>0){
                if(v[0] || v[1] || v[2]) initSQL+="AND ";
                initSQL+="ACCION = ?a ";
                parameter.put("a", (Estado) modelEstado.getSelectedItem());
                parameters.put("Accion",view.cbAccion.getSelectedItem());
            }
            List<HistorialItem>l=control.historialItem.QuerySQL(initSQL, parameter);
            modelTable.setData(l);
        });
        
        view.btnLimpiar.addActionListener((ae) -> {
            view.txtEmpleado.setText(null);
            view.txtProducto.setText(null);
            view.dtFecha.setDate(null);
            view.cbAccion.setSelectedIndex(0);
        });
        
        view.btnExportar.addActionListener((ae) -> {
            parameters.put("Logo", DashboardController.getLogo());
            JasperReportController report=new JasperReportController("HistorialCambios", db.getConection());
            report.getReport(parameters);
        });
    }
}
