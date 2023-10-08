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
@Table(name = "almacen")
public class Almacen implements Serializable {
    @Id
    @Column(name = "cod")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cod;
    @Column(name = "nombre")
    private String nombre;
    @OneToOne
    @Column(name = "origen")
    Localizacion ciudad;

    public Almacen() {
    }

    public Almacen(int cod, String nombre, Localizacion ciudad) {
        this.cod = cod;
        this.nombre = nombre;
        this.ciudad = ciudad;
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

    public Localizacion getCiudad() {
        return ciudad;
    }

    public void setCiudad(Localizacion ciudad) {
        this.ciudad = ciudad;
    }
}
