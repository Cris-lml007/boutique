/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author metallica
 */
public class empleado extends db{
    private boolean active=false;
    public int ci;
    public String apellido;
    public String nombre;
    private int rol;
    private String contraseña;

    public void setRol(int rol) {
        this.rol = rol;
    }
    
    public empleado(){
        super();
    }
    
    public empleado(int ci){
        super();
        try{
            String sql="select * from emplado where ci="+ci;
            ResultSet r=queryResult(sql);
            while(r.next()){
                this.ci=r.getInt("ci");
                this.apellido=r.getString("apellido");
                this.nombre=r.getString("nombre");
                this.rol=r.getInt("rol");
            }
            active=true;
        }catch(SQLException e){
            System.out.println("error empleado: "+e);
        }
    }
    
    public boolean save(){
        if(!active){
            String sql="insert into empleado values("+ci+",";
            sql+=apellido+",";
            sql+=nombre+",";
            sql+=rol+",";
            sql+=contraseña+");";
            return queryInput(sql);
        }else{
            String sql="update empleado set ci="+ci+",";
            sql+="apellido="+apellido+",";
            sql+="nombre="+nombre+",";
            sql+="rol="+rol+",";
            sql+="contraseña="+contraseña+" where ci="+ci+";";
            return queryInput(sql);
        }
    }
    
    public void setCi(int ci){
        this.ci=ci;
    }
    
    public void setApellido(String text){
        this.apellido=text;
    }
    
    public void setNombre(String text){
        this.nombre=text;
    }

    public void setContraseña(String text){
        this.contraseña=md5.getMD5Hash(text);
    }
    
    public int getCi(){
        return ci;
    }
    
    public String getApellido(){
        return apellido;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getRol(){
        return rol;
    }
    
    public void where(String result[],String condition[]){
        String sql="select"+String.join(",",result)+" where "+String.join(" ",condition);
        try{
            ResultSet r=queryResult(sql);
            Map<String,String>obj=new HashMap<String,String>();
            ResultSetMetaData meta=r.getMetaData();
            for(int i=0;r.next();i++){
                obj.put(meta.getColumnName(ci),r.getString(1));
            }
        }catch(SQLException e){
            System.out.println("error where: "+e);
        }
    }
    
    
    public static void main(String []args){
        Map<String,String>a=new HashMap<String,String>();
        a.put("as", "hola");
        System.out.println(a.get("as"));
    }
}
