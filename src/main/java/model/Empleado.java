/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author metallica
 */
@Entity(name = "Empleado")
@Table(name = "empleado")
public class Empleado implements Serializable {
    @Id
    @Column(name = "ci")
    private int ci;
    @Column(name = "apellido",nullable = false)
    private String apellido;
    @Column(name = "nombre",nullable = false)
    private String nombre;
    @Column(name = "rol",nullable = false)
    private int rol;
    @Column(name = "contraseña",nullable = true)
    private String contraseña;
    @Column(name = "usuario",nullable = false,unique = true)
    private String usuario;

    public Empleado() {
    }

    public Empleado(int ci, String apellido, String nombre, int rol, String contraseña, String usuario) {
        this.ci = ci;
        this.apellido = apellido;
        this.nombre = nombre;
        this.rol = rol;
        this.contraseña = contraseña;
        this.usuario = usuario;
    }

    public int getCi() {
        return ci;
    }

    public void setCi(int ci) {
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

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
}
