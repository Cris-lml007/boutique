/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import utility.WindowDesign;
import view.AdministracionView;
import view.administracion.GestionEmpleadoView;
import view.administracion.HistorialCambiosView;
import view.administracion.ReporteView;

/**
 *
 * @author metallica
 */
public class AdministracionController {
    AdministracionView view;

    public AdministracionController(AdministracionView v) {
        this.view=v;
        initAction();
        view.btnRecuperacion.setVisible(false);
    }
    
    public void initAction(){
        view.btnEmpleado.addActionListener((ae) -> {
            GestionEmpleadoView v=new GestionEmpleadoView();
            new WindowDesign().callPanel(v, view);
            new GestionEmpleadoController(v);
        });
        
        view.btnHistorialItem.addActionListener((ae) -> {
            HistorialCambiosView v=new HistorialCambiosView();
            new WindowDesign().callPanel(v,view);
            new HistorialCambiosController(v);
        });
        
        view.btnReporte.addActionListener((ae) -> {
            ReporteView v=new ReporteView();
            new WindowDesign().callPanel(v, view);
            new ReporteController(v);
        });
    }
    
    
}
