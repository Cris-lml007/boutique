/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Empleado;
import view.DashboardView;

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
    }
    
    
    public void initAction(){
        
    }
    
}
