/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author metallica
 */
public enum TipoItem {
    materiaPrima("Materia Prima"),
    mercaderia("Mercaderia"),
    sinTipo("Sin Tipo"),
    Todo("Todo");
    
    private final String descripcion;
    
    TipoItem(String s){
        this.descripcion=s;
    }
    
    public String getDescripcion(){
        return descripcion;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
