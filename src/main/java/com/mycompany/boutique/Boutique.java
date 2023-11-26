/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.boutique;

import com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme;
import java.util.Collections;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import view.SplashView;

/**
 *
 * @author metallica
 */
public class Boutique {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        try{
            UIManager.setLookAndFeel(new FlatLightFlatIJTheme());
            FlatLightFlatIJTheme.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#333333"));
            FlatLightFlatIJTheme.setup();
        }catch(UnsupportedLookAndFeelException e){
            System.out.println(e);
        }
        //LoginView login=new LoginView();
        //login.setVisible(true);
        //new LoginController(login);
        new SplashView().setVisible(true);
    }
}
