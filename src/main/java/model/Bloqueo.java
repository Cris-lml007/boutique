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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "BLOQUEO", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Bloqueo.findAll", query = "SELECT b FROM Bloqueo b"),
    @NamedQuery(name = "Bloqueo.findById", query = "SELECT b FROM Bloqueo b WHERE b.id = :id"),
    @NamedQuery(name = "Bloqueo.findByVal", query = "SELECT b FROM Bloqueo b WHERE b.val = :val"),
    @NamedQuery(name = "Bloqueo.findByFechaA", query = "SELECT b FROM Bloqueo b WHERE b.fechaA = :fechaA")})
public class Bloqueo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VAL", nullable = false)
    private int val;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_A", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaA;

    public Bloqueo() {
    }

    public Bloqueo(Integer id) {
        this.id = id;
    }

    public Bloqueo(Integer id, int val, Date fechaA) {
        this.id = id;
        this.val = val;
        this.fechaA = fechaA;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Date getFechaA() {
        return fechaA;
    }

    public void setFechaA(Date fechaA) {
        this.fechaA = fechaA;
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
        if (!(object instanceof Bloqueo)) {
            return false;
        }
        Bloqueo other = (Bloqueo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Bloqueo[ id=" + id + " ]";
    }
    
}
