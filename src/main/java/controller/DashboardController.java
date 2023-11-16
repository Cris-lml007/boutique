/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Empleado;
import view.DashboardView;
import view.RegistrarLocalView;
import view.RegistrarView;

/**
 *
 * @author metallica
 */
public class DashboardController {
    
    private Empleado current;
    private DashboardView view;
    
    public DashboardController(DashboardView v,Empleado user){
        this.view=v;
        this.current=user;
        view.lblUsuario.setText(current.getNombre()+" "+current.getApellido());
        initAction();
    }
    
    
    public void initAction(){
        view.btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                RegistrarView v=new RegistrarView();
                new utility.WindowDesign().callPanel(v, view.pnContenido);
                new RegistrarController(v,current);
            }
        });
    }
    
}
