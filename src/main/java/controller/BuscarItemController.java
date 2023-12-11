/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.table.JTableHeader;
import model.Item;
import model.TableJPA;
import model.TipoItem;
import persistent.Control;
import view.BuscarItemView;

/**
 *
 * @author metallica
 */
public class BuscarItemController {
    
    BuscarItemView view;
    JFrame f;
    Control control=new Control();
    TableJPA<Item>modelItem;
    DefaultComboBoxModel<TipoItem>modelTipo;
    String columns[]={"Cod","Nombre","Tipo"};
    String atribs[]={"cod","nombre","tipoName"};
    public static Item itemBuscado;
    
    public BuscarItemController(BuscarItemView v,JFrame f){
        this.f=f;
        view=v;
        modelTipo=new DefaultComboBoxModel<>();
        modelTipo.addElement(TipoItem.sinTipo);
        modelTipo.addElement(TipoItem.mercaderia);
        modelTipo.addElement(TipoItem.materiaPrima);
        view.cbTipo.setModel(modelTipo);
        loadData(control.item.findItemEntities(true));
        initAction();
        JTableHeader t=view.tbItem.getTableHeader();
        t.setBackground(new Color(25, 25, 25));
        t.setForeground(Color.white);
    }
    
    public void initAction(){
        view.btnBuscar.addActionListener((ae -> {
            if(view.txtNombre.getText().equals("")){
                loadData(control.item.findItemEntities(true));
            }else{
                String buscar="%"+view.txtNombre.getText().toUpperCase()+"%";
                TipoItem i = (TipoItem) view.cbTipo.getSelectedItem();
                List<Item> l=control.item.findItemEntities(buscar, i);
                loadData(l);
            }
        }));
        
        view.btnSeleccionar.addActionListener((ae -> {
            itemBuscado=modelItem.getObject(view.tbItem.getSelectedRow());
            f.dispose();
        }));
    }
    
    public static Item getItem(){
        return itemBuscado;
    }
    
    public void loadData(List<Item>l){
        modelItem=new TableJPA(l,columns,atribs,new Boolean[]{false,false,false},Item.class);
        view.tbItem.setModel(modelItem);
    }
    
}
