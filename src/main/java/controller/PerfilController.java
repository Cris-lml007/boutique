/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Empleado;
import model.md5;
import persistent.Control;
import persistent.exceptions.NonexistentEntityException;
import utility.WindowDesign;
import view.PerfilView;

/**
 *
 * @author metallica
 */
public class PerfilController{
    Control control=new Control();
    PerfilView view;
    Empleado emp;
    WindowDesign window=new WindowDesign();
    
    public PerfilController(PerfilView v){
        this.view=v;
        loadData();
        initAction();
        window.JPasswordFieldPlaceHolder(view.passPassword, "asdaefdas");
        window.JPasswordFieldPlaceHolder(view.passNewPassword, "asdaefdas");
        window.JPasswordFieldPlaceHolder(view.passVerifyPassword, "asdaefdas");
    }
    
    public void initAction(){
        view.btnUpdate.addActionListener((ae) -> {
            emp.setApellido(view.txtSurname.getText());
            emp.setNombre(view.txtName.getText());
            emp.setUsuario(view.txtUsername.getText());
            try {
                control.empleado.edit(emp);
                System.out.println("se edito el usuario");
            } catch (NonexistentEntityException ex) {
                JOptionPane.showMessageDialog(view, "Se actualizo correctamente");
                Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(view, "No se pudo actualizar");
            } catch (Exception ex) {
                Logger.getLogger(PerfilController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(view, "No se pudo actualizar");
            }finally{
                loadData();
            }
        });
        
        view.btnChangePassword.addActionListener((ae) -> {
            String pass=md5.getMD5Hash(String.valueOf(view.passPassword.getPassword()));
            if(emp.equalContraseña(pass)){
                
                if(String.valueOf(view.passNewPassword.getPassword()).equals((String.valueOf(view.passVerifyPassword.getPassword())))  ){
                    emp.setContraseña(String.valueOf(view.passNewPassword.getPassword()));
                    try{
                        control.empleado.edit(emp);
                        JOptionPane.showMessageDialog(view, "Se modifico la contraseña correctamente");
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(view, "No se pudo modificar la contraseña");
                    }
                }else JOptionPane.showMessageDialog(view, "Contraseña actual incorrecta");
            }else JOptionPane.showMessageDialog(view, "Las contraseñas no son iguales");
        });
    }
    
    public void loadData(){
        emp=LoginController.getCurrentEmpleado();
        view.txtCi.setText(emp.getCi().toString());
        view.txtRol.setText(emp.getRol().toString());
        view.txtName.setText(emp.getNombre());
        view.txtSurname.setText(emp.getApellido());
        view.txtUsername.setText(emp.getUsuario());
        long e=control.totalSubministroByEmpleado(emp);
        long s=control.totalDistribucionByEmpleado(emp);
        long t=e+s;
        view.txtOrdenE.setText(e+"");
        view.txtOrdenS.setText(s+"");
        view.txtOrdenT.setText(t+"");
    }
}
