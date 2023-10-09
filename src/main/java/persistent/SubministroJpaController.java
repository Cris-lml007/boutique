/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistent;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Almacen;
import model.Empleado;
import model.Proveedor;
import model.DetalleSub;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Subministro;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class SubministroJpaController implements Serializable {

    public SubministroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Subministro subministro) throws PreexistingEntityException, Exception {
        if (subministro.getDetalleSubList() == null) {
            subministro.setDetalleSubList(new ArrayList<DetalleSub>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen almacen = subministro.getAlmacen();
            if (almacen != null) {
                almacen = em.getReference(almacen.getClass(), almacen.getCod());
                subministro.setAlmacen(almacen);
            }
            Empleado empleado = subministro.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getCi());
                subministro.setEmpleado(empleado);
            }
            Proveedor proveedor = subministro.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getCod());
                subministro.setProveedor(proveedor);
            }
            List<DetalleSub> attachedDetalleSubList = new ArrayList<DetalleSub>();
            for (DetalleSub detalleSubListDetalleSubToAttach : subministro.getDetalleSubList()) {
                detalleSubListDetalleSubToAttach = em.getReference(detalleSubListDetalleSubToAttach.getClass(), detalleSubListDetalleSubToAttach.getId());
                attachedDetalleSubList.add(detalleSubListDetalleSubToAttach);
            }
            subministro.setDetalleSubList(attachedDetalleSubList);
            em.persist(subministro);
            if (almacen != null) {
                almacen.getSubministroList().add(subministro);
                almacen = em.merge(almacen);
            }
            if (empleado != null) {
                empleado.getSubministroList().add(subministro);
                empleado = em.merge(empleado);
            }
            if (proveedor != null) {
                proveedor.getSubministroList().add(subministro);
                proveedor = em.merge(proveedor);
            }
            for (DetalleSub detalleSubListDetalleSub : subministro.getDetalleSubList()) {
                Subministro oldSubministroOfDetalleSubListDetalleSub = detalleSubListDetalleSub.getSubministro();
                detalleSubListDetalleSub.setSubministro(subministro);
                detalleSubListDetalleSub = em.merge(detalleSubListDetalleSub);
                if (oldSubministroOfDetalleSubListDetalleSub != null) {
                    oldSubministroOfDetalleSubListDetalleSub.getDetalleSubList().remove(detalleSubListDetalleSub);
                    oldSubministroOfDetalleSubListDetalleSub = em.merge(oldSubministroOfDetalleSubListDetalleSub);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSubministro(subministro.getCod()) != null) {
                throw new PreexistingEntityException("Subministro " + subministro + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Subministro subministro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subministro persistentSubministro = em.find(Subministro.class, subministro.getCod());
            Almacen almacenOld = persistentSubministro.getAlmacen();
            Almacen almacenNew = subministro.getAlmacen();
            Empleado empleadoOld = persistentSubministro.getEmpleado();
            Empleado empleadoNew = subministro.getEmpleado();
            Proveedor proveedorOld = persistentSubministro.getProveedor();
            Proveedor proveedorNew = subministro.getProveedor();
            List<DetalleSub> detalleSubListOld = persistentSubministro.getDetalleSubList();
            List<DetalleSub> detalleSubListNew = subministro.getDetalleSubList();
            if (almacenNew != null) {
                almacenNew = em.getReference(almacenNew.getClass(), almacenNew.getCod());
                subministro.setAlmacen(almacenNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getCi());
                subministro.setEmpleado(empleadoNew);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getCod());
                subministro.setProveedor(proveedorNew);
            }
            List<DetalleSub> attachedDetalleSubListNew = new ArrayList<DetalleSub>();
            for (DetalleSub detalleSubListNewDetalleSubToAttach : detalleSubListNew) {
                detalleSubListNewDetalleSubToAttach = em.getReference(detalleSubListNewDetalleSubToAttach.getClass(), detalleSubListNewDetalleSubToAttach.getId());
                attachedDetalleSubListNew.add(detalleSubListNewDetalleSubToAttach);
            }
            detalleSubListNew = attachedDetalleSubListNew;
            subministro.setDetalleSubList(detalleSubListNew);
            subministro = em.merge(subministro);
            if (almacenOld != null && !almacenOld.equals(almacenNew)) {
                almacenOld.getSubministroList().remove(subministro);
                almacenOld = em.merge(almacenOld);
            }
            if (almacenNew != null && !almacenNew.equals(almacenOld)) {
                almacenNew.getSubministroList().add(subministro);
                almacenNew = em.merge(almacenNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getSubministroList().remove(subministro);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getSubministroList().add(subministro);
                empleadoNew = em.merge(empleadoNew);
            }
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getSubministroList().remove(subministro);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getSubministroList().add(subministro);
                proveedorNew = em.merge(proveedorNew);
            }
            for (DetalleSub detalleSubListOldDetalleSub : detalleSubListOld) {
                if (!detalleSubListNew.contains(detalleSubListOldDetalleSub)) {
                    detalleSubListOldDetalleSub.setSubministro(null);
                    detalleSubListOldDetalleSub = em.merge(detalleSubListOldDetalleSub);
                }
            }
            for (DetalleSub detalleSubListNewDetalleSub : detalleSubListNew) {
                if (!detalleSubListOld.contains(detalleSubListNewDetalleSub)) {
                    Subministro oldSubministroOfDetalleSubListNewDetalleSub = detalleSubListNewDetalleSub.getSubministro();
                    detalleSubListNewDetalleSub.setSubministro(subministro);
                    detalleSubListNewDetalleSub = em.merge(detalleSubListNewDetalleSub);
                    if (oldSubministroOfDetalleSubListNewDetalleSub != null && !oldSubministroOfDetalleSubListNewDetalleSub.equals(subministro)) {
                        oldSubministroOfDetalleSubListNewDetalleSub.getDetalleSubList().remove(detalleSubListNewDetalleSub);
                        oldSubministroOfDetalleSubListNewDetalleSub = em.merge(oldSubministroOfDetalleSubListNewDetalleSub);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subministro.getCod();
                if (findSubministro(id) == null) {
                    throw new NonexistentEntityException("The subministro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Subministro subministro;
            try {
                subministro = em.getReference(Subministro.class, id);
                subministro.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subministro with id " + id + " no longer exists.", enfe);
            }
            Almacen almacen = subministro.getAlmacen();
            if (almacen != null) {
                almacen.getSubministroList().remove(subministro);
                almacen = em.merge(almacen);
            }
            Empleado empleado = subministro.getEmpleado();
            if (empleado != null) {
                empleado.getSubministroList().remove(subministro);
                empleado = em.merge(empleado);
            }
            Proveedor proveedor = subministro.getProveedor();
            if (proveedor != null) {
                proveedor.getSubministroList().remove(subministro);
                proveedor = em.merge(proveedor);
            }
            List<DetalleSub> detalleSubList = subministro.getDetalleSubList();
            for (DetalleSub detalleSubListDetalleSub : detalleSubList) {
                detalleSubListDetalleSub.setSubministro(null);
                detalleSubListDetalleSub = em.merge(detalleSubListDetalleSub);
            }
            em.remove(subministro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Subministro> findSubministroEntities() {
        return findSubministroEntities(true, -1, -1);
    }

    public List<Subministro> findSubministroEntities(int maxResults, int firstResult) {
        return findSubministroEntities(false, maxResults, firstResult);
    }

    private List<Subministro> findSubministroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Subministro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Subministro findSubministro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Subministro.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubministroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Subministro> rt = cq.from(Subministro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
