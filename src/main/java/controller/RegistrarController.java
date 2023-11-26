/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import model.Empleado;
import utility.WindowDesign;
import view.RegistrarEntradaView;
import view.RegistrarItemView;
import view.RegistrarLocalView;
import view.RegistrarPDView;
import view.RegistrarSalidaView;
import view.RegistrarView;

/**
 *
 * @author metallica
 */
public class RegistrarController {
    
    public RegistrarView view;
    public WindowDesign w=new WindowDesign();
    public Empleado empleado;
    
    public RegistrarController(RegistrarView v,Empleado emp){
        this.view=v;
        this.empleado=emp;
        initAction();
    }
    
    private void initAction(){
        view.btnRegistrarLocal.addActionListener((ActionEvent ae) -> {
            RegistrarLocalView v=new RegistrarLocalView();
            w.callPanel(v, view);
            new RegistrarLocalController(v);
        });
        
        view.btnRegistrarPD.addActionListener((ActionEvent ae) -> {
            RegistrarPDView v=new RegistrarPDView();
            w.callPanel(v, view);
            new RegistrarPDController(v);
        });
        
        view.btnRegistrarProducto.addActionListener((ActionEvent ae) -> {
            RegistrarItemView v=new RegistrarItemView();
            new RegistrarItemController(v);
            w.callPanel(v, view);
        });
        
        view.btnRegistrarE.addActionListener((ae)->{
            RegistrarEntradaView v=new RegistrarEntradaView();
            new RegistrarEntradaController(v,empleado);
            w.callPanel(v, view);
        });
        
        view.btnRegistrarS.addActionListener((ae -> {
            RegistrarSalidaView v=new RegistrarSalidaView();
            new RegistrarSalidaController(v, empleado);
            w.callPanel(v, view);
        }));
    }
}
