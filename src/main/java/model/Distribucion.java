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
@Table(name = "DISTRIBUCION", catalog = "boutique1", schema = "")
@NamedQueries({
    @NamedQuery(name = "Distribucion.findAll", query = "SELECT d FROM Distribucion d")})
public class Distribucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;
    @OneToMany(mappedBy = "distribucion", fetch = FetchType.LAZY)
    private List<DetalleDis> detalleDisList;
    @JoinColumn(name = "DESTINO", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Destino destino;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "CI")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;
    @JoinColumn(name = "ENCARGADO", referencedColumnName = "CI")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado encargado;

    public Distribucion() {
    }

    public Distribucion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DetalleDis> getDetalleDisList() {
        return detalleDisList;
    }

    public void setDetalleDisList(List<DetalleDis> detalleDisList) {
        this.detalleDisList = detalleDisList;
    }

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Empleado getEncargado() {
        return encargado;
    }

    public void setEncargado(Empleado encargado) {
        this.encargado = encargado;
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
        if (!(object instanceof Distribucion)) {
            return false;
        }
        Distribucion other = (Distribucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Distribucion[ id=" + id + " ]";
    }
    
}