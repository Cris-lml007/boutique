/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "pais")
public class Pais implements Serializable {
    @Id
    @Column(name = "cod")
    String cod;
    @Column(name = "nombre")
    String pais;

    public Pais() {
    }

    public Pais(String cod, String pais) {
        this.cod = cod;
        this.pais = pais;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
    
    
}
