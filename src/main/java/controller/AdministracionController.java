/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import utility.WindowDesign;
import view.AdministracionView;
import view.DashboardView;
import view.administracion.GestionEmpleadoView;

/**
 *
 * @author metallica
 */
public class AdministracionController {
    AdministracionView view;

    public AdministracionController(AdministracionView v) {
        this.view=v;
        initAction();
    }
    
    public void initAction(){
        view.btnEmpleado.addActionListener((ae) -> {
            GestionEmpleadoView v=new GestionEmpleadoView();
            new WindowDesign().callPanel(v, view);
            new GestionEmpleadoController(v);
        });
    }
    
    
}
