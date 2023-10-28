/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author metallica
 */
public class WindowDesign{
    int X,Y;
    
    public WindowDesign(){
    }
    
    public void init(){
        
    }
    
        public void  windowsMove(JFrame window){
        window.addMouseMotionListener(new MouseMotionListener(){
            @Override
            public void mouseDragged(MouseEvent me) {
                window.setLocation(me.getXOnScreen()-X,me.getYOnScreen()- Y);
            }

            @Override
            public void mouseMoved(MouseEvent me) { 
            }
        });
        window.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent me) {
            }

            @Override
            public void mousePressed(MouseEvent me) {
                X=me.getX();
                Y=me.getY();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
            }

            @Override
            public void mouseEntered(MouseEvent me) {
            }

            @Override
            public void mouseExited(MouseEvent me) {     
            }
            
        });
    }
    
    public void JTextFieldPlaceHolder(JTextField a,String text){
        a.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                /*if(a.getText().equals(text)){
                    a.setText("");
                    a.setForeground(Color.black);
                }else if(a.getText().equals("")){
                    a.setText(text);
                    a.setForeground(new Color(204,204,204));
                }*/
            }

            @Override
            public void mousePressed(MouseEvent me) {
                
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                
            }
        });
        a.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                if(a.getText().equals(text)){
                    a.setText("");
                    a.setForeground(Color.black);
                }else if(a.getText().equals("")){
                    a.setText(text);
                    a.setForeground(new Color(204,204,204));
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if(a.getText().equals("")){
                    a.setText(text);
                    a.setForeground(new Color(204,204,204));
                }        
            }
        });
    }
    
    public void JPasswordFieldPlaceHolder(JPasswordField a,String text){
        a.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                /*if(String.valueOf(a.getPassword()).equals(text)){
                    a.setText("");
                    a.setForeground(Color.black);
                }else if(String.valueOf(a.getPassword()).equals("")){
                    a.setText(text);
                    a.setForeground(new Color(204,204,204));
                }*/
            }

            @Override
            public void mousePressed(MouseEvent me) {
                
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                
            }

            @Override
            public void mouseEntered(MouseEvent me) {
                
            }

            @Override
            public void mouseExited(MouseEvent me) {
                
            }
        });
        a.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {    
                if(String.valueOf(a.getPassword()).equals(text)){
                    a.setText("");
                    a.setForeground(Color.black);
                }else if(String.valueOf(a.getPassword()).equals("")){
                    a.setText(text);
                    a.setForeground(new Color(204,204,204));
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                if(String.valueOf(a.getPassword()).equals("")){
                    a.setText(text);
                    a.setForeground(new Color(204,204,204));
                }
            }
        });
    }
    
    public void callPanel(JPanel input,JPanel output){
        input.setLocation(0,0);
        input.setSize(output.getSize().width,output.getSize().height);
        output.removeAll();
        output.add(input);
        output.revalidate();
        output.repaint();
    }
}
