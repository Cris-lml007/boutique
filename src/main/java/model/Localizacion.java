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

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "LOCALIZACION", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Localizacion.findAll", query = "SELECT l FROM Localizacion l"),
    @NamedQuery(name = "Localizacion.findByCod", query = "SELECT l FROM Localizacion l WHERE l.cod = :cod"),
    @NamedQuery(name = "Localizacion.findByCiudad", query = "SELECT l FROM Localizacion l WHERE l.ciudad = :ciudad")})
public class Localizacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD", nullable = false, length = 10)
    private String cod;
    @Column(name = "CIUDAD", length = 60)
    private String ciudad;
    @JoinColumn(name = "PAIS", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Pais pais;
    @OneToMany(mappedBy = "origen", fetch = FetchType.LAZY)
    private List<Almacen> almacenList;
    @OneToMany(mappedBy = "origen", fetch = FetchType.LAZY)
    private List<ProveedorDistribuidor> proveedorDistribuidorList;

    public Localizacion() {
    }

    public Localizacion(String cod) {
        this.cod = cod;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public List<Almacen> getAlmacenList() {
        return almacenList;
    }

    public void setAlmacenList(List<Almacen> almacenList) {
        this.almacenList = almacenList;
    }

    public List<ProveedorDistribuidor> getProveedorDistribuidorList() {
        return proveedorDistribuidorList;
    }

    public void setProveedorDistribuidorList(List<ProveedorDistribuidor> proveedorDistribuidorList) {
        this.proveedorDistribuidorList = proveedorDistribuidorList;
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
        if (!(object instanceof Localizacion)) {
            return false;
        }
        Localizacion other = (Localizacion) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Localizacion[ cod=" + cod + " ]";
    }
    
}
