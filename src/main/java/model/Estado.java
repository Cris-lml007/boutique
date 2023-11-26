/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author metallica
 */
public enum Estado {
    inactivo("DESPEDIDO"),
    activo("CONTRATADO"),
    CREADO("CREADO"),
    MODIFICADO("MODIFICADO"),
    ELIMINADO("ELIMINADO");
    
    private final String descripcion;
    
    Estado(String r){
        this.descripcion=r;
    }
    
    public String getEstadoEmpleado(){
        return descripcion;
    }
}
