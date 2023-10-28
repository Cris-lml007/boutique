/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import java.awt.*;
import javax.swing.ImageIcon;

/**
 *
 * @author metallica
 */
public class LoadImage {
    private Image img=null;
    private ImageIcon icon=null;
    
    
    public LoadImage(String url,int height,int width){
        img=new ImageIcon(getClass().getResource(url)).getImage();
        icon=new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    public ImageIcon getImg(){
        return icon;
    }
}
