/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.Distribucion;
import model.Empleado;
import model.ProveedorDistribuidor;
import model.TableJPA;
import model.db;
import persistent.Control;
import view.SalidaView;

/**
 *
 * @author metallica
 */
public class SalidaController {
    
    Control control=new Control();
    SalidaView view;
    TableJPA<Distribucion>modelDis;
    DefaultComboBoxModel<Empleado>modelEmpleado;
    String colum[]={"Cod","Nombre","Fecha","Descripcion"};
    String atrib[]={"id","DestinoName","fecha","descripcion"};
    Boolean edit[]={false,false,false,false};
    
    public SalidaController(SalidaView v){
        this.view=v;
        view.dtA.setDate(new Date());
        view.dtB.setDate(new Date());
        loadData();
        initAction();
        view.btnRango.doClick();
    }
    
    
    public void initAction(){
        
        view.btnBuscar.addActionListener((ae) -> {
            Map<String,Object>parameter=new HashMap<>();
            String initSQL="SELECT u.* FROM DISTRIBUCION u WHERE ";
            boolean v[]={false,false,false};
            if(!view.txtCod.getText().equals("")){
                initSQL+="u.ID = ?c ";
                v[0]=true;
                parameter.put("c", Integer.valueOf(view.txtCod.getText()));
            }
            if(!view.txtNIT.getText().equals("")){
                if(v[0]) initSQL+="AND ";
                initSQL+="u.DESTINO = ?p ";
                v[1]=true;
                ProveedorDistribuidor p=control.proveedorDis.findProveedorDistribuidor(Integer.valueOf(view.txtNIT.getText()));
                parameter.put("p", p.getCod());
            }
            if(view.dtFecha.getDate()!=null){
                if(v[1] || v[0]) initSQL+="AND ";
                v[2]=true;
                initSQL+="DATE( u.FECHA ) = DATE( ?f )";
                Long lg=view.dtFecha.getDate().getTime();
                java.sql.Date date=new java.sql.Date(lg);
                parameter.put("f", date);
            }
            if(((Empleado)view.cbEncargado.getSelectedItem()).getCi()!=null){
                if(v[0] || v[1] || v[2]) initSQL+="AND ";
                initSQL+="u.ENCARGADO = ?e";
                parameter.put("e",((Empleado) view.cbEncargado.getSelectedItem()).getCi());
            }
            List<Distribucion>ll=control.distribucion.QuerySQL(initSQL,parameter);
            modelDis.setData(ll);
            view.txtTotalRes.setText(modelDis.getRowCount()+"");
        });
        
        view.btnRango.addActionListener((ae) -> {
            if(view.dtA.getDate()==null || view.dtB.getDate()==null){
                JOptionPane.showMessageDialog(view, "Campos Vacios, intente nuevamente");
                return;
            }
            Long da=view.dtA.getDate().getTime();
            Long db=view.dtB.getDate().getTime();
            java.sql.Date a=new java.sql.Date(da);
            java.sql.Date b=new java.sql.Date(db);
            List<Distribucion>l=control.distribucion.findDistribucionDate(a, b);
            view.txtTotalRes.setText(l.size()+"");
            modelDis.setData(l);
        });
        
        view.tbEntrada.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Integer id=((Distribucion)modelDis.getObject(view.tbEntrada.getSelectedRow())).getId();
                HashMap<String,Object> p=new HashMap<>();
                System.out.println(id);
                p.put("COD_DIS", id);
                InputStream logo=getClass().getResourceAsStream("/Asset/logoK12-black.png");
                p.put("Logo", logo);
                new report.JasperReportController("OrdenSalidaReport",db.getConection()).getReport(p);
            }

            @Override
            public void mousePressed(MouseEvent me) {
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {
            }
        });
        
    }
    
    public void loadData(){
        modelDis=new TableJPA(colum,atrib,edit);
        modelDis.loadMethod(Distribucion.class);
        view.tbEntrada.setModel(modelDis);
        
        modelEmpleado=new DefaultComboBoxModel();
        modelEmpleado.addElement(new Empleado(null,"","Ninguno",null));
        modelEmpleado.addAll(modelEmpleado.getSize(),(Vector) control.empleado.findEmpleadoEntities());
        view.cbEncargado.setModel(modelEmpleado);
    }
    
}
