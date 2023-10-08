/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "localizacion")
public class Localizacion implements Serializable {
    @Id
    @Column(name = "cod")
    String cod;
    @OneToOne
    @Column(name = "pais")
    Pais pais;
    @Column(name = "ciudad")
    String ciudad;

    public Localizacion() {
    }

    public Localizacion(String cod, Pais pais, String ciudad) {
        this.cod = cod;
        this.pais = pais;
        this.ciudad = ciudad;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    
}
