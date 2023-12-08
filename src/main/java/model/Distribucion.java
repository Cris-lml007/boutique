/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "DISTRIBUCION", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Distribucion.findAll", query = "SELECT d FROM Distribucion d"),
    @NamedQuery(name = "Distribucion.findById", query = "SELECT d FROM Distribucion d WHERE d.id = :id"),
    @NamedQuery(name = "Distribucion.findByFecha", query = "SELECT d FROM Distribucion d WHERE d.fecha = :fecha"),
    @NamedQuery(name = "Distribucion.findByDescripcion", query = "SELECT d FROM Distribucion d WHERE d.descripcion = :descripcion"),
    @NamedQuery(name = "Distribucion.totalByEmpleado",query = "SELECT COUNT(d) FROM Distribucion d WHERE d.empleado = :emp")})
public class Distribucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false,updatable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;
    @OneToMany(mappedBy = "distribucion", fetch = FetchType.LAZY)
    private List<DetalleDis> detalleDisList;
    @JoinColumn(name = "DESTINO", referencedColumnName = "COD")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProveedorDistribuidor destino;
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

    public Distribucion(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(fecha);
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

    public List<DetalleDis> getDetalleDisList() {
        return detalleDisList;
    }

    public void setDetalleDisList(List<DetalleDis> detalleDisList) {
        this.detalleDisList = detalleDisList;
    }
    
    public String getDestinoName(){
        return destino.getNombre();
    }

    public ProveedorDistribuidor getDestino() {
        return destino;
    }

    public void setDestino(ProveedorDistribuidor destino) {
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
    
    public double getTotal(){
        double t=0;
        for (DetalleDis i : this.detalleDisList){
            t+=i.getSubtotal();
        }
        return t;
    }
    
}
