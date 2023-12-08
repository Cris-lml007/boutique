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
import javax.swing.JOptionPane;
import model.ProveedorDistribuidor;
import model.Subministro;
import model.TableJPA;
import model.db;
import persistent.Control;
import view.EntradaView;

/**
 *
 * @author metallica
 */
public class EntradaController {
    
    Control control=new Control();
    EntradaView view;
    TableJPA<Subministro>modelSub;
    String colum[]={"Cod","Proveedor/Distribuidor","Fecha","Descripcion"};
    String atrib[]={"cod","proveedorName","fecha","descripcion"};
    Boolean edit[]={false,false,false,false};
    
    public EntradaController(EntradaView v){
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
            String initSQL="SELECT u.* FROM SUBMINISTRO u WHERE ";
            boolean v[]={false,false};
            if(!view.txtCod.getText().equals("")){
                initSQL+="u.COD = ?c ";
                v[0]=true;
                parameter.put("c", Integer.valueOf(view.txtCod.getText()));
            }
            if(!view.txtNIT.getText().equals("")){
                if(v[0]) initSQL+="AND ";
                initSQL+="u.PROVEEDOR = ?p ";
                v[1]=true;
                ProveedorDistribuidor p=control.proveedorDis.findProveedorDistribuidor(Integer.valueOf(view.txtNIT.getText()));
                parameter.put("p", p.getCod());
            }
            if(view.dtFecha.getDate()!=null){
                if(v[1] || v[0]) initSQL+="AND ";
                initSQL+="DATE( u.FECHA ) = DATE( ?f )";
                Long lg=view.dtFecha.getDate().getTime();
                java.sql.Date date=new java.sql.Date(lg);
                parameter.put("f", date);
            }
            List<Subministro>ll=control.subministro.QuerySQL(initSQL,parameter);
            modelSub.setData(ll);
            view.txtTotalRes.setText(modelSub.getRowCount()+"");
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
            List<Subministro>l=control.subministro.findSubministroDate(a,b);
            view.txtTotalRes.setText(l.size()+"");
            modelSub.setData(l);
        });
        
        view.tbEntrada.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Integer id=((Subministro)modelSub.getObject(view.tbEntrada.getSelectedRow())).getCod();
                HashMap<String,Object> p=new HashMap<>();
                System.out.println(id);
                p.put("COD_SUB", id);
                //InputStream logo=getClass().getResourceAsStream("/Asset/logoK12-black.png");
                p.put("Logo", getClass().getResourceAsStream("/Asset/logoK12-black.png"));
                new report.JasperReportController("OrdenEntradaReport",db.getConection()).getReport(p);
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
        modelSub=new TableJPA(colum,atrib,edit);
        modelSub.loadMethod(Subministro.class);
        view.tbEntrada.setModel(modelSub);
    }
    
}
