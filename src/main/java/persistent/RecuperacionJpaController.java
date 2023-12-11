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
import model.Empleado;
import model.Recuperacion;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class RecuperacionJpaController implements Serializable {

    public RecuperacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Recuperacion recuperacion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado duenio = recuperacion.getDuenio();
            if (duenio != null) {
                duenio = em.getReference(duenio.getClass(), duenio.getCi());
                recuperacion.setDuenio(duenio);
            }
            em.persist(recuperacion);
            if (duenio != null) {
                duenio.getRecuperacionList().add(recuperacion);
                duenio = em.merge(duenio);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRecuperacion(recuperacion.getId()) != null) {
                throw new PreexistingEntityException("Recuperacion " + recuperacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Recuperacion recuperacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Recuperacion persistentRecuperacion = em.find(Recuperacion.class, recuperacion.getId());
            Empleado duenioOld = persistentRecuperacion.getDuenio();
            Empleado duenioNew = recuperacion.getDuenio();
            if (duenioNew != null) {
                duenioNew = em.getReference(duenioNew.getClass(), duenioNew.getCi());
                recuperacion.setDuenio(duenioNew);
            }
            recuperacion = em.merge(recuperacion);
            if (duenioOld != null && !duenioOld.equals(duenioNew)) {
                duenioOld.getRecuperacionList().remove(recuperacion);
                duenioOld = em.merge(duenioOld);
            }
            if (duenioNew != null && !duenioNew.equals(duenioOld)) {
                duenioNew.getRecuperacionList().add(recuperacion);
                duenioNew = em.merge(duenioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = recuperacion.getId();
                if (findRecuperacion(id) == null) {
                    throw new NonexistentEntityException("The recuperacion with id " + id + " no longer exists.");
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
            Recuperacion recuperacion;
            try {
                recuperacion = em.getReference(Recuperacion.class, id);
                recuperacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The recuperacion with id " + id + " no longer exists.", enfe);
            }
            Empleado duenio = recuperacion.getDuenio();
            if (duenio != null) {
                duenio.getRecuperacionList().remove(recuperacion);
                duenio = em.merge(duenio);
            }
            em.remove(recuperacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Recuperacion> findRecuperacionEntities() {
        return findRecuperacionEntities(true, -1, -1);
    }

    public List<Recuperacion> findRecuperacionEntities(int maxResults, int firstResult) {
        return findRecuperacionEntities(false, maxResults, firstResult);
    }

    private List<Recuperacion> findRecuperacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Recuperacion.class));
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

    public Recuperacion findRecuperacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Recuperacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getRecuperacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Recuperacion> rt = cq.from(Recuperacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
