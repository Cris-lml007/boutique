/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.UIManager;
import model.Empleado;
import view.DashboardView;
import view.EntradaView;
import view.RegistrarView;

/**
 *
 * @author metallica
 */
public class DashboardController {
    
    private Empleado current;
    private DashboardView view;
    
    public DashboardController(DashboardView v,Empleado user){
        try{
            UIManager.setLookAndFeel(new FlatLightLaf());
            FlatLightLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor","#00cc66"));
            FlatLightLaf.setup();
        }catch(Exception e){
            System.out.println(e);
        }
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
        
        view.btnEntrada.addActionListener((ae) -> {
            EntradaView v=new EntradaView();
            new utility.WindowDesign().callPanel(v, view.pnContenido);
            new EntradaController(v);
        });
    }
}
