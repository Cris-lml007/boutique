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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "PROVEEDOR_DISTRIBUIDOR", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "ProveedorDistribuidor.findAll", query = "SELECT p FROM ProveedorDistribuidor p"),
    @NamedQuery(name = "ProveedorDistribuidor.findByCod", query = "SELECT p FROM ProveedorDistribuidor p WHERE p.cod = :cod"),
    @NamedQuery(name = "ProveedorDistribuidor.findByNombre", query = "SELECT p FROM ProveedorDistribuidor p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "ProveedorDistribuidor.findByTipo", query = "SELECT p FROM ProveedorDistribuidor p WHERE p.tipo = :tipo")})
public class ProveedorDistribuidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD", nullable = false)
    private Integer cod;
    @Column(name = "NOMBRE", length = 60)
    private String nombre;
    @Column(name = "TIPO")
    @Enumerated(EnumType.ORDINAL)
    private Tipo tipo;
    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<Subministro> subministroList;
    @OneToMany(mappedBy = "destino", fetch = FetchType.LAZY)
    private List<Distribucion> distribucionList;
    @JoinColumn(name = "ORIGEN", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Localizacion origen;

    public ProveedorDistribuidor() {
    }

    public ProveedorDistribuidor(Integer cod) {
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
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public List<Subministro> getSubministroList() {
        return subministroList;
    }

    public void setSubministroList(List<Subministro> subministroList) {
        this.subministroList = subministroList;
    }

    public List<Distribucion> getDistribucionList() {
        return distribucionList;
    }

    public void setDistribucionList(List<Distribucion> distribucionList) {
        this.distribucionList = distribucionList;
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
        if (!(object instanceof ProveedorDistribuidor)) {
            return false;
        }
        ProveedorDistribuidor other = (ProveedorDistribuidor) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ProveedorDistribuidor[ cod=" + cod + " ]";
    }
    
}
