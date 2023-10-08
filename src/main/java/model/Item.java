/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "item")
public class Item implements Serializable {
    @Id
    @Column(name = "cod")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cod;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "precio")
    private float precio;
    @Column(name = "descripcion")
    private String descripcion;

    public Item() {
    }

    public Item(int cod, String nombre, float precio, String descripcion) {
        this.cod = cod;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
