/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "ALMACEN", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Almacen.findAll", query = "SELECT a FROM Almacen a"),
    @NamedQuery(name = "Almacen.findByCod", query = "SELECT a FROM Almacen a WHERE a.cod = :cod"),
    @NamedQuery(name = "Almacen.findByNombre", query = "SELECT a FROM Almacen a WHERE a.nombre = :nombre")})
public class Almacen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD", nullable = false)
    private Integer cod;
    @Column(name = "NOMBRE", length = 30)
    private String nombre;
    @OneToMany(mappedBy = "almacen", fetch = FetchType.LAZY)
    private List<Subministro> subministroList;
    @JoinColumn(name = "ORIGEN", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Localizacion origen;
    
    @Transient
    int mode=0;

    public Almacen() {
    }

    public Almacen(Integer cod) {
        this.cod = cod;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public List<Subministro> getSubministroList() {
        return subministroList;
    }

    public void setSubministroList(List<Subministro> subministroList) {
        this.subministroList = subministroList;
    }

    public Localizacion getOrigen() {
        return origen;
    }

    public void setOrigen(Localizacion origen) {
        this.origen = origen;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Almacen)) {
            return false;
        }
        Almacen other = (Almacen) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }
    
    public void changedModeToString(int i){
        this.mode=i;
    }
    
    public int getChangedToString(){
        return mode;
    }

    @Override
    public String toString() {
        //return "model.Almacen[ cod=" + cod + " ]";
        switch (mode) {
            case 0:
                return cod + " - " +nombre + " - " + origen.getCiudad();
            case 1:
                return nombre;
            default:
                throw new AssertionError();
        }
        
    }
    
}
