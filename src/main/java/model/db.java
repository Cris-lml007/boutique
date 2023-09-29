/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author metallica
 */
public class db {
    private final String host="localhost";
    private final String user="root";
    private final String password="";
    private final String database="boutique";
    protected Connection conect=null;
    
    
    public db(){
        try{
            conect=DriverManager.getConnection("jdbc:mysql://"+host+"/"+database+"?user="+user+"&password="+password);
            System.out.println("conexion existosa");
        }catch(SQLException e){
            System.out.println("error al conectar:"+e);
        }
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
    
    public Map<String,String> where(String result[],String condition){
        String nameClass=this.getClass().getSimpleName();
        String sql="select"+String.join(",",result)+" from "+nameClass+" where "+condition;
        try{
            ResultSet r=queryResult(sql);
            Map<String,String>obj=new HashMap<String,String>();
            ResultSetMetaData meta=r.getMetaData();
            for(int i=0;r.next();i++){
                obj.put(meta.getColumnName(i),r.getString(i+1));
            }
            return obj;
        }catch(SQLException e){
            System.out.println("error where: "+e);
            return null;
        }
    }
    
}
