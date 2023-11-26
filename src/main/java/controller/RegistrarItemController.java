/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.Empleado;
import model.Estado;
import model.HistorialItem;
import model.Item;
import model.TableJPA;
import model.TipoItem;
import persistent.Control;
import view.RegistrarItemView;

/**
 *
 * @author metallica
 */
public class RegistrarItemController{
    RegistrarItemView view;
    TableJPA modelTable;
    DefaultComboBoxModel<TipoItem> modelTipo;
    Control control=new Control();
    Item item;
    Empleado currentUser=LoginController.getCurrentEmpleado();
    
    public RegistrarItemController(RegistrarItemView v){
        this.view=v;
        loadData();
        initAction();
    }
    
    public void initAction(){
        view.tbItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                item=(Item)modelTable.getObject(view.tbItem.getSelectedRow());
                view.txtNombre.setText(item.getNombre());
                view.txtDesc.setText(item.getDescripcion());
                view.txtPrecio.setText(item.getPrecio().toString());
                view.cbTipo.setSelectedItem(item.getTipo());
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
        
        view.btnLimpiar.addActionListener(((ae) -> {
            view.txtNombre.setText(null);
            view.txtPrecio.setText(null);
            view.txtDesc.setText(null);
            item=null;
            view.cbTipo.setSelectedItem(TipoItem.sinTipo);
        }));
        
        view.btnGuardar.addActionListener((ae) -> {
            HistorialItem h=new HistorialItem();
            boolean verify=item!=null;
            if(!verify) item=new Item();
            Item copy=Item.newInstance(item);
            item.setNombre(view.txtNombre.getText());
            item.setPrecio(BigDecimal.valueOf(Double.parseDouble(view.txtPrecio.getText())));
            item.setDescripcion(view.txtDesc.getText());
            TipoItem t=(TipoItem)view.cbTipo.getSelectedItem();
            item.setTipo(t);
            try{
                if(!verify){
                    item.setCod(null);
                    control.item.create(item);
                    h.setAccion(Estado.CREADO);
                    h.setEmpleado(currentUser);
                    h.setItem(item);
                    control.historialItem.create(h);
                }else{
                    control.item.edit(item);
                    modifyItem(copy, item);
                }
                view.btnLimpiar.doClick();
                loadData();
                item=null;
            }catch(Exception e){
                JOptionPane.showMessageDialog(view, "No se pudo guargar/Modificar");
            }
        });
        
        view.btnEliminar.addActionListener((ae) ->{
            try{
                if(item!=null){
                    //control.item.destroy(item.getCod());
                    item.setActivo(Estado.ELIMINADO);
                    control.item.edit(item);
                    HistorialItem h=new HistorialItem();
                    h.setAccion(Estado.ELIMINADO);
                    h.setEmpleado(currentUser);
                    h.setItem(item);
                    control.historialItem.create(h);
                    view.btnLimpiar.doClick();
                    loadData();
                    item=null;
                }
            }catch(Exception r){
                JOptionPane.showMessageDialog(view, "No se pudo Borrar");
            }
        });
    }
    
    public void loadData(){
        modelTipo=new DefaultComboBoxModel<>();
        modelTipo.addElement(TipoItem.sinTipo);
        modelTipo.addElement(TipoItem.mercaderia);
        modelTipo.addElement(TipoItem.materiaPrima);
        view.cbTipo.setModel(modelTipo);
        modelTable=new TableJPA(
                control.item.findItemEntities(true),
                new String[]{"Cod","Nombre","Precio","Tipo"},
                new String[]{"cod","nombre","precio","tipoDesc"},
                new Boolean[]{false,false,false},
                Item.class
        );
        view.tbItem.setModel(modelTable);
    }
    
    public void modifyItem(Item prev,Item next){
        if(!prev.getNombre().equals(next.getNombre())) modify("NOMBRE", prev.getNombre(), next.getNombre());
        if(!prev.getPrecio().equals(next.getPrecio())) modify("PRECIO", prev.getPrecio().toString(), next.getPrecio().toString());
        if(!prev.getDescripcion().equals(next.getDescripcion())) modify("DESCRIPCION", prev.getDescripcion(), next.getDescripcion());
        if(!prev.getTipo().equals(next.getTipo())) modify("TIPO", prev.getTipoDesc(), next.getTipoDesc());
    }
    
    public void modify(String campo,String valuePrev, String valueNext){
        HistorialItem h=new HistorialItem();
        h.setEmpleado(currentUser);
        h.setAccion(Estado.MODIFICADO);
        h.setItem(item);
        h.setCampo(campo);
        h.setValorAnterior(valuePrev);
        h.setValorNuevo(valueNext);
        control.historialItem.create(h);
    }
}
