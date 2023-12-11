/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.JTableHeader;
import model.Empleado;
import model.Recuperacion;
import model.Rol;
import model.TableJPA;
import model.Usb;
import model.md5;
import persistent.Control;
import persistent.exceptions.NonexistentEntityException;
import view.administracion.RecuperacionView;
/**
 *
 * @author metallica
 */
public class RecuperacionController{
    RecuperacionView view;
    Control control=new Control();
    TableJPA<Recuperacion>modelTable;
    String atrib[]={"duenioName","nombre"};
    String column[]={"Dueño","Dispositivo"};
    Boolean edit[]={false,false};
    DefaultComboBoxModel modelEmpleado;
    DefaultComboBoxModel<Usb> modelUsb;

    public RecuperacionController(RecuperacionView v) {
        this.view=v;
        modelTable=new TableJPA(column,atrib,edit);
        modelTable.loadMethod(Recuperacion.class);
        view.tbLlave.setModel(modelTable);
        List<Empleado>l=control.empleado.findEmpleadoEntities(true);
        modelEmpleado=new DefaultComboBoxModel();
        view.cbDueño.setModel(modelEmpleado);
        for(Empleado i : l){
            if(i.getRol()==Rol.gerente){
                modelEmpleado.addElement(i);
            }
        }
        loadData();
        initAction();
        view.btnActualizar.doClick();
        JTableHeader t=view.tbLlave.getTableHeader();
        t.setBackground(new Color(19, 19, 19));
        t.setForeground(Color.white);
    }
    
    public void loadData(){
        modelTable.setData(control.recuperacion.findRecuperacionEntities());
        view.btnActualizar.doClick();
        System.out.println("se cargo correctamente");
    }
    
    public void initAction(){
        view.btnCrearLlave.addActionListener((ae) -> {
            if(view.cbDueño.getSelectedItem()!=null && view.cbDispositivo.getSelectedItem()!=null){
                Usb u=(Usb)view.cbDispositivo.getSelectedItem();
                Recuperacion r=new Recuperacion();
                r.setDuenio((Empleado)view.cbDueño.getSelectedItem());
                r.setNombre(u.getLabel());
                r.setLlave(md5.getMD5Hash(u.getUUID()));
                try {
                    control.recuperacion.create(r);
                    System.out.println("se creo correctamente");
                    loadData();
                } catch (Exception ex) {
                    Logger.getLogger(RecuperacionController.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("este fue el error: "+ex);
                }
            }
        });
        
        view.btnActualizar.addActionListener((ae) -> {
            List l=new LinkedList();
            l.add(1);
            l.add(2);
            Vector lv=new Vector(Usb.getUSBDevices());
            System.out.println(lv.size());
            modelUsb=new DefaultComboBoxModel(lv);
            view.cbDispositivo.setModel(modelUsb);
        });
        
        view.btnEliminarLlave.addActionListener((ae) -> {
            if(view.tbLlave.getSelectedRow()!=-1){
                Recuperacion r=modelTable.getObject(view.tbLlave.getSelectedRow());
                try {
                    control.recuperacion.destroy(r.getId());
                    loadData();
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(RecuperacionController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
}
