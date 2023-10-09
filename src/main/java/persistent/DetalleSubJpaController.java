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
import model.DetalleSub;
import model.Item;
import model.Subministro;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class DetalleSubJpaController implements Serializable {

    public DetalleSubJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleSub detalleSub) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item producto = detalleSub.getProducto();
            if (producto != null) {
                producto = em.getReference(producto.getClass(), producto.getCod());
                detalleSub.setProducto(producto);
            }
            Subministro subministro = detalleSub.getSubministro();
            if (subministro != null) {
                subministro = em.getReference(subministro.getClass(), subministro.getCod());
                detalleSub.setSubministro(subministro);
            }
            em.persist(detalleSub);
            if (producto != null) {
                producto.getDetalleSubList().add(detalleSub);
                producto = em.merge(producto);
            }
            if (subministro != null) {
                subministro.getDetalleSubList().add(detalleSub);
                subministro = em.merge(subministro);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleSub(detalleSub.getId()) != null) {
                throw new PreexistingEntityException("DetalleSub " + detalleSub + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleSub detalleSub) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleSub persistentDetalleSub = em.find(DetalleSub.class, detalleSub.getId());
            Item productoOld = persistentDetalleSub.getProducto();
            Item productoNew = detalleSub.getProducto();
            Subministro subministroOld = persistentDetalleSub.getSubministro();
            Subministro subministroNew = detalleSub.getSubministro();
            if (productoNew != null) {
                productoNew = em.getReference(productoNew.getClass(), productoNew.getCod());
                detalleSub.setProducto(productoNew);
            }
            if (subministroNew != null) {
                subministroNew = em.getReference(subministroNew.getClass(), subministroNew.getCod());
                detalleSub.setSubministro(subministroNew);
            }
            detalleSub = em.merge(detalleSub);
            if (productoOld != null && !productoOld.equals(productoNew)) {
                productoOld.getDetalleSubList().remove(detalleSub);
                productoOld = em.merge(productoOld);
            }
            if (productoNew != null && !productoNew.equals(productoOld)) {
                productoNew.getDetalleSubList().add(detalleSub);
                productoNew = em.merge(productoNew);
            }
            if (subministroOld != null && !subministroOld.equals(subministroNew)) {
                subministroOld.getDetalleSubList().remove(detalleSub);
                subministroOld = em.merge(subministroOld);
            }
            if (subministroNew != null && !subministroNew.equals(subministroOld)) {
                subministroNew.getDetalleSubList().add(detalleSub);
                subministroNew = em.merge(subministroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleSub.getId();
                if (findDetalleSub(id) == null) {
                    throw new NonexistentEntityException("The detalleSub with id " + id + " no longer exists.");
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
            DetalleSub detalleSub;
            try {
                detalleSub = em.getReference(DetalleSub.class, id);
                detalleSub.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleSub with id " + id + " no longer exists.", enfe);
            }
            Item producto = detalleSub.getProducto();
            if (producto != null) {
                producto.getDetalleSubList().remove(detalleSub);
                producto = em.merge(producto);
            }
            Subministro subministro = detalleSub.getSubministro();
            if (subministro != null) {
                subministro.getDetalleSubList().remove(detalleSub);
                subministro = em.merge(subministro);
            }
            em.remove(detalleSub);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleSub> findDetalleSubEntities() {
        return findDetalleSubEntities(true, -1, -1);
    }

    public List<DetalleSub> findDetalleSubEntities(int maxResults, int firstResult) {
        return findDetalleSubEntities(false, maxResults, firstResult);
    }

    private List<DetalleSub> findDetalleSubEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleSub.class));
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

    public DetalleSub findDetalleSub(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleSub.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleSubCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleSub> rt = cq.from(DetalleSub.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
