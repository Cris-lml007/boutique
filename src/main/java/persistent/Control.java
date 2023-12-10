/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.SqlResultSetMapping;
import model.Empleado;
import model.Item;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author metallica
 */
public class Control {
    public EntityManagerFactory conexion;
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
    public HistorialItemJpaController historialItem;

    public Control() {
        conexion=Persistence.createEntityManagerFactory("JpaBoutique");
        detalleDistribucion=new DetalleDisJpaController(conexion);
        detalleSubministro=new DetalleSubJpaController(conexion);
        distribucion=new DistribucionJpaController(conexion);
        empleado=new EmpleadoJpaController(conexion);
        item=new ItemJpaController(conexion);
        localizacion=new LocalizacionJpaController(conexion);
        pais=new PaisJpaController(conexion);
        subministro=new SubministroJpaController(conexion);
        proveedorDis=new ProveedorDistribuidorJpaController(conexion);
        historialItem=new HistorialItemJpaController(conexion);
    }
    
    public long totalSubministroByEmpleado(Empleado e){
        return (long) subministro.getEntityManager().createNamedQuery("Subministro.totalByEmpleado")
                .setParameter("emp", e)
                .setHint(QueryHints.READ_ONLY, true)
                .getSingleResult();
    }
    
    public long totalDistribucionByEmpleado(Empleado e){
        return (long) distribucion.getEntityManager().createNamedQuery("Distribucion.totalByEmpleado")
                .setParameter("emp", e)
                .setHint(QueryHints.READ_ONLY, true)
                .getSingleResult();
    }
    
    public List<Item> itemExisting(){
        return item.getEntityManager().createNamedQuery("Item.findByCantidadExisting")
                .setHint(QueryHints.READ_ONLY, true)
                .getResultList();
    }
    
    public List<TotalByDateDTO> getTotalByDateSubministro(Integer a,Integer m) {
        List<Object[]> results = subministro.getEntityManager().createNativeQuery(
                "SELECT DATE(s.FECHA) AS fecha, SUM(d.PRECIO * d.CANTIDAD) AS total "
                        + "FROM SUBMINISTRO s LEFT JOIN DETALLE_SUB d ON d.SUBMINISTRO = s.COD "
                        + "WHERE "
                        + "CASE WHEN ?a IS NULL THEN TRUE ELSE EXTRACT(YEAR FROM s.FECHA)= ?a END "
                        + "AND "
                        + "CASE WHEN ?m IS NULL THEN TRUE ELSE EXTRACT(MONTH FROM s.FECHA) = ?m END "
                        + "GROUP BY DATE(s.FECHA)",
                "TotalByDateMapping"
        )
                .setParameter("a", a)
                .setParameter("m", m)
                .getResultList();

        List<TotalByDateDTO> totalByDateDTOList = new ArrayList<>();

        for (Object[] result : results) {
            Date fecha = (Date) result[0];
            BigDecimal total = (BigDecimal) result[1];

            TotalByDateDTO dto = new TotalByDateDTO(fecha, total);
            totalByDateDTOList.add(dto);
        }

        return totalByDateDTOList;
    }
    
        public List<TotalByDateDTO> getTotalByDateDistribucion(Integer a,Integer m) {
        List<Object[]> results = subministro.getEntityManager().createNativeQuery(
                "SELECT DATE(s.FECHA) AS fecha, SUM(i.PRECIO * d.CANTIDAD) AS total "
                        + "FROM DISTRIBUCION s "
                        + "LEFT JOIN DETALLE_DIS d ON d.DISTRIBUCION = s.ID "
                        + "LEFT JOIN ITEM i ON d.PRODUCTO = i.COD "
                        + "WHERE "
                        + "CASE WHEN ?a IS NULL THEN TRUE ELSE EXTRACT(YEAR FROM s.FECHA)= ?a END "
                        + "AND "
                        + "CASE WHEN ?m IS NULL THEN TRUE ELSE EXTRACT(MONTH FROM s.FECHA) = ?m END "
                        + "GROUP BY DATE(s.FECHA)",
                "TotalByDateMapping"
        )
                .setParameter("a", a)
                .setParameter("m", m)
                .getResultList();

        List<TotalByDateDTO> totalByDateDTOList = new ArrayList<>();

        for (Object[] result : results) {
            Date fecha = (Date) result[0];
            BigDecimal total = (BigDecimal) result[1];

            TotalByDateDTO dto = new TotalByDateDTO(fecha, total);
            totalByDateDTOList.add(dto);
        }

        return totalByDateDTOList;
    }


    @SqlResultSetMapping(
        name = "TotalByDateMapping",
        classes = @ConstructorResult(
            targetClass = TotalByDateDTO.class,
            columns = {
                @ColumnResult(name = "fecha", type = Date.class),
                @ColumnResult(name = "total", type = BigDecimal.class)
            }
        )
    )
    public final class TotalByDateDTO {
        private Date fecha;
        private BigDecimal total;

        public TotalByDateDTO(Date fecha, BigDecimal total) {
            this.fecha = fecha;
            this.total = total;
        }

        public Date getFecha() {
            return fecha;
        }

        public BigDecimal getTotal() {
            return total;
        }

        @Override
        public String toString() {
            return "TotalByDateDTO{" + "fecha=" + fecha + ", total=" + total + '}';
        }
    }

    public static void main(String[] args) {
        Control a = new Control();
        List<TotalByDateDTO> l = a.getTotalByDateDistribucion(2023,null);
        if (!l.isEmpty()) {
            System.out.println("No está vacía: " + l);
            for (TotalByDateDTO i : l) {
                System.out.println(i);
            }
        }
    }
}
