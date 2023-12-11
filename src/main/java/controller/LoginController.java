/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Bloqueo;
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
    private int intentos=0;
    private Bloqueo b;
    
    public static Empleado e;
    
    public LoginController(LoginView view){
        this.vlogin=view;
        initAction();
        b=control.bloqueo.findBloqueo(1);
    }
    
    public static Empleado getCurrentEmpleado(){
        return e;
    }
    
    
    public void initAction(){
        vlogin.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(intentos==3 && b.getVal()>0){
                    Long time=new Date().getTime()-b.getFechaA().getTime();
                    if((time/(1000*60))<3){
                        JOptionPane.showMessageDialog(vlogin, "Limite de intento superados espere: 3 min");
                        return;
                    }else{
                        intentos=0;
                    }
                }

                if((emp=control.empleado.login(vlogin.txtUser.getText(),new String(vlogin.pasPass.getPassword())))!=null){
                    e=emp;
                    DashboardView v=new DashboardView();
                    DashboardController c=new DashboardController(v,emp);
                    v.setVisible(true);
                    vlogin.dispose();
                }else{
                    JOptionPane.showMessageDialog(vlogin, "usuario y contraseña incorrectos");
                    intentos+=1;
                    if(intentos==3){
                        b.setVal(b.getVal()+1);
                        b.setFechaA(null);
                        try {
                            control.bloqueo.edit(b);
                            b=control.bloqueo.findBloqueo(1);
                        } catch (Exception ex) {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if(b.getVal()==3) System.exit(0);
                }
            }
        });
    }
}
