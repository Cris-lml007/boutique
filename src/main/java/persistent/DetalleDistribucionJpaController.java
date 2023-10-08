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
import model.DetalleDistribucion;
import persistent.exceptions.NonexistentEntityException;

/**
 *
 * @author metallica
 */
public class DetalleDistribucionJpaController implements Serializable {

    public DetalleDistribucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleDistribucion detalleDistribucion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(detalleDistribucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleDistribucion detalleDistribucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            detalleDistribucion = em.merge(detalleDistribucion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = detalleDistribucion.getId();
                if (findDetalleDistribucion(id) == null) {
                    throw new NonexistentEntityException("The detalleDistribucion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleDistribucion detalleDistribucion;
            try {
                detalleDistribucion = em.getReference(DetalleDistribucion.class, id);
                detalleDistribucion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleDistribucion with id " + id + " no longer exists.", enfe);
            }
            em.remove(detalleDistribucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleDistribucion> findDetalleDistribucionEntities() {
        return findDetalleDistribucionEntities(true, -1, -1);
    }

    public List<DetalleDistribucion> findDetalleDistribucionEntities(int maxResults, int firstResult) {
        return findDetalleDistribucionEntities(false, maxResults, firstResult);
    }

    private List<DetalleDistribucion> findDetalleDistribucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleDistribucion.class));
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

    public DetalleDistribucion findDetalleDistribucion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleDistribucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleDistribucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleDistribucion> rt = cq.from(DetalleDistribucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
