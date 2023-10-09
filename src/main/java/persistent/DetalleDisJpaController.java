/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistent;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.DetalleDis;
import model.Distribucion;
import model.Item;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class DetalleDisJpaController implements Serializable {

    public DetalleDisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleDis detalleDis) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Distribucion distribucion = detalleDis.getDistribucion();
            if (distribucion != null) {
                distribucion = em.getReference(distribucion.getClass(), distribucion.getId());
                detalleDis.setDistribucion(distribucion);
            }
            Item producto = detalleDis.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getCod());
                detalleDis.setProducto(producto);
            }
            em.persist(detalleDis);
            if (distribucion != null) {
                distribucion.getDetalleDisList().add(detalleDis);
                distribucion = em.merge(distribucion);
            }
            if (producto != null) {
                producto.getDetalleDisList().add(detalleDis);
                producto = em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleDis(detalleDis.getId()) != null) {
                throw new PreexistingEntityException("DetalleDis " + detalleDis + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleDis detalleDis) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleDis persistentDetalleDis = em.find(DetalleDis.class, detalleDis.getId());
            Distribucion distribucionOld = persistentDetalleDis.getDistribucion();
            Distribucion distribucionNew = detalleDis.getDistribucion();
            Item productoOld = persistentDetalleDis.getProducto();
            Item productoNew = detalleDis.getProducto();
            if (distribucionNew != null) {
                distribucionNew = em.getReference(distribucionNew.getClass(), distribucionNew.getId());
                detalleDis.setDistribucion(distribucionNew);
            }
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getCod());
                detalleDis.setProducto(productoNew);
            }
            detalleDis = em.merge(detalleDis);
            if (distribucionOld != null && !distribucionOld.equals(distribucionNew)) {
                distribucionOld.getDetalleDisList().remove(detalleDis);
                distribucionOld = em.merge(distribucionOld);
            }
            if (distribucionNew != null && !distribucionNew.equals(distribucionOld)) {
                distribucionNew.getDetalleDisList().add(detalleDis);
                distribucionNew = em.merge(distribucionNew);
            }
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDetalleDisList().remove(detalleDis);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDetalleDisList().add(detalleDis);
                productoNew = em.merge(productoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleDis.getId();
                if (findDetalleDis(id) == null) {
                    throw new NonexistentEntityException("The detalleDis with id " + id + " no longer exists.");
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
            DetalleDis detalleDis;
            try {
                detalleDis = em.getReference(DetalleDis.class, id);
                detalleDis.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleDis with id " + id + " no longer exists.", enfe);
            }
            Distribucion distribucion = detalleDis.getDistribucion();
            if (distribucion != null) {
                distribucion.getDetalleDisList().remove(detalleDis);
                distribucion = em.merge(distribucion);
            }
            Item producto = detalleDis.getProducto();
            if (producto != null) {
                producto.getDetalleDisList().remove(detalleDis);
                producto = em.merge(producto);
            }
            em.remove(detalleDis);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleDis> findDetalleDisEntities() {
        return findDetalleDisEntities(true, -1, -1);
    }

    public List<DetalleDis> findDetalleDisEntities(int maxResults, int firstResult) {
        return findDetalleDisEntities(false, maxResults, firstResult);
    }

    private List<DetalleDis> findDetalleDisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleDis.class));
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

    public DetalleDis findDetalleDis(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleDis.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleDisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleDis> rt = cq.from(DetalleDis.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
