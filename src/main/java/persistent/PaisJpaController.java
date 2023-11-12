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
import model.Localizacion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Pais;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) throws PreexistingEntityException, Exception {
        if (pais.getLocalizacionList() == null) {
            pais.setLocalizacionList(new ArrayList<Localizacion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Localizacion> attachedLocalizacionList = new ArrayList<Localizacion>();
            for (Localizacion localizacionListLocalizacionToAttach : pais.getLocalizacionList()) {
                localizacionListLocalizacionToAttach = em.getReference(localizacionListLocalizacionToAttach.getClass(), localizacionListLocalizacionToAttach.getCod());
                attachedLocalizacionList.add(localizacionListLocalizacionToAttach);
            }
            pais.setLocalizacionList(attachedLocalizacionList);
            em.persist(pais);
            for (Localizacion localizacionListLocalizacion : pais.getLocalizacionList()) {
                Pais oldPaisOfLocalizacionListLocalizacion = localizacionListLocalizacion.getPais();
                localizacionListLocalizacion.setPais(pais);
                localizacionListLocalizacion = em.merge(localizacionListLocalizacion);
                if (oldPaisOfLocalizacionListLocalizacion != null) {
                    oldPaisOfLocalizacionListLocalizacion.getLocalizacionList().remove(localizacionListLocalizacion);
                    oldPaisOfLocalizacionListLocalizacion = em.merge(oldPaisOfLocalizacionListLocalizacion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPais(pais.getCod()) != null) {
                throw new PreexistingEntityException("Pais " + pais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getCod());
            List<Localizacion> localizacionListOld = persistentPais.getLocalizacionList();
            List<Localizacion> localizacionListNew = pais.getLocalizacionList();
            List<Localizacion> attachedLocalizacionListNew = new ArrayList<Localizacion>();
            for (Localizacion localizacionListNewLocalizacionToAttach : localizacionListNew) {
                localizacionListNewLocalizacionToAttach = em.getReference(localizacionListNewLocalizacionToAttach.getClass(), localizacionListNewLocalizacionToAttach.getCod());
                attachedLocalizacionListNew.add(localizacionListNewLocalizacionToAttach);
            }
            localizacionListNew = attachedLocalizacionListNew;
            pais.setLocalizacionList(localizacionListNew);
            pais = em.merge(pais);
            for (Localizacion localizacionListOldLocalizacion : localizacionListOld) {
                if (!localizacionListNew.contains(localizacionListOldLocalizacion)) {
                    localizacionListOldLocalizacion.setPais(null);
                    localizacionListOldLocalizacion = em.merge(localizacionListOldLocalizacion);
                }
            }
            for (Localizacion localizacionListNewLocalizacion : localizacionListNew) {
                if (!localizacionListOld.contains(localizacionListNewLocalizacion)) {
                    Pais oldPaisOfLocalizacionListNewLocalizacion = localizacionListNewLocalizacion.getPais();
                    localizacionListNewLocalizacion.setPais(pais);
                    localizacionListNewLocalizacion = em.merge(localizacionListNewLocalizacion);
                    if (oldPaisOfLocalizacionListNewLocalizacion != null && !oldPaisOfLocalizacionListNewLocalizacion.equals(pais)) {
                        oldPaisOfLocalizacionListNewLocalizacion.getLocalizacionList().remove(localizacionListNewLocalizacion);
                        oldPaisOfLocalizacionListNewLocalizacion = em.merge(oldPaisOfLocalizacionListNewLocalizacion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pais.getCod();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<Localizacion> localizacionList = pais.getLocalizacionList();
            for (Localizacion localizacionListLocalizacion : localizacionList) {
                localizacionListLocalizacion.setPais(null);
                localizacionListLocalizacion = em.merge(localizacionListLocalizacion);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
