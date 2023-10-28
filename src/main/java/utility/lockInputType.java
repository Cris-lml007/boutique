/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTextField;

/**
 *
 * @author metallica
 */
public class lockInputType {
    private ArrayList ignore;
    public lockInputType(){
      ignore=new ArrayList();
    }
    
    public void setIgnore(ArrayList list){
        ignore=list;
    }
    
    private boolean iterationIgnore(KeyEvent ev){
        for(int i=0;i<ignore.size();i++){
            char key=ignore.get(i).toString().charAt(0);
            if((int)ev.getKeyChar()==(int)key){
                return true;
            }
        }
        return false;
    }
    public void lockNumber(JTextField text){
        text.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(iterationIgnore(ke)) return;
                if((int)ke.getKeyChar()>=(int)'0' && (int)ke.getKeyChar()<=(int)'9'){
                    ke.consume();
                }
            }
            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });    
    }
    public void lockSymbol(JTextField text){
        text.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(iterationIgnore(ke)) return;
                if((int)ke.getKeyChar()>=(int)'A' && (int)ke.getKeyChar()<=(int)'Z'){
                    return;
                }else if((int)ke.getKeyChar()>=(int)'a' && (int)ke.getKeyChar()<=(int)'z'){
                    return;
                }else if((int)ke.getKeyChar()>=(int)'0' && (int)ke.getKeyChar()<=(int)'9'){
                    return;
                }else if((int)ke.getKeyChar()==(int)' '){
                    return;
                }else ke.consume();
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
    }
    
    public void lockAlfa(JTextField text){
        text.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
                if(iterationIgnore(ke)) return;
                if((int)ke.getKeyChar()>=(int)'A' && (int)ke.getKeyChar()<=(int)'Z'){
                    ke.consume();
                }else if((int)ke.getKeyChar()>=(int)'a' && (int)ke.getKeyChar()<=(int)'z'){
                    ke.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                
            }
        });
    }
}
