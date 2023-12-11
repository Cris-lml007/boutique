/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import model.DetalleSub;
import model.Empleado;
import model.Item;
import model.ProveedorDistribuidor;
import model.Subministro;
import model.TableJPA;
import model.db;
import persistent.Control;
import view.BuscarItemView;
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
    DefaultComboBoxModel modelPD;
    DefaultComboBoxModel modelItem;
    ProveedorDistribuidor pd;
    Item item;
    Empleado emp;
    JTextField editPD;
    JTextField editItem;
    double total=0;

    public RegistrarEntradaController(RegistrarEntradaView v,Empleado e) {
        this.emp=e;
        this.view=v;
        editPD=(JTextField) view.cbNombre.getEditor().getEditorComponent();
        editItem=(JTextField) view.cbItem.getEditor().getEditorComponent();
        loadData();
        initAction();
        modelTable=new TableJPA(new String[]{"Producto","Cantidad","Precio","Subtotal"},new String[]{"productoName","cantidad","precio","subtotal"}, new Boolean[]{false,true,true,false});
        modelTable.loadMethod(DetalleSub.class);
        view.tbItem.setModel(modelTable);
        JTableHeader t=view.tbItem.getTableHeader();
        t.setBackground(new Color(25, 25, 25));
        t.setForeground(Color.white);
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
        
        view.cbNombre.addActionListener((ae -> {
            pd=(ProveedorDistribuidor) view.cbNombre.getSelectedItem();
            if( pd!=null) view.txtNIT.setText(pd.getCod().toString());
        }));
        
        view.btnLimpiar.addActionListener((ae -> {
            pd=null;
            view.txtNIT.setText(null);
            view.cbNombre.setSelectedItem(null);
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
                item=null;
                view.txtCodItem.setText(null);
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
                    existItem();
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
        
        view.cbItem.addActionListener((ae -> {
            item=(Item) view.cbItem.getSelectedItem();
            if(item!=null) view.txtCodItem.setText(item.getCod().toString());
        }));
        
        view.btnAñadir.addActionListener((ae -> {
            if(existItem()){
                for(int i=0;i<modelTable.getRowCount();i++){
                    Item it=modelTable.getObject(i).getProducto();
                    if(it.equals(item)){
                        JOptionPane.showMessageDialog(view, "Este producto ya fue agregado");
                        return;
                    }
                }
                DetalleSub det=new DetalleSub();
                det.setProducto(item);
                det.setPrecio(BigDecimal.valueOf(Double.parseDouble(view.txtPrecio.getText())));
                det.setCantidad(Integer.valueOf(view.txtCantidad.getText()));
                modelTable.addElement(det);
                total+=(det.getPrecio().doubleValue()*det.getCantidad());
                view.txtTotal.setText(total+"");
                view.txtCodItem.setText(null);
                view.cbItem.setSelectedItem(null);
                view.txtPrecio.setText(null);
                view.txtCantidad.setText(null);
                item=null;
            }
        }));
        
        view.tbItem.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int x=view.tbItem.getSelectedRow();
                int y=view.tbItem.getSelectedColumn();
                if(view.tbItem.isCellEditable(x, y)){
                    if(view.tbItem.getColumnName(y).equals("Precio")){
                        DetalleSub d=modelTable.getObject(x);
                        String t=JOptionPane.showInputDialog(view, "Precio: ");
                        if(t==null) return;
                        double precio = Double.parseDouble(t);
                        BigDecimal p=BigDecimal.valueOf(precio);
                        total-= d.getPrecio().doubleValue()*d.getCantidad();
                        d.setPrecio(p);
                        modelTable.update(d);
                        total+= d.getPrecio().doubleValue()*d.getCantidad();
                        view.txtTotal.setText(total+"");
                    }else if(view.tbItem.getColumnName(y).equals("Cantidad")){
                        DetalleSub d=modelTable.getObject(x);
                        String t=JOptionPane.showInputDialog(view, "Cantidad: ");
                        if(t==null) return;
                        total-= d.getPrecio().doubleValue()*d.getCantidad();
                        Integer cant = Integer.valueOf(t);
                        d.setCantidad(cant);
                        modelTable.update(d);
                        total+= d.getPrecio().doubleValue()*d.getCantidad();
                        view.txtTotal.setText(total+"");
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
            DetalleSub d= modelTable.getObject(view.tbItem.getSelectedRow());
            total-= d.getPrecio().doubleValue()*d.getCantidad();
            view.txtTotal.setText(total+"");
            modelTable.removeIndex(view.tbItem.getSelectedRow());
            item=null;
        }));
        
        view.btnQuitarTodo.addActionListener((ae -> {
            modelTable.clear();
            total=0;
            view.txtTotal.setText("0");
            item=null;
        }));
        
        view.btnFinalizar.addActionListener((ae -> {
            Subministro sub=new Subministro();
            sub.setProveedor(pd);
            sub.setDescripcion(view.txtDesc.getText());
            sub.setEmpleado(emp);
            try{
                control.subministro.create(sub);
            }catch(Exception e){
                System.out.println("hubo un error al crear subministro: "+e);
            }
            try{
                for(DetalleSub i : modelTable.getAll()){
                    i.setSubministro(sub);
                    Item it=i.getProducto();
                    it.setCantidad(it.getCantidad()+i.getCantidad());
                    control.detalleSubministro.create(i);
                    control.item.edit(it);
                }
                Map<String,Object>p=new HashMap();
                p.put("COD_SUB", sub.getCod());
                p.put("Logo", getClass().getResourceAsStream("/Asset/logoK12-black.png"));
                new report.JasperReportController("OrdenEntradaReport",db.getConection()).getReport(p);
                view.btnQuitarTodo.doClick();
                view.btnLimpiar.doClick();
                view.txtDesc.setText(null);
                item=null;
                pd=null;
            }catch(Exception e){
                System.out.println("Hubo un error al crear detalle: "+e);
            }
        }));
        
        view.btnBuscar.addActionListener((ae -> {
            JFrame frame=new JFrame("Buscar Producto");
            BuscarItemView v=new BuscarItemView();
            frame.add(v);
            frame.setSize(670, 380);
            frame.setVisible(true);
            new BuscarItemController(v,frame);            
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent we) {
                }

                @Override
                public void windowClosing(WindowEvent we) {
                    frame.dispose();
                }

                @Override
                public void windowClosed(WindowEvent we) {
                    System.out.println("se ejecuto");
                    if(BuscarItemController.getItem()!=null){
                        item=BuscarItemController.getItem();
                        item.changedModeToString(1);
                        view.txtCodItem.setText(item.getCod().toString());
                        view.cbItem.setSelectedItem(item);
                    }
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
            
        }));
    }
    
    public final void loadData(){
        modelPD=new DefaultComboBoxModel();
        modelItem=new DefaultComboBoxModel();
        List <ProveedorDistribuidor>l=control.proveedorDis.findProveedorDistribuidorEntities();
        List <Item> li=control.item.findItemEntities(true);
        for(ProveedorDistribuidor i : l){
            i.changedModeToString(1);
            modelPD.addElement(i);
        }
        for(Item i : li){
            i.changedModeToString(1);
            modelItem.addElement(i);
        }
        view.cbNombre.setModel(modelPD);
        view.cbItem.setModel(modelItem);
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
