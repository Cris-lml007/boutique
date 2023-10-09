/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author metallica
 */
public class Control {
    public EntityManagerFactory conexion;
    public AlmacenJpaController almacen;
    public DestinoJpaController destino;
    public DetalleDisJpaController detalleDistribucion;
    public DetalleSubJpaController detalleSubministro;
    public DistribucionJpaController distribucion;
    public EmpleadoJpaController empleado;
    public ItemJpaController item;
    public LocalizacionJpaController localizacion;
    public PaisJpaController pais;
    public ProveedorJpaController proveedor;
    public SubministroJpaController subministro;

    public Control() {
        conexion=Persistence.createEntityManagerFactory("JpaBoutique");
        almacen=new AlmacenJpaController(conexion);
        destino=new DestinoJpaController(conexion);
        detalleDistribucion=new DetalleDisJpaController(conexion);
        detalleSubministro=new DetalleSubJpaController(conexion);
        distribucion=new DistribucionJpaController(conexion);
        empleado=new EmpleadoJpaController(conexion);
        item=new ItemJpaController(conexion);
        localizacion=new LocalizacionJpaController(conexion);
        pais=new PaisJpaController(conexion);
        proveedor=new ProveedorJpaController(conexion);
        subministro=new SubministroJpaController(conexion);
    }        
    
    
}
