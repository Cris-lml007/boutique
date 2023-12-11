/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author metallica
 */
public class Usb {
    String name;
    String label;
    String UUID;

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getUUID() {
        return UUID;
    }

    @Override
    public String toString() {
        return label;
    }
    
    
    
    public static List<Usb> getUSBDevices(){
        List<Usb>l=new ArrayList();
        try{
            String command[]={"bash","-c","lsblk /dev/sd[b-z][1-9] -Po NAME,LABEL,UUID"};
            ProcessBuilder p=new ProcessBuilder(command);
            //p.redirectErrorStream(true);
            Process process=p.start();
            BufferedReader read=new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line=null;
            while((line=read.readLine())!=null){
                String split[]=line.split(" ");
                Usb u=new Usb();
                for(String i : split){
                    if(i.contains("NAME")) u.name=i.substring(6,i.length()-1);
                    else if(i.contains("LABEL")) u.label=i.substring(7, split.length>3 ? i.length():i.length()-1);
                    else if(i.contains("UUID")) u.UUID=i.substring(6,i.length()-1);
                    if(i.contains("LABEL") && split.length>3){
                        for(int ii=2;ii<split.length-1;ii++){
                            u.label+=" "+split[ii];
                        }
                        u.label=u.label.substring(0,u.label.length()-1);
                    }
                }
                l.add(u);
            }
        }catch(Exception e){
            System.out.println("error de lectura: "+e);
        }finally{
            return l;
        }
    }
    
    public static void main(String[] args) {
        for (Usb i : getUSBDevices()){
            System.out.println("label: "+i.label+" uuid: "+i.UUID+" name: "+i.name);
        }
    }
}
