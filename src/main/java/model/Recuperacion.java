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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "RECUPERACION", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Recuperacion.findAll", query = "SELECT r FROM Recuperacion r"),
    @NamedQuery(name = "Recuperacion.findById", query = "SELECT r FROM Recuperacion r WHERE r.id = :id"),
    @NamedQuery(name = "Recuperacion.findByNombre", query = "SELECT r FROM Recuperacion r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Recuperacion.findByLlave", query = "SELECT r FROM Recuperacion r WHERE r.llave = :llave")})
public class Recuperacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false,updatable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(max = 30)
    @Column(name = "NOMBRE", length = 30)
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "LLAVE", nullable = false, length = 32)
    private String llave;
    @JoinColumn(name = "DUENIO", referencedColumnName = "CI", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empleado duenio;

    public Recuperacion() {
    }

    public Recuperacion(Integer id) {
        this.id = id;
    }

    public Recuperacion(Integer id, String llave) {
        this.id = id;
        this.llave = llave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public Empleado getDuenio() {
        return duenio;
    }
    
    public String getDuenioName() {
        return duenio.toString();
    }

    public void setDuenio(Empleado duenio) {
        this.duenio = duenio;
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
        if (!(object instanceof Recuperacion)) {
            return false;
        }
        Recuperacion other = (Recuperacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Recuperacion[ id=" + id + " ]";
    }
    
}
