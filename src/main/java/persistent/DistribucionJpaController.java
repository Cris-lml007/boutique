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
import model.ProveedorDistribuidor;
import model.Empleado;
import model.DetalleDis;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Distribucion;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class DistribucionJpaController implements Serializable {

    public DistribucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Distribucion distribucion) throws PreexistingEntityException, Exception {
        if (distribucion.getDetalleDisList() == null) {
            distribucion.setDetalleDisList(new ArrayList<DetalleDis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProveedorDistribuidor destino = distribucion.getDestino();
            if (destino != null) {
                destino = em.getReference(destino.getClass(), destino.getCod());
                distribucion.setDestino(destino);
            }
            Empleado empleado = distribucion.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getCi());
                distribucion.setEmpleado(empleado);
            }
            Empleado encargado = distribucion.getEncargado();
            if (encargado != null) {
                encargado = em.getReference(encargado.getClass(), encargado.getCi());
                distribucion.setEncargado(encargado);
            }
            List<DetalleDis> attachedDetalleDisList = new ArrayList<DetalleDis>();
            for (DetalleDis detalleDisListDetalleDisToAttach : distribucion.getDetalleDisList()) {
                detalleDisListDetalleDisToAttach = em.getReference(detalleDisListDetalleDisToAttach.getClass(), detalleDisListDetalleDisToAttach.getId());
                attachedDetalleDisList.add(detalleDisListDetalleDisToAttach);
            }
            distribucion.setDetalleDisList(attachedDetalleDisList);
            em.persist(distribucion);
            if (destino != null) {
                destino.getDistribucionList().add(distribucion);
                destino = em.merge(destino);
            }
            if (empleado != null) {
                empleado.getDistribucionList().add(distribucion);
                empleado = em.merge(empleado);
            }
            if (encargado != null) {
                encargado.getDistribucionList().add(distribucion);
                encargado = em.merge(encargado);
            }
            for (DetalleDis detalleDisListDetalleDis : distribucion.getDetalleDisList()) {
                Distribucion oldDistribucionOfDetalleDisListDetalleDis = detalleDisListDetalleDis.getDistribucion();
                detalleDisListDetalleDis.setDistribucion(distribucion);
                detalleDisListDetalleDis = em.merge(detalleDisListDetalleDis);
                if (oldDistribucionOfDetalleDisListDetalleDis != null) {
                    oldDistribucionOfDetalleDisListDetalleDis.getDetalleDisList().remove(detalleDisListDetalleDis);
                    oldDistribucionOfDetalleDisListDetalleDis = em.merge(oldDistribucionOfDetalleDisListDetalleDis);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDistribucion(distribucion.getId()) != null) {
                throw new PreexistingEntityException("Distribucion " + distribucion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Distribucion distribucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distribucion persistentDistribucion = em.find(Distribucion.class, distribucion.getId());
            ProveedorDistribuidor destinoOld = persistentDistribucion.getDestino();
            ProveedorDistribuidor destinoNew = distribucion.getDestino();
            Empleado empleadoOld = persistentDistribucion.getEmpleado();
            Empleado empleadoNew = distribucion.getEmpleado();
            Empleado encargadoOld = persistentDistribucion.getEncargado();
            Empleado encargadoNew = distribucion.getEncargado();
            List<DetalleDis> detalleDisListOld = persistentDistribucion.getDetalleDisList();
            List<DetalleDis> detalleDisListNew = distribucion.getDetalleDisList();
            if (destinoNew != null) {
                destinoNew = em.getReference(destinoNew.getClass(), destinoNew.getCod());
                distribucion.setDestino(destinoNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getCi());
                distribucion.setEmpleado(empleadoNew);
            }
            if (encargadoNew != null) {
                encargadoNew = em.getReference(encargadoNew.getClass(), encargadoNew.getCi());
                distribucion.setEncargado(encargadoNew);
            }
            List<DetalleDis> attachedDetalleDisListNew = new ArrayList<DetalleDis>();
            for (DetalleDis detalleDisListNewDetalleDisToAttach : detalleDisListNew) {
                detalleDisListNewDetalleDisToAttach = em.getReference(detalleDisListNewDetalleDisToAttach.getClass(), detalleDisListNewDetalleDisToAttach.getId());
                attachedDetalleDisListNew.add(detalleDisListNewDetalleDisToAttach);
            }
            detalleDisListNew = attachedDetalleDisListNew;
            distribucion.setDetalleDisList(detalleDisListNew);
            distribucion = em.merge(distribucion);
            if (destinoOld != null && !destinoOld.equals(destinoNew)) {
                destinoOld.getDistribucionList().remove(distribucion);
                destinoOld = em.merge(destinoOld);
            }
            if (destinoNew != null && !destinoNew.equals(destinoOld)) {
                destinoNew.getDistribucionList().add(distribucion);
                destinoNew = em.merge(destinoNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getDistribucionList().remove(distribucion);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getDistribucionList().add(distribucion);
                empleadoNew = em.merge(empleadoNew);
            }
            if (encargadoOld != null && !encargadoOld.equals(encargadoNew)) {
                encargadoOld.getDistribucionList().remove(distribucion);
                encargadoOld = em.merge(encargadoOld);
            }
            if (encargadoNew != null && !encargadoNew.equals(encargadoOld)) {
                encargadoNew.getDistribucionList().add(distribucion);
                encargadoNew = em.merge(encargadoNew);
            }
            for (DetalleDis detalleDisListOldDetalleDis : detalleDisListOld) {
                if (!detalleDisListNew.contains(detalleDisListOldDetalleDis)) {
                    detalleDisListOldDetalleDis.setDistribucion(null);
                    detalleDisListOldDetalleDis = em.merge(detalleDisListOldDetalleDis);
                }
            }
            for (DetalleDis detalleDisListNewDetalleDis : detalleDisListNew) {
                if (!detalleDisListOld.contains(detalleDisListNewDetalleDis)) {
                    Distribucion oldDistribucionOfDetalleDisListNewDetalleDis = detalleDisListNewDetalleDis.getDistribucion();
                    detalleDisListNewDetalleDis.setDistribucion(distribucion);
                    detalleDisListNewDetalleDis = em.merge(detalleDisListNewDetalleDis);
                    if (oldDistribucionOfDetalleDisListNewDetalleDis != null && !oldDistribucionOfDetalleDisListNewDetalleDis.equals(distribucion)) {
                        oldDistribucionOfDetalleDisListNewDetalleDis.getDetalleDisList().remove(detalleDisListNewDetalleDis);
                        oldDistribucionOfDetalleDisListNewDetalleDis = em.merge(oldDistribucionOfDetalleDisListNewDetalleDis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = distribucion.getId();
                if (findDistribucion(id) == null) {
                    throw new NonexistentEntityException("The distribucion with id " + id + " no longer exists.");
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
            Distribucion distribucion;
            try {
                distribucion = em.getReference(Distribucion.class, id);
                distribucion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distribucion with id " + id + " no longer exists.", enfe);
            }
            ProveedorDistribuidor destino = distribucion.getDestino();
            if (destino != null) {
                destino.getDistribucionList().remove(distribucion);
                destino = em.merge(destino);
            }
            Empleado empleado = distribucion.getEmpleado();
            if (empleado != null) {
                empleado.getDistribucionList().remove(distribucion);
                empleado = em.merge(empleado);
            }
            Empleado encargado = distribucion.getEncargado();
            if (encargado != null) {
                encargado.getDistribucionList().remove(distribucion);
                encargado = em.merge(encargado);
            }
            List<DetalleDis> detalleDisList = distribucion.getDetalleDisList();
            for (DetalleDis detalleDisListDetalleDis : detalleDisList) {
                detalleDisListDetalleDis.setDistribucion(null);
                detalleDisListDetalleDis = em.merge(detalleDisListDetalleDis);
            }
            em.remove(distribucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Distribucion> findDistribucionEntities() {
        return findDistribucionEntities(true, -1, -1);
    }

    public List<Distribucion> findDistribucionEntities(int maxResults, int firstResult) {
        return findDistribucionEntities(false, maxResults, firstResult);
    }

    private List<Distribucion> findDistribucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Distribucion.class));
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

    public Distribucion findDistribucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Distribucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistribucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Distribucion> rt = cq.from(Distribucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
