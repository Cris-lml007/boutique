/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import model.Localizacion;
import model.ProveedorDistribuidor;
import model.TableJPA;
import model.Tipo;
import persistent.Control;
import view.RegistrarLocalView;
import view.RegistrarPDView;

/**
 *
 * @author metallica
 */
public class RegistrarPDController {

    public RegistrarPDView view;
    public ProveedorDistribuidor pd;
    public Control control=new Control();
    public TableJPA model;
    public List<Localizacion> listLocal;
    public DefaultComboBoxModel modelCbLocal;
    public JTextField editComponent;
    
    public RegistrarPDController(RegistrarPDView v){
        this.view=v;
        view.cbTipo.addItem(Tipo.proveedor);
        view.cbTipo.addItem(Tipo.planta);
        view.cbTipo.addItem(Tipo.local);
        view.cbTipo.addItem(Tipo.otros);
        editComponent=(JTextField) view.cpLocal.getEditor().getEditorComponent();
        loadData();
        initAction();
    }
    
    public final void initAction(){
        
        editComponent.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar()=='\n'){
                    existLocalizacion();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
         
            }

            @Override
            public void keyReleased(KeyEvent ke) {
         
            }
        });
        
        editComponent.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                editComponent.selectAll();
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
        
        view.tbPD.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                pd=(ProveedorDistribuidor)model.getObject(view.tbPD.getSelectedRow());
                view.txtNIT.setText(pd.getCod().toString());
                view.txtNombre.setText(pd.getNombre());
                //view.txtLocalidad.setText(pd.getOrigen().getCiudad());
                view.cpLocal.setSelectedItem(pd.getOrigen());
                view.cbTipo.setSelectedItem(pd.getTipo());
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
        
        
        view.btnLimpiar.addActionListener((ActionEvent ae) -> {
            pd=null;
            view.txtNIT.setText(null);
            view.txtNombre.setText(null);
            view.cpLocal.setSelectedItem(null);
            view.cbTipo.setSelectedItem(null);
        });
        
        view.btnGuardarPD.addActionListener((ActionEvent ae) -> {     
            try{
                if(existLocalizacion()){
                    boolean verify=pd!=null;
                    pd= pd==null ? new ProveedorDistribuidor() : pd;
                    pd.setCod(Integer.valueOf(view.txtNIT.getText()));
                    pd.setNombre(view.txtNombre.getText());
                    pd.setOrigen((Localizacion)view.cpLocal.getSelectedItem());
                    pd.setTipo((Tipo) view.cbTipo.getSelectedItem());
                    if(verify) control.proveedorDis.edit(pd);
                    else control.proveedorDis.create(pd);
                    loadData();
                    view.btnLimpiar.doClick();
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(view, "No se pudo guardar/Modificar");
            }
        });
        
        view.btnEliminarPD.addActionListener(((ae) -> {
            if(pd!=null && JOptionPane.showConfirmDialog(view, "estas seguro de borrar?")==0){
                try{
                    control.proveedorDis.destroy(pd.getCod());
                    view.btnLimpiar.doClick();
                    loadData();
                }catch(Exception e){
                    JOptionPane.showMessageDialog(view, "no se pudo borrar");
                }
                
            }
        }));
    }
    
    public final void loadData(){
        listLocal=control.localizacion.findLocalizacionEntities();
        List <ProveedorDistribuidor> l=control.proveedorDis.findProveedorDistribuidorEntities();
        model=new TableJPA(l, new String[]{"NIT","Nombre","Origen","Tipo"}, new boolean[]{false,false,false,false});
        view.tbPD.setModel(model);
        
        modelCbLocal=new DefaultComboBoxModel((Vector) listLocal);
        view.cpLocal.setModel(modelCbLocal);
    }
    
    
    public boolean existLocalizacion(){
        for(Localizacion l : listLocal){
            if(l.toString().contains(editComponent.getText().toUpperCase())){
                    view.cpLocal.setSelectedItem(l);
                    return true;
                }
            }
        if((JOptionPane.showConfirmDialog(
                view, "la localizacion de: "+editComponent.getText()+" no existe, Desea crearla?")
                )==0){
            JFrame a=new JFrame("ventana");
            RegistrarLocalView w=new RegistrarLocalView();
            a.add(w);
            new RegistrarLocalController(w);
            a.setSize(870, 650);
            a.setResizable(false);
            a.setVisible(true);
            a.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent we) {
                }

                @Override
                public void windowClosing(WindowEvent we) {
                    a.dispose();
                }

                @Override
                public void windowClosed(WindowEvent we) {
                    loadData();
                }

                @Override
                public void windowIconified(WindowEvent we) {
                }

                @Override
                public void windowDeiconified(WindowEvent we) {
                }

                @Override
                public void windowActivated(WindowEvent we) {
                }

                @Override
                public void windowDeactivated(WindowEvent we) {
                }
            });
        }
        return false;
    }
    
}
