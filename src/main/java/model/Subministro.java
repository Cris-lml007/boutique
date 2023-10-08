/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "subministro")
public class Subministro implements Serializable {
    @Id
    @Column(name = "cod")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cod;
    @OneToOne
    @Column(name = "empleado")
    private Empleado empleado;
    @OneToOne
    @Column(name = "proveedor")
    private Proveedor proveedor;
    @OneToOne
    @Column(name = "almacen")
    private Almacen almacen;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "descripcion")
    private String descripcion;

    public Subministro() {
    }

    public Subministro(int cod, Empleado empleado, Proveedor proveedor, Almacen almacen, Date fecha, String descripcion) {
        this.cod = cod;
        this.empleado = empleado;
        this.proveedor = proveedor;
        this.almacen = almacen;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
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
    
    
}
