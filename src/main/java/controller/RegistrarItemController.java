/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import model.Item;
import model.TableJPA;
import persistent.Control;
import view.RegistrarItemView;

/**
 *
 * @author metallica
 */
public class RegistrarItemController{
    RegistrarItemView view;
    TableJPA modelTable;
    Control control=new Control();
    Item item;
    
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
        }));
        
        view.btnGuardar.addActionListener((ae) -> {
            boolean verify=item!=null;
            if(!verify) item=new Item();
            item.setNombre(view.txtNombre.getText());
            item.setPrecio(BigDecimal.valueOf(Double.parseDouble(view.txtPrecio.getText())));
            item.setDescripcion(view.txtDesc.getText());
            try{
                if(!verify){
                    item.setCod(null);
                    control.item.create(item);
                }else{
                    control.item.edit(item);
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
                    control.item.destroy(item.getCod());
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
        modelTable=new TableJPA(control.item.findItemEntities(),new String[]{"Cod","Nombre","Precio"}, new boolean[]{false,false,false});
        view.tbItem.setModel(modelTable);
    }
}
