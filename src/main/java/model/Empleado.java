/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import model.Estado;
import model.Rol;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "EMPLEADO", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findByCi", query = "SELECT e FROM Empleado e WHERE e.ci = :ci"),
    @NamedQuery(name = "Empleado.findByApellido", query = "SELECT e FROM Empleado e WHERE e.apellido = :apellido"),
    @NamedQuery(name = "Empleado.findByNombre", query = "SELECT e FROM Empleado e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleado.findByRol", query = "SELECT e FROM Empleado e WHERE e.rol = :rol"),
    @NamedQuery(name = "Empleado.findByContrase\u00f1a", query = "SELECT e FROM Empleado e WHERE e.contrase\u00f1a = :contrase\u00f1a"),
    @NamedQuery(name = "Empleado.findByUsuario", query = "SELECT e FROM Empleado e WHERE e.usuario = :usuario")})
public class Empleado implements Serializable {

    @Column(name = "ROL")
    @Enumerated(EnumType.ORDINAL)
    private Rol rol;
    @Column(name = "ACTIVO")
    @Enumerated(EnumType.ORDINAL)
    private Estado activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "duenio", fetch = FetchType.LAZY)
    private List<Recuperacion> recuperacionList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CI", nullable = false)
    private Integer ci;
    @Column(name = "APELLIDO", length = 30)
    private String apellido;
    @Column(name = "NOMBRE", length = 30)
    private String nombre;
    @Column(name = "CONTRASE\u00d1A", length = 32)
    private String contraseña;
    @Column(name = "USUARIO", length = 30)
    private String usuario;
    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<Subministro> subministroList;
    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<Distribucion> distribucionList;
    @OneToMany(mappedBy = "encargado", fetch = FetchType.LAZY)
    private List<Distribucion> distribucionList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleado", fetch = FetchType.LAZY)
    private List<HistorialItem> historialItemList;

    public Empleado() {
    }

    public Empleado(Integer ci, String apellido, String nombre, Rol rol) {
        this.ci = ci;
        this.apellido = apellido;
        this.nombre = nombre;
        this.rol = rol;
    }

    public Empleado(Integer ci) {
        this.ci = ci;
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
        this.apellido = apellido.toUpperCase();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }


    /*public String getContraseña() {
        return contraseña;
    }*/

    public void setContraseña(String contraseña) {
        this.contraseña = md5.getMD5Hash(contraseña);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public boolean equalContraseña(String pass){
        return contraseña.equals(pass) ? true : false;
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
        //return "model.Empleado[ ci=" + ci + " ]";
        return apellido+" "+nombre;
    }


    public List<HistorialItem> getHistorialItemList() {
        return historialItemList;
    }

    public void setHistorialItemList(List<HistorialItem> historialItemList) {
        this.historialItemList = historialItemList;
    }


    public String getActivoName(){
        return activo.getEstadoEmpleado();
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Estado getActivo() {
        return activo;
    }

    public void setActivo(Estado activo) {
        this.activo = activo;
    }

    public List<Recuperacion> getRecuperacionList() {
        return recuperacionList;
    }

    public void setRecuperacionList(List<Recuperacion> recuperacionList) {
        this.recuperacionList = recuperacionList;
    }
    
}
