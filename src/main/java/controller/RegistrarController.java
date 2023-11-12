/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utility.WindowDesign;
import view.RegistrarItemView;
import view.RegistrarLocalView;
import view.RegistrarPDView;
import view.RegistrarView;

/**
 *
 * @author metallica
 */
public class RegistrarController {
    
    public RegistrarView view;
    public WindowDesign w=new WindowDesign();
    
    public RegistrarController(RegistrarView v){
        this.view=v;
        initAction();
    }
    
    private void initAction(){
        view.btnRegistrarLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                RegistrarLocalView v=new RegistrarLocalView();
                w.callPanel(v, view);
                new RegistrarLocalController(v);
            }
        });
        
        view.btnRegistrarPD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                RegistrarPDView v=new RegistrarPDView();
                w.callPanel(v, view);
                new RegistrarPDController(v);
                
            }
        });
        
        view.btnRegistrarProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                RegistrarItemView v=new RegistrarItemView();
                new RegistrarItemController(v);
                w.callPanel(v, view);
            }
        });
    }
}
