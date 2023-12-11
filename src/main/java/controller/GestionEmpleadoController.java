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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import model.Empleado;
import model.Estado;
import model.Rol;
import model.TableJPA;
import persistent.Control;
import persistent.exceptions.IllegalOrphanException;
import persistent.exceptions.NonexistentEntityException;
import utility.WindowDesign;
import view.administracion.GestionEmpleadoView;

/**
 *
 * @author metallica
 */
public class GestionEmpleadoController {
    GestionEmpleadoView view;
    TableJPA<Empleado>modelTable;
    DefaultComboBoxModel<Rol>modelRol;
    DefaultComboBoxModel<Rol>modelRol1;
    String column[]={"Ci","Apellidos","Nombres","Estado","Tipo"};
    String atrib[]={"ci","apellido","nombre","activoName","rol"};
    Boolean edit[]={false,false,false,false,false};
    Control control=new Control();
    Empleado emp;

    public GestionEmpleadoController(GestionEmpleadoView v) {
        modelRol=new DefaultComboBoxModel(new Object[]{
            Rol.administrativo,
            Rol.gerente,
            Rol.personal
        });
        modelRol1=new DefaultComboBoxModel(new Object[]{
            Rol.administrativo,
            Rol.gerente,
            Rol.personal
        });
        this.view=v;
        modelTable=new TableJPA(column,atrib,edit);
        modelTable.loadMethod(Empleado.class);
        view.tbEmpleado.setModel(modelTable);
        view.cbRol1.setModel(modelRol1);
        view.cbRol.setModel(modelRol);
        loadData();
        initAction();
        new WindowDesign().JPasswordFieldPlaceHolder(view.passNewPassword, "********");
        new WindowDesign().JPasswordFieldPlaceHolder(view.passVerifyPassword, "********");
        JTableHeader t=view.tbEmpleado.getTableHeader();
        t.setBackground(new Color(25, 25, 25));
        t.setForeground(Color.white);
    }
    
    public void initAction(){
        view.tbEmpleado.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                int y=view.tbEmpleado.getSelectedRow();
                emp=modelTable.getObject(y);
                view.txtCi1.setText(emp.getCi()+"");
                view.textName1.setText(emp.getNombre());
                view.textSurname1.setText(emp.getApellido());
                view.cbRol1.setSelectedItem(emp.getRol());
                view.ckContratado1.setSelected(emp.getActivo()==Estado.activo ? true : false);
                view.txtCi1.setEditable(false);
                view.btnEliminar.setEnabled(true);
                view.btnModificar.setEnabled(true);
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
        
        view.txtCi1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar()=='\n'){
                    String ci=view.txtCi1.getText();
                    for(Empleado obj : modelTable.getAll()){
                        if(obj.getCi()==Integer.parseInt(ci)){
                            emp=obj;
                            view.textName1.setText(emp.getNombre());
                            view.textSurname1.setText(emp.getApellido());
                            view.cbRol.setSelectedItem(emp.getRol());
                            view.ckContratado1.setSelected(emp.getActivo()==Estado.activo ? true : false);
                            view.txtCi1.setEditable(false);
                            view.btnEliminar.setEnabled(true);
                            view.btnModificar.setEnabled(true);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(view,"No se encontro Empleado");
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        
        view.btnLimpiar1.addActionListener((ae) -> {
            emp=null;
            view.txtCi1.setText(null);
            view.txtCi1.setEditable(true);
            view.textName1.setText(null);
            view.textSurname1.setText(null);
            view.cbRol1.setSelectedItem(null);
            view.passNewPassword1.setText(null);
            view.passVerifyPassword1.setText(null);
            view.ckContratado1.setSelected(false);
            view.btnEliminar.setEnabled(false);
            view.btnModificar.setEnabled(false);
        });
        
        view.btnCreate.addActionListener((ae) -> {
            String p1=String.valueOf(view.passNewPassword.getPassword());
            String p2=String.valueOf(view.passVerifyPassword.getPassword());
            emp=new Empleado();
            emp.setRol((Rol) view.cbRol.getSelectedItem());
            if(emp.getRol()!=Rol.personal){
                if(view.textUsername.getText().equals("")){
                    JOptionPane.showMessageDialog(view, "Campo Usuario vacio");
                    return;
                }
                if(p1.equals("********") || p2.equals("********")){
                    JOptionPane.showMessageDialog(view, "Campos contraseña vacio");
                    return;
                }
            }
            emp.setCi(Integer.valueOf(view.textCi.getText()));
            emp.setNombre(view.textName.getText());
            emp.setApellido(view.textSurname.getText());
            emp.setUsuario(view.textUsername.getText());
            emp.setActivo(view.ckContratado.isSelected() ? Estado.activo : Estado.inactivo);
            if(p1.equals(p2) && p2.equals(p1)){
                emp.setContraseña(p1);
                try {
                    control.empleado.create(emp);
                    view.btnLimpiar.doClick();
                    loadData();
                    emp=null;
                } catch (Exception ex) {
                    Logger.getLogger(GestionEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(view, "No se puede crear el empleado");
                }
            }else JOptionPane.showMessageDialog(view, "Contraseñas no son iguales");
        });
        
        view.btnEliminar.addActionListener((ae) -> {
            if(emp!=null){
                try {
                    if(JOptionPane.showConfirmDialog(view, "Esta seguro de borrar este empleado?")!=0) return;
                    control.empleado.destroy(emp.getCi());
                    loadData();
                    view.btnLimpiar1.doClick();
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(GestionEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(view, "No se pudo eliminar porque el empleado no exite");
                }catch (Exception ex) {
                    Logger.getLogger(GestionEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(view,"No se puede eliminar este Empleado, empleado comprometido");
                } 
            }
        });
        
        view.btnLimpiar.addActionListener((ae) -> {
            view.textCi.setText(null);
            view.textName.setText(null);
            view.textSurname.setText(null);
            view.textUsername.setText(null);
            view.ckContratado.setSelected(false);
        });
        
        view.btnModificar.addActionListener((ae) -> {
            if(!emp.getCi().toString().equals(view.txtCi1.getText())){
                JOptionPane.showMessageDialog(view, "No se puede cambiar el CI");
                return;
            }
            emp.setNombre(view.textName1.getText());
            emp.setApellido(view.textSurname1.getText());
            emp.setRol((Rol) view.cbRol1.getSelectedItem());
            emp.setActivo(view.ckContratado1.isSelected() ? Estado.activo : Estado.inactivo);
            String p1=String.valueOf(view.passNewPassword1.getPassword());
            String p2=String.valueOf(view.passVerifyPassword1.getPassword());
            if(!p1.equals("")){
                if(p1.equals(p2)){
                    emp.setContraseña(p1);
                }else JOptionPane.showMessageDialog(view, "Contraseñas no son iguales");
            }
            if(!p2.equals("") && p1.equals("")) JOptionPane.showMessageDialog(view, "No se lleno el campo nueva contraseña");
            try {
                control.empleado.edit(emp);
                JOptionPane.showMessageDialog(view, "El empleado se modifico correctamente");
                loadData();
                view.btnLimpiar1.doClick();
                //view.btnLimpiar.doClick();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(GestionEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(view, "No se pudo modificar porque el empleado no exite");
            } catch (Exception ex) {
                Logger.getLogger(GestionEmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(view, "Error al modificar este Empleado");
            }
        });
    }
    
    public void loadData(){
        List<Empleado>l=control.empleado.findEmpleadoEntities();
        modelTable.setData(l);
        view.btnEliminar.setEnabled(false);
        view.btnModificar.setEnabled(false);
    }
}
