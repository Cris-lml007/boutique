/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import model.Empleado;
import model.Item;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "HISTORIAL_ITEM", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "HistorialItem.findAll", query = "SELECT h FROM HistorialItem h"),
    @NamedQuery(name = "HistorialItem.findById", query = "SELECT h FROM HistorialItem h WHERE h.id = :id"),
    @NamedQuery(name = "HistorialItem.findByFecha", query = "SELECT h FROM HistorialItem h WHERE h.fecha = :fecha"),
    @NamedQuery(name = "HistorialItem.findByAccion", query = "SELECT h FROM HistorialItem h WHERE h.accion = :accion"),
    @NamedQuery(name = "HistorialItem.findByCampo", query = "SELECT h FROM HistorialItem h WHERE h.campo = :campo")})
public class HistorialItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "ACCION", nullable = false, length = 15)
    private String accion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "CAMPO", nullable = false, length = 12)
    private String campo;
    @Lob
    @Size(max = 65535)
    @Column(name = "VALOR_ANTERIOR", length = 65535)
    private String valorAnterior;
    @Lob
    @Size(max = 65535)
    @Column(name = "VALOR_NUEVO", length = 65535)
    private String valorNuevo;
    @JoinColumn(name = "ITEM", referencedColumnName = "COD", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Item item;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "CI", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empleado empleado;

    public HistorialItem() {
    }

    public HistorialItem(Integer id) {
        this.id = id;
    }

    public HistorialItem(Integer id, Date fecha, String accion, String campo) {
        this.id = id;
        this.fecha = fecha;
        this.accion = accion;
        this.campo = campo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public void setValorNuevo(String valorNuevo) {
        this.valorNuevo = valorNuevo;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof HistorialItem)) {
            return false;
        }
        HistorialItem other = (HistorialItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.boutique.HistorialItem[ id=" + id + " ]";
    }
    
}
