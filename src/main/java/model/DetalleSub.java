/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "DETALLE_SUB", catalog = "boutique1", schema = "")
@NamedQueries({
    @NamedQuery(name = "DetalleSub.findAll", query = "SELECT d FROM DetalleSub d")})
public class DetalleSub implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIO", precision = 10, scale = 2)
    private BigDecimal precio;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @Column(name = "CANT_EXIS")
    private Integer cantExis;
    @JoinColumn(name = "PRODUCTO", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item producto;
    @JoinColumn(name = "SUBMINISTRO", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Subministro subministro;

    public DetalleSub() {
    }

    public DetalleSub(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantExis() {
        return cantExis;
    }

    public void setCantExis(Integer cantExis) {
        this.cantExis = cantExis;
    }

    public Item getProducto() {
        return producto;
    }

    public void setProducto(Item producto) {
        this.producto = producto;
    }

    public Subministro getSubministro() {
        return subministro;
    }

    public void setSubministro(Subministro subministro) {
        this.subministro = subministro;
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
        if (!(object instanceof DetalleSub)) {
            return false;
        }
        DetalleSub other = (DetalleSub) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.DetalleSub[ id=" + id + " ]";
    }
    
}