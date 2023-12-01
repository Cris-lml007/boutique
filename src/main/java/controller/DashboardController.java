/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.Empleado;
import model.Rol;
import model.db;
import utility.WindowDesign;
import view.AdministracionView;
import view.BuscarItemView;
import view.DashboardView;
import view.EntradaView;
import view.InventarioView;
import view.LoginView;
import view.PerfilView;
import view.RegistrarView;
import view.SalidaView;

/**
 *
 * @author metallica
 */
public final class DashboardController {
    
    private Empleado current;
    private final DashboardView view;
    private Color colorBlack=new Color(19,19,19), ColorFocus=new Color(50, 50, 50);
    private JButton a,b;
    private boolean active=false;
    private db database;
    private static JFrame frame;
    
    public DashboardController(DashboardView v,Empleado user){
        try{
            database=new db();
            UIManager.setLookAndFeel(new FlatLightLaf());
            FlatLightLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor","#191919"));
            
            FlatLightLaf.setup();
        }catch(UnsupportedLookAndFeelException e){
            System.out.println(e);
        }
        this.view=v;
        this.current=user;
        view.lblUsuario.setText(current.getNombre()+" "+current.getApellido());
        initAction();
        frame=v;
        if(current.getRol()!=Rol.gerente ) view.btnAdmin.setVisible(false);
    }
    
    
    public void initAction(){
        view.btnRegistrar.addActionListener((ActionEvent ae) -> {
            RegistrarView v=new RegistrarView();
            new utility.WindowDesign().callPanel(v, view.pnContenido);
            new RegistrarController(v,current);
            b=view.btnRegistrar;
            LoadColorFocus();
        });
        
        view.btnEntrada.addActionListener((ae) -> {
            EntradaView v=new EntradaView();
            new WindowDesign().callPanel(v, view.pnContenido);
            new EntradaController(v);
            b=view.btnEntrada;
            LoadColorFocus();
        });
        
        view.btnSalida.addActionListener((ae) -> {
            SalidaView v=new SalidaView();
            new WindowDesign().callPanel(v, view.pnContenido);
            new SalidaController(v);
            b=view.btnSalida;
            LoadColorFocus();
        });
        
        view.btnCerrarSesion.addActionListener((ae) -> {
            current=null;
            view.dispose();
            LoginView v=new LoginView();
            new LoginController(v);
            v.setVisible(true);
            b=view.btnCerrarSesion;
            LoadColorFocus();
        });
        
        view.btnInventario.addActionListener((ae) -> {
            InventarioView v=new InventarioView();
            new WindowDesign().callPanel(v, view.pnContenido);
            new InventarioController(v);
            b=view.btnInventario;
            LoadColorFocus();
        });
        
        view.btnPerfil.addActionListener((ae) -> {
            PerfilView v=new PerfilView();
            new WindowDesign().callPanel(v, view.pnContenido);
            new PerfilController(v);
            b=view.btnPerfil;
            LoadColorFocus();
        });
        
        view.btnAdmin.addActionListener((ae) -> {
            AdministracionView v=new AdministracionView();
            new WindowDesign().callPanel(v, view.pnContenido);
            new AdministracionController(v);
            b=view.btnAdmin;
            LoadColorFocus();
        });
    }
    
    public void LoadColorFocus(){
        b.setBackground(ColorFocus);
        if(active==false){
            a=b;
            active=true;
        }else{
            a.setBackground(colorBlack);
            a=b;
        }
    }
    
    public static JFrame getFrame(){
        return frame;
    }
    
}
