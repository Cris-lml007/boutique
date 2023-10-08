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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "detalle_sub")
public class DetalleSubministro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @OneToOne
    @Column(name = "subministro")
    private Subministro subministro;
    @OneToOne
    @Column(name = "producto")
    private Item producto;
    @Column(name = "precio")
    private float precio;
    @Column(name = "cantidad")
    private int cantidad;
    @Column(name = "cant_exis")
    private int cantExistente;

    public DetalleSubministro() {
    }

    public DetalleSubministro(int id, Subministro subministro, Item producto, float precio, int cantidad, int cantExistente) {
        this.id = id;
        this.subministro = subministro;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.cantExistente = cantExistente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subministro getSubministro() {
        return subministro;
    }

    public void setSubministro(Subministro subministro) {
        this.subministro = subministro;
    }

    public Item getProducto() {
        return producto;
    }

    public void setProducto(Item producto) {
        this.producto = producto;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantExistente() {
        return cantExistente;
    }

    public void setCantExistente(int cantExistente) {
        this.cantExistente = cantExistente;
    }
    
    
}
