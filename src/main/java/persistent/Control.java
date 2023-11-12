/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistent;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import model.Almacen;
import model.Empleado;
import model.Localizacion;
import model.Pais;
import model.Rol;

/**
 *
 * @author metallica
 */
public class Control {
    public EntityManagerFactory conexion;
    public AlmacenJpaController almacen;
    //public DestinoJpaController destino;
    public DetalleDisJpaController detalleDistribucion;
    public DetalleSubJpaController detalleSubministro;
    public DistribucionJpaController distribucion;
    public EmpleadoJpaController empleado;
    public ItemJpaController item;
    public LocalizacionJpaController localizacion;
    public PaisJpaController pais;
    //public ProveedorJpaController proveedor;
    public SubministroJpaController subministro;
    public ProveedorDistribuidorJpaController proveedorDis;

    public Control() {
        conexion=Persistence.createEntityManagerFactory("JpaBoutique");
        almacen=new AlmacenJpaController(conexion);
        detalleDistribucion=new DetalleDisJpaController(conexion);
        detalleSubministro=new DetalleSubJpaController(conexion);
        distribucion=new DistribucionJpaController(conexion);
        empleado=new EmpleadoJpaController(conexion);
        item=new ItemJpaController(conexion);
        localizacion=new LocalizacionJpaController(conexion);
        pais=new PaisJpaController(conexion);
        subministro=new SubministroJpaController(conexion);
        proveedorDis=new ProveedorDistribuidorJpaController(conexion);
    }        
    
    public static void main(String args []){
        Control a= new Control();
        Empleado q=a.empleado.findEmpleado(7329034);//new Empleado(7329034,"Abalos","Cristian",Rol.gerente);
        q.setUsuario("cris");
        q.setContraseña("12345");
        Pais p=a.pais.findPais("ARG");
        
        try{
            a.pais.edit(p);
            a.empleado.edit(q);
        }catch(Exception e){
            System.out.println("error "+e);
        }
        q=a.empleado.findEmpleado(7328034);
        /*Empleado q=a.empleado.login("cris", "12345");
        if(q==null) System.out.println("error");
        else{
            System.out.println("mmmm...parece que funciono");
            System.out.println(q.getRol());
        }*/
        
    }
}
