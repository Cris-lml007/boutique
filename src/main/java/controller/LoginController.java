/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Empleado;
import org.eclipse.persistence.internal.oxm.mappings.Login;
import persistent.Control;
import persistent.EmpleadoJpaController;
import view.DashboardView;
import view.LoginView;

/**
 *
 * @author metallica
 */
public class LoginController {
    private LoginView vlogin;
    private Empleado emp;
    private Control control=new Control();
    
    public LoginController(LoginView view){
        this.vlogin=view;
        initAction();
    }
    
    
    public void initAction(){
        vlogin.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if((emp=control.empleado.login(vlogin.txtUser.getText(),new String(vlogin.pasPass.getPassword())))!=null){
                    DashboardView v=new DashboardView();
                    DashboardController c=new DashboardController(v,emp);
                    v.setVisible(true);
                    vlogin.dispose();
                }else JOptionPane.showMessageDialog(vlogin, "usuario y contraseña incorrectos");
            }
        });
    }
}
