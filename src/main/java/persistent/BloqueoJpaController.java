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
import model.Bloqueo;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class BloqueoJpaController implements Serializable {

    public BloqueoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bloqueo bloqueo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(bloqueo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBloqueo(bloqueo.getId()) != null) {
                throw new PreexistingEntityException("Bloqueo " + bloqueo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bloqueo bloqueo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bloqueo = em.merge(bloqueo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bloqueo.getId();
                if (findBloqueo(id) == null) {
                    throw new NonexistentEntityException("The bloqueo with id " + id + " no longer exists.");
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
            Bloqueo bloqueo;
            try {
                bloqueo = em.getReference(Bloqueo.class, id);
                bloqueo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bloqueo with id " + id + " no longer exists.", enfe);
            }
            em.remove(bloqueo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bloqueo> findBloqueoEntities() {
        return findBloqueoEntities(true, -1, -1);
    }

    public List<Bloqueo> findBloqueoEntities(int maxResults, int firstResult) {
        return findBloqueoEntities(false, maxResults, firstResult);
    }

    private List<Bloqueo> findBloqueoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Bloqueo.class));
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

    public Bloqueo findBloqueo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bloqueo.class, id);
        } finally {
            em.close();
        }
    }

    public int getBloqueoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Bloqueo> rt = cq.from(Bloqueo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
