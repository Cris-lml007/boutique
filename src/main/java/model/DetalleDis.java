/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "DETALLE_DIS", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "DetalleDis.findAll", query = "SELECT d FROM DetalleDis d"),
    @NamedQuery(name = "DetalleDis.findById", query = "SELECT d FROM DetalleDis d WHERE d.id = :id"),
    @NamedQuery(name = "DetalleDis.findByCantidad", query = "SELECT d FROM DetalleDis d WHERE d.cantidad = :cantidad")})
public class DetalleDis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @JoinColumn(name = "DISTRIBUCION", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Distribucion distribucion;
    @JoinColumn(name = "PRODUCTO", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item producto;

    public DetalleDis() {
    }

    public DetalleDis(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Distribucion getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(Distribucion distribucion) {
        this.distribucion = distribucion;
    }

    public Item getProducto() {
        return producto;
    }

    public void setProducto(Item producto) {
        this.producto = producto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleDis)) {
            return false;
        }
        DetalleDis other = (DetalleDis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.DetalleDis[ id=" + id + " ]";
    }
    
}
