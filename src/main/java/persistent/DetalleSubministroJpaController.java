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
import model.DetalleSubministro;
import persistent.exceptions.NonexistentEntityException;

/**
 *
 * @author metallica
 */
public class DetalleSubministroJpaController implements Serializable {

    public DetalleSubministroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleSubministro detalleSubministro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(detalleSubministro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleSubministro detalleSubministro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            detalleSubministro = em.merge(detalleSubministro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = detalleSubministro.getId();
                if (findDetalleSubministro(id) == null) {
                    throw new NonexistentEntityException("The detalleSubministro with id " + id + " no longer exists.");
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
            DetalleSubministro detalleSubministro;
            try {
                detalleSubministro = em.getReference(DetalleSubministro.class, id);
                detalleSubministro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleSubministro with id " + id + " no longer exists.", enfe);
            }
            em.remove(detalleSubministro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleSubministro> findDetalleSubministroEntities() {
        return findDetalleSubministroEntities(true, -1, -1);
    }

    public List<DetalleSubministro> findDetalleSubministroEntities(int maxResults, int firstResult) {
        return findDetalleSubministroEntities(false, maxResults, firstResult);
    }

    private List<DetalleSubministro> findDetalleSubministroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleSubministro.class));
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

    public DetalleSubministro findDetalleSubministro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleSubministro.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleSubministroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleSubministro> rt = cq.from(DetalleSubministro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
