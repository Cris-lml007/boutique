/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author metallica
 */
@Entity
@Table(name = "ITEM", catalog = "boutique", schema = "")
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
    @NamedQuery(name = "Item.findByCod", query = "SELECT i FROM Item i WHERE i.cod = :cod"),
    @NamedQuery(name = "Item.findByNombre", query = "SELECT i FROM Item i WHERE i.nombre = :nombre"),
    @NamedQuery(name = "Item.findByPrecio", query = "SELECT i FROM Item i WHERE i.precio = :precio"),
    @NamedQuery(name = "Item.findByDescripcion", query = "SELECT i FROM Item i WHERE i.descripcion = :descripcion"),
    @NamedQuery(name = "Item.findbyCantidad", query = "SELECT i FROM Item i WHERE i.cantidad = :cantidad")})
public class Item implements Serializable {

    @Column(name = "TIPO")
    @Enumerated(EnumType.ORDINAL)
    private TipoItem tipo;
    @Column(name = "ACTIVO")
    @Enumerated(EnumType.ORDINAL)
    private Estado activo=Estado.activo;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "COD", nullable = true, updatable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cod;
    @Column(name = "NOMBRE", length = 30)
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRECIO", precision = 10, scale = 2)
    private BigDecimal precio;
    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;
    @Column(name = "CANTIDAD",updatable = true)
    private int cantidad=0;
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<DetalleSub> detalleSubList;
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private List<DetalleDis> detalleDisList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", fetch = FetchType.LAZY)
    private List<HistorialItem> historialItemList;    
    
    @Transient
    int mode=0;

    public Item() {
    }
    
    public Item(Item i){
        this.cod=i.cod;
        this.nombre=i.nombre;
        this.precio=i.precio;
        this.descripcion=i.descripcion;
        this.tipo=i.tipo;
        this.activo=i.activo;
        this.detalleSubList=i.detalleSubList;
        this.detalleDisList=i.detalleDisList;
        this.historialItemList=i.historialItemList;
    }
    
    public static Item newInstance(Item i){
        return new Item(i);
    }

    public Item(Integer cod) {
        this.cod = cod;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<DetalleSub> getDetalleSubList() {
        return detalleSubList;
    }

    public void setDetalleSubList(List<DetalleSub> detalleSubList) {
        this.detalleSubList = detalleSubList;
    }

    public List<DetalleDis> getDetalleDisList() {
        return detalleDisList;
    }

    public void setDetalleDisList(List<DetalleDis> detalleDisList) {
        this.detalleDisList = detalleDisList;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    public void changedModeToString(int i){
        this.mode=i;
    }
    
    public int getChangedToString(){
        return mode;
    }
    
    @Override
    public String toString() {
        //return "model.Item[ cod=" + cod + " ]";
        //return cod + " - " + nombre + " - " + precio + " - " + descripcion;
        switch(mode){
            case 0:
                return cod + " - " + nombre + " - " + precio + " - " + descripcion;
            case 1:
                return nombre;
            default:
                throw new AssertionError();
        }
    }

    
    public String getTipoName(){
        return tipo.getDescripcion();
    }
    
    public String getTipoDesc(){
        return tipo.getDescripcion();
    }


    public List<HistorialItem> getHistorialItemList() {
        return historialItemList;
    }

    public void setHistorialItemList(List<HistorialItem> historialItemList) {
        this.historialItemList = historialItemList;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    public void setTipo(TipoItem tipo) {
        this.tipo = tipo;
    }

    public Estado getActivo() {
        return activo;
    }

    public void setActivo(Estado activo) {
        this.activo = activo;
    }
    
}
