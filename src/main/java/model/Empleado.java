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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "EMPLEADO", catalog = "boutique1", schema = "")
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CI", nullable = false)
    private Integer ci;
    @Column(name = "APELLIDO", length = 30)
    private String apellido;
    @Column(name = "NOMBRE", length = 30)
    private String nombre;
    @Column(name = "ROL")
    private Integer rol;
    @Column(name = "CONTRASE\u00d1A", length = 32)
    private String contraseña;
    @Basic(optional = false)
    @Column(name = "USUARIO", nullable = false, length = 30)
    private String usuario;
    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<Subministro> subministroList;
    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<Distribucion> distribucionList;
    @OneToMany(mappedBy = "encargado", fetch = FetchType.LAZY)
    private List<Distribucion> distribucionList1;

    public Empleado() {
    }

    public Empleado(Integer ci) {
        this.ci = ci;
    }

    public Empleado(Integer ci, String usuario) {
        this.ci = ci;
        this.usuario = usuario;
    }

    public Integer getCi() {
        return ci;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getRol() {
        return rol;
    }

    public void setRol(Integer rol) {
        this.rol = rol;
    }

    public void setContraseña(String contraseña) {
        
        this.contraseña = md5.getMD5Hash(contraseña);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
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

    public List<Distribucion> getDistribucionList1() {
        return distribucionList1;
    }

    public void setDistribucionList1(List<Distribucion> distribucionList1) {
        this.distribucionList1 = distribucionList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ci != null ? ci.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.ci == null && other.ci != null) || (this.ci != null && !this.ci.equals(other.ci))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Empleado[ ci=" + ci + " ]";
    }
    
}
