/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.*;
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
    
}
