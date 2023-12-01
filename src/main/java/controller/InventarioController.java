/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import model.Empleado;
import model.Item;
import model.ProveedorDistribuidor;
import model.TableJPA;
import model.TipoItem;
import persistent.Control;
import view.InventarioView;

/**
 *
 * @author metallica
 */
public class InventarioController {
    
    InventarioView view;
    TableJPA<Item>modelTable;
    DefaultComboBoxModel<TipoItem>modelCb;
    Control control=new Control();
    String column[]={"Cod","Nombre","Cantidad","Tipo"};
    String atrib[]={"cod","nombre","cantidad","tipoName"};
    Boolean edit[]={false,false,false,false};
    String sql;
    
    public InventarioController(InventarioView v){
        this.view=v;
        modelTable=new TableJPA(column,atrib,edit);
        modelTable.loadMethod(Item.class);
        view.tbItem.setModel(modelTable);
        loadData();
        initAction();
        modelCb=new DefaultComboBoxModel(new Object[]{
            TipoItem.Todo,
            TipoItem.materiaPrima,
            TipoItem.mercaderia,
            TipoItem.sinTipo
        });
        view.cbTipo.setModel(modelCb);
    }
    
    public void initAction(){
        view.btnRestablecer.addActionListener((ae) -> {
            loadData();
        });
        
        view.btnBuscar.addActionListener((ae) -> {
            Map<String,Object>parameter=new HashMap<>();
            String initSQL="SELECT u.* FROM ITEM u WHERE u.CANTIDAD > 0 ";
            if(!view.txtCod.getText().equals("")){
                initSQL+="AND ";
                initSQL+="u.COD = ?c ";
                parameter.put("c", Integer.valueOf(view.txtCod.getText()));
            }
            if(!view.txtNombre.getText().equals("")){
                initSQL+="AND ";
                initSQL+="u.NOMBRE = ?p ";
                parameter.put("p", view.txtNombre.getText());
            }
            if(view.cbTipo.getSelectedItem()!=TipoItem.Todo){
                initSQL+="AND ";
                initSQL+="u.TIPO = ?t ";
                TipoItem i=(TipoItem) view.cbTipo.getSelectedItem();
                parameter.put("t", i.ordinal());
            }
            List<Item>l=control.item.QuerySQL(initSQL, parameter);
            modelTable.setData(l);
            sql=initSQL;
        });
        
        view.btnExportar.addActionListener((ae) -> {
            
        });
    }
    
    public void loadData(){
        sql="SELECT u.* FROM ITEM u WHERE u.CANTIDAD > 0 ";
        List<Item>l=control.itemExisting();
        modelTable.setData(l);
    }
}
