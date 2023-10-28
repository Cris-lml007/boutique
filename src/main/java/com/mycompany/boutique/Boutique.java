/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.boutique;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme;
import controller.LoginController;
import java.util.Collection;
import java.util.Collections;
import javax.swing.UIManager;
import org.eclipse.persistence.internal.sessions.factories.model.login.LoginConfig;
import view.LoginView;
import view.SplashView;

/**
 *
 * @author metallica
 */
public class Boutique {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        try{
            UIManager.setLookAndFeel(new FlatLightLaf());
            FlatLightLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#333333"));
            FlatLightLaf.setup();
        }catch(Exception e){
            System.out.println(e);
        }
        //LoginView login=new LoginView();
        //login.setVisible(true);
        //new LoginController(login);
        new SplashView().setVisible(true);
    }
}
