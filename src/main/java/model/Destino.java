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
@Table(name = "destino")
public class Destino implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "tipo")
    private int tipo;
    @OneToOne
    @Column(name = "origen")
    private Localizacion cidudad;

    public Destino() {
    }

    public Destino(int id, String nombre, int tipo, Localizacion cidudad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cidudad = cidudad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Localizacion getCidudad() {
        return cidudad;
    }

    public void setCidudad(Localizacion cidudad) {
        this.cidudad = cidudad;
    }
    
    
}
