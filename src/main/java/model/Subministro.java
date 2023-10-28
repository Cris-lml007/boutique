/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "SUBMINISTRO", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Subministro.findAll", query = "SELECT s FROM Subministro s"),
    @NamedQuery(name = "Subministro.findByCod", query = "SELECT s FROM Subministro s WHERE s.cod = :cod"),
    @NamedQuery(name = "Subministro.findByFecha", query = "SELECT s FROM Subministro s WHERE s.fecha = :fecha"),
    @NamedQuery(name = "Subministro.findByDescripcion", query = "SELECT s FROM Subministro s WHERE s.descripcion = :descripcion")})
public class Subministro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD", nullable = false)
    private Integer cod;
    @Basic(optional = false)
    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;
    @JoinColumn(name = "ALMACEN", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private Almacen almacen;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "CI")
    @ManyToOne(fetch = FetchType.LAZY)
    private Empleado empleado;
    @JoinColumn(name = "PROVEEDOR", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProveedorDistribuidor proveedor;
    @OneToMany(mappedBy = "subministro", fetch = FetchType.LAZY)
    private List<DetalleSub> detalleSubList;

    public Subministro() {
    }

    public Subministro(Integer cod) {
        this.cod = cod;
    }

    public Subministro(Integer cod, Date fecha) {
        this.cod = cod;
        this.fecha = fecha;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public ProveedorDistribuidor getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorDistribuidor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleSub> getDetalleSubList() {
        return detalleSubList;
    }

    public void setDetalleSubList(List<DetalleSub> detalleSubList) {
        this.detalleSubList = detalleSubList;
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
        if (!(object instanceof Subministro)) {
            return false;
        }
        Subministro other = (Subministro) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Subministro[ cod=" + cod + " ]";
    }
    
}
