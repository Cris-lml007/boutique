/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Almacen;
import model.DetalleSub;
import model.Empleado;
import model.Item;
import model.ProveedorDistribuidor;
import model.Subministro;
import model.TableJPA;
import persistent.Control;
import view.DashboardView;
import view.RegistrarEntradaView;
import view.RegistrarItemView;
import view.RegistrarPDView;

/**
 *
 * @author metallica
 */
public class RegistrarEntradaController {
    Control control=new Control();
    RegistrarEntradaView view;
    TableJPA<DetalleSub> modelTable;
    DefaultComboBoxModel modelAlmacen;
    DefaultComboBoxModel modelPD;
    DefaultComboBoxModel modelItem;
    ProveedorDistribuidor pd;
    Item item;
    Empleado emp;
    JTextField editPD;
    JTextField editItem;

    public RegistrarEntradaController(RegistrarEntradaView v,Empleado e) {
        this.emp=e;
        this.view=v;
        editPD=(JTextField) view.cbNombre.getEditor().getEditorComponent();
        editItem=(JTextField) view.cbItem.getEditor().getEditorComponent();
        loadData();
        initAction();
        modelTable=new TableJPA(new String[]{"Producto","Cantidad","Precio","Subtotal"}, new Boolean[]{false,true,true,false});
        view.tbItem.setModel(modelTable);
    }
    
    public final void initAction(){        
        view.txtNIT.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar()=='\n'){
                    existPD();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                for(int i=0;i<modelPD.getSize();i++){
                    pd=(ProveedorDistribuidor) modelPD.getElementAt(i);
                    if(view.txtNIT.getText().equals(pd.getCod().toString()) && !view.txtNIT.getText().equals("")){
                        view.cbNombre.setSelectedItem(pd);
                        return;
                    }else{
                        pd=null;
                       view.cbNombre.setSelectedItem(null);
                    }
                }
            }
        });
        
        editPD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                pd=null;
                view.txtNIT.setText(null);
                if(ke.getKeyChar()=='\n'){
                    for(int i=0;i<modelPD.getSize();i++){
                        if(modelPD.getElementAt(i).toString().contains(editPD.getText().toUpperCase()) && !editPD.getText().equals("")){
                            pd=(ProveedorDistribuidor) modelPD.getElementAt(i);
                            view.cbNombre.setSelectedItem(pd);
                            view.txtNIT.setText(pd.getCod().toString());
                            return;
                        }
                    }
                    existPD();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {

            }
        });
        
        view.btnLimpiar.addActionListener((ae -> {
            pd=null;
            view.txtNIT.setText(null);
            view.cbNombre.setSelectedItem(null);
            view.cbAlmacen.setSelectedItem(null);
            view.txtDesc.setText(null);
        }));
        
        view.btnLimpiar.addActionListener((ae -> {
            pd=null;
            view.txtNIT.setText(null);
            editPD.setText(null);
            view.txtDesc.setText(null);
        }));
        
        view.txtCodItem.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar()=='\n'){
                    existItem();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                for(int i=0;i<modelItem.getSize();i++){
                    if(((Item)modelItem.getElementAt(i)).getCod().toString().equals(view.txtCodItem.getText())){
                        item=(Item)modelItem.getElementAt(i);
                        view.cbItem.setSelectedItem(item);
                        return;
                    }
                }
                item=null;
                view.cbItem.setSelectedItem(null);
            }
        });
        
        editItem.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar()=='\n'){
                    item=null;
                    for(int i=0;i<modelItem.getSize();i++){
                        if(modelItem.getElementAt(i).toString().contains(editItem.getText().toUpperCase()) && !editItem.getText().equals("")){
                            item=(Item)modelItem.getElementAt(i);
                            view.cbItem.setSelectedItem(item);
                            view.txtCodItem.setText(item.getCod().toString());
                            return;
                        }
                    }
                    item=null;
                    view.txtCodItem.setText(null);
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        
        view.btnAñadir.addActionListener((ae -> {
            if(existItem()){
                DetalleSub det=new DetalleSub();
                det.setProducto(item);
                det.setPrecio(BigDecimal.valueOf(Double.parseDouble(view.txtPrecio.getText())));
                det.setCantidad(Integer.parseInt(view.txtCantidad.getText()));
                det.setCantExis(Integer.parseInt(view.txtCantidad.getText()));
                modelTable.addElement(det);
                view.txtTotal.setText((det.getPrecio().doubleValue()*det.getCantidad())+"");
                view.txtCodItem.setText(null);
                view.cbItem.setSelectedItem(null);
                view.txtPrecio.setText(null);
                view.txtCantidad.setText(null);
            }
        }));
        
        view.tbItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int x=view.tbItem.getSelectedRow();
                int y=view.tbItem.getSelectedColumn();
                if(view.tbItem.isCellEditable(x, y)){
                    if(view.tbItem.getColumnName(y).toString().equals("Precio")){
                        DetalleSub d=(DetalleSub)modelTable.getObject(x);
                        String t=JOptionPane.showInputDialog(view, "Precio: ");
                        if(t==null) return;
                        double precio = Double.parseDouble(t);
                        BigDecimal p=BigDecimal.valueOf(precio);
                        d.setPrecio(p);
                        modelTable.update(d);
                        view.txtTotal.setText((d.getPrecio().doubleValue()*d.getCantidad())+"");
                    }else if(view.tbItem.getColumnName(y).toString().equals("Cantidad")){
                        DetalleSub d=(DetalleSub)modelTable.getObject(x);
                        String t=JOptionPane.showInputDialog(view, "Cantidad: ");
                        if(t==null) return;
                        Integer cant = Integer.parseInt(t);
                        d.setCantidad(cant);
                        d.setCantExis(cant);
                        modelTable.update(d);
                        view.txtTotal.setText((d.getPrecio().doubleValue()*d.getCantidad())+"");
                    }
                }
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
        
        view.btnQuitar.addActionListener((ae -> {
            modelTable.removeIndex(view.tbItem.getSelectedRow());
        }));
        
        view.btnQuitarTodo.addActionListener((ae -> {
            modelTable.clear();
        }));
        
        view.btnFinalizar.addActionListener((ae -> {
            Subministro sub=new Subministro();
            sub.setProveedor(pd);
            sub.setDescripcion(view.txtDesc.getText());
            Almacen alm=(Almacen) modelAlmacen.getSelectedItem();
            sub.setAlmacen(alm);
            sub.setEmpleado(emp);
            try{
                control.subministro.create(sub);
                System.out.println(sub.getCod());
            }catch(Exception e){
                System.out.println("hubo un error al crear subministro: "+e);
            }
            try{
                for(DetalleSub i : modelTable.getAll()){
                    i.setSubministro(sub);
                    control.detalleSubministro.create(i);
                }
            }catch(Exception e){
                System.out.println("Hubo un error al crear detalle: "+e);
            }
        }));
    }
    
    public final void loadData(){
        modelAlmacen=new DefaultComboBoxModel();
        modelPD=new DefaultComboBoxModel();
        modelItem=new DefaultComboBoxModel();
        List <ProveedorDistribuidor>l=control.proveedorDis.findProveedorDistribuidorEntities();
        List <Almacen>la=control.almacen.findAlmacenEntities();
        List <Item> li=control.item.findItemEntities();
        for(ProveedorDistribuidor i : l){
            i.changedModeToString(1);
            modelPD.addElement(i);
        }
        for(Almacen i : la){
            i.changedModeToString(1);
            modelAlmacen.addElement(i);
        }
        for(Item i : li){
            i.changedModeToString(1);
            modelItem.addElement(i);
        }
        view.cbNombre.setModel(modelPD);
        view.cbAlmacen.setModel(modelAlmacen);
        view.cbItem.setModel(modelItem);
        view.cbAlmacen.setSelectedItem(null);
        view.cbNombre.setSelectedItem(null);
        view.cbItem.setSelectedItem(null);
    }
    
    
    public boolean existPD(){
        if(pd==null){
            if(JOptionPane.showConfirmDialog(view, "este proveedor/distribuidor no existe, desea crearlo?")==0){
                JFrame frame=new JFrame("Registrar Proveedor/Distribuidor");
                RegistrarPDView v=new RegistrarPDView();
                new RegistrarPDController(v);
                v.setLocation(0,0);
                frame.add(v);
                frame.setSize(870,670);
                frame.setResizable(false);
                frame.setVisible(true);

                frame.addWindowListener(new WindowListener(
                ) {
                    @Override
                    public void windowOpened(WindowEvent we) {
                    }

                    @Override
                    public void windowClosing(WindowEvent we) {
                        frame.dispose();
                        loadData();
                    }

                    @Override
                    public void windowClosed(WindowEvent we) {
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
        return true;
    }
    
    public boolean existItem(){
        if(item==null){
            if(JOptionPane.showConfirmDialog(view, "este Producto no existe, desea crearlo?")==0){
                JFrame frame=new JFrame("Registrar Item");
                RegistrarItemView v=new RegistrarItemView();
                new RegistrarItemController(v);
                v.setLocation(0,0);
                frame.add(v);
                frame.setSize(870,670);
                frame.setResizable(false);
                frame.setVisible(true);

                frame.addWindowListener(new WindowListener(
                ) {
                    @Override
                    public void windowOpened(WindowEvent we) {
                    }

                    @Override
                    public void windowClosing(WindowEvent we) {
                        frame.dispose();
                        loadData();
                    }

                    @Override
                    public void windowClosed(WindowEvent we) {
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
        return true;
    }
}
