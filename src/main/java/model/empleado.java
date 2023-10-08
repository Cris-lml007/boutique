/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;

/**
 *
 * @author metallica
 */
public class empleado extends db{
    private boolean active=false;
    protected int ci;
    protected String apellido;
    protected String nombre;
    protected int rol;
    private String contraseña;
    protected String usuario;
    
    public void setUsuario(String txt){
        this.usuario=txt;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
    
    public empleado(){
        super();
    }
    
    public empleado(int ci){
        super();
        try{
            String sql="select * from empleado where ci="+ci;
            ResultSet r=queryResult(sql);
            while(r.next()){
                this.ci=r.getInt("ci");
                this.apellido=r.getString("apellido");
                this.nombre=r.getString("nombre");
                this.rol=r.getInt("rol");
                this.usuario=r.getString("usuario");
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
    
    public static empleado[] all(){
        DefaultListModel<DefaultListModel> list=db.all("empleado");
        empleado r[]=new empleado[list.size()];
        for(int i=0;i<list.size();i++){
            DefaultListModel<String> obj=list.get(i);
            r[i]=new empleado();
            r[i].ci=Integer.parseInt(obj.get(i));
            r[i].nombre=obj.get(1);
            r[i].apellido=obj.get(2);
            r[i].rol=Integer.parseInt(obj.get(3));
            r[i].usuario=obj.get(5).toString();
        }
        return r;
    }
    
    public static void main(String []args){
        empleado a[]=empleado.all();
        /*String aa[]=new String[2];
        aa[0]="ci";
        aa[1]="nombre";*/
        System.out.println(a[0].getNombre());
    }
}
