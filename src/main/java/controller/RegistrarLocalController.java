/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Localizacion;
import model.Pais;
import model.TableJPA;
import persistent.Control;
import utility.lockInputType;
import view.RegistrarLocalView;

/**
 *
 * @author metallica
 */

public class RegistrarLocalController {
    public RegistrarLocalView view;
    public Control control=new Control();
    public lockInputType lock=new lockInputType();
    public DefaultComboBoxModel<Pais> modelCbPais;
    String [] columnasPais={"COD","Pais"};
    String [] columnasLocal={"COD","Ciudad","Pais"};
    public TableJPA modelPais;
    public TableJPA modelLocal;
    public Pais pais;
    public Localizacion local;
            
            
    public RegistrarLocalController(RegistrarLocalView v){
        this.view=v;
        lock.lockNumber(view.txtCodPais);
        lock.lockSymbol(view.txtCodPais);
        lock.lockNumber(view.txtPais);
        lock.lockSymbol(view.txtPais);
        
        lock.lockNumber(view.txtCodLocal);
        lock.lockSymbol(view.txtCodLocal);
        lock.lockNumber(view.txtCiudad);
        lock.lockSymbol(view.txtCiudad);
        loadData();
        initAction();
    }
    
    public void initAction(){
        
        view.btnLimpiarP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                view.txtCodPais.setText(null);
                view.txtPais.setText(null);
                pais=null;
                view.tbPais.clearSelection();
            }
        });
        
        view.btnLimpiarL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                view.txtCiudad.setText(null);
                view.txtCodLocal.setText(null);
                local=null;
                view.tbLocal.clearSelection();
            }
        });
        view.tbPais.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int i=view.tbPais.getSelectedRow();
                pais=(Pais) modelPais.getObject(i);
                view.txtCodPais.setText(pais.getCod());
                view.txtPais.setText(pais.getNombre());
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
        
        view.tbLocal.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int i=view.tbLocal.getSelectedRow();
                local=(Localizacion)modelLocal.getObject(i);
                view.txtCiudad.setText(local.getCiudad());
                view.txtCodLocal.setText(local.getCod());
                view.cbPais.setSelectedItem(local.getPais());
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
        
        view.btnGuardarPais.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(view.txtCodPais.getText()!=null && view.txtPais.getText()!=null){
                    try{
                        if (pais==null){
                            pais=new Pais(view.txtCodPais.getText().toUpperCase(),view.txtPais.getText().toUpperCase());
                            control.pais.create(pais);
                        }else{
                            pais.setCod(view.txtCodPais.getText().toUpperCase());
                            pais.setNombre(view.txtPais.getText().toUpperCase());
                            control.pais.edit(pais);
                        }
                        view.txtCodPais.setText(null);
                        view.txtPais.setText(null);
                        pais=null;
                        loadData();
                    }catch(Exception e){
                        System.out.println("hubo un error: "+e);
                    }
                }
            }
        });
        
        view.btnGuardarLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(view.txtCodLocal.getText()!=null && view.txtCiudad.getText()!=null){
                    try{
                        if(local==null){
                            local=new Localizacion(
                            view.txtCodLocal.getText().toUpperCase(),
                            view.txtCiudad.getText().toUpperCase(),
                            (Pais)view.cbPais.getSelectedItem()
                            );
                            control.localizacion.create(local);
                        }else{
                            local.setCiudad(view.txtCiudad.getText().toUpperCase());
                            local.setCod(view.txtCodLocal.getText().toUpperCase());
                            local.setPais((Pais)view.cbPais.getSelectedItem());
                            control.localizacion.edit(local);
                        }
                        view.txtCiudad.setText(null);
                        view.txtCodLocal.setText(null);
                        local=null;
                        loadData();
                    }catch(Exception e){
                        System.out.println("hubo un error: "+e);
                    }
                }
            }
        });
        
        view.btnEliminarPais.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(pais!=null){
                    try{
                        if(JOptionPane.showConfirmDialog(view, "Desea borrar este registro?")==0){
                            control.pais.destroy(pais.getCod());
                            view.txtCodPais.setText(null);
                            view.txtPais.setText(null);
                            pais=null;
                            loadData();
                        }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(view, "Hubo un error: "+e);
                    }
                    
                }
            }
        });
        
        view.btnEliminarLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try{
                    if(JOptionPane.showConfirmDialog(view, "Desea borrar este registro?")==0){
                        control.localizacion.destroy(local.getCod());
                        view.txtCiudad.setText(null);
                        view.txtCodLocal.setText(null);
                        local=null;
                        loadData();
                    }
                }catch(Exception e){
                    JOptionPane.showMessageDialog(view, "Hubo un error: "+e);
                }
            }
        });
    }
    
    public void loadData(){
        List<Pais> lp=control.pais.findPaisEntities();
        List<Localizacion> ll=control.localizacion.findLocalizacionEntities();
        modelCbPais=new DefaultComboBoxModel((Vector) lp);
        view.cbPais.setModel(modelCbPais);
        
        
        modelPais=new TableJPA(lp, columnasPais, new Boolean[]{false,false});
        view.tbPais.setModel(modelPais);
        
        modelLocal=new TableJPA(ll, columnasLocal, new Boolean[]{false,false,false});
        view.tbLocal.setModel(modelLocal);
    }
}
