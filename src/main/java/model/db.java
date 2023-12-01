/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
/**
 *
 * @author metallica
 */
public class db {
    private final String host="localhost";
    private final String user="root";
    private final String password="";
    private final String database="boutique";
    protected static Connection conect=null;
    
    
    public db(){
        try{
            conect=DriverManager.getConnection("jdbc:mysql://"+host+"/"+database+"?user="+user+"&password="+password);
            System.out.println("conexion existosa");
        }catch(SQLException e){
            System.out.println("error al conectar:"+e);
        }
    }
    
    public static Connection getConection(){
        return conect;
    }
    
    public boolean queryInput(String sql){
        boolean result=false;
        try{
        PreparedStatement query=conect.prepareStatement(sql);
        if(query.execute()) result=true;
        }catch(SQLException e){
            System.out.println("error al realizar consulta: "+e);
        }finally{
            return result;
        }
    }
    
    public ResultSet queryResult(String sql){
        ResultSet result=null;
        try{
            PreparedStatement query=conect.prepareStatement(sql);
            result=query.executeQuery();
        }catch(SQLException e){
            System.out.println("error al retornar: "+e);
        }finally{
            return result;
        }
    }
    
    
    //return a DefaultListModel with HashMap that have all data query requiered
    public DefaultListModel<Map> where(String result[],String condition){
        DefaultListModel<Map>list=new DefaultListModel<Map>();
        String nameClass=this.getClass().getSimpleName();
        String sql="select "+String.join(",",result)+" from "+nameClass+" where "+condition;
        try{
            ResultSet r=queryResult(sql);
            Map<String,String>obj=new HashMap<String,String>();
            ResultSetMetaData meta=r.getMetaData();
            while(r.next()){
                for(int i=1;i<=r.getMetaData().getColumnCount();i++){
                    obj.put(meta.getColumnName(i),r.getString(i));
                }
                list.addElement(obj);
            }
            return list;
        }catch(SQLException e){
            System.out.println("error where: "+e);
            return null;
        }
    }
    
    protected static DefaultListModel<DefaultListModel> all(String name){
        db c=new db();
        String sql="select * from "+name;
        try{
            ResultSet result=c.queryResult(sql);
            DefaultListModel<DefaultListModel>list=new DefaultListModel<>();
            DefaultListModel<String> obj=new DefaultListModel<>();
            while(result.next()){
                for(int i=1;i<=result.getMetaData().getColumnCount();i++){
                    obj.addElement(result.getString(i));
                }
                list.addElement(obj);
            }
            return list;
        }catch(SQLException e){
            System.out.println("error all: "+e);
            return null;
        }
    }
    
    
    public static void main(String [] args){
        db a=new db();
        String aa[]=new String[1];
        aa[0]="*";
        Map<String,String>w=a.where(aa,"ci=1234").lastElement();
        System.out.println(w.get("usuario"));
    }
    
}
