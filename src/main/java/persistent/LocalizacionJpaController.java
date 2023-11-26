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
import model.Pais;
import model.ProveedorDistribuidor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Localizacion;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class LocalizacionJpaController implements Serializable {

    public LocalizacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localizacion localizacion) throws PreexistingEntityException, Exception {
        if (localizacion.getProveedorDistribuidorList() == null) {
            localizacion.setProveedorDistribuidorList(new ArrayList<ProveedorDistribuidor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais pais = localizacion.getPais();
            if (pais != null) {
                pais = em.getReference(pais.getClass(), pais.getCod());
                localizacion.setPais(pais);
            }
            List<ProveedorDistribuidor> attachedProveedorDistribuidorList = new ArrayList<ProveedorDistribuidor>();
            for (ProveedorDistribuidor proveedorDistribuidorListProveedorDistribuidorToAttach : localizacion.getProveedorDistribuidorList()) {
                proveedorDistribuidorListProveedorDistribuidorToAttach = em.getReference(proveedorDistribuidorListProveedorDistribuidorToAttach.getClass(), proveedorDistribuidorListProveedorDistribuidorToAttach.getCod());
                attachedProveedorDistribuidorList.add(proveedorDistribuidorListProveedorDistribuidorToAttach);
            }
            localizacion.setProveedorDistribuidorList(attachedProveedorDistribuidorList);
            em.persist(localizacion);
            if (pais != null) {
                pais.getLocalizacionList().add(localizacion);
                pais = em.merge(pais);
            }
            for (ProveedorDistribuidor proveedorDistribuidorListProveedorDistribuidor : localizacion.getProveedorDistribuidorList()) {
                Localizacion oldOrigenOfProveedorDistribuidorListProveedorDistribuidor = proveedorDistribuidorListProveedorDistribuidor.getOrigen();
                proveedorDistribuidorListProveedorDistribuidor.setOrigen(localizacion);
                proveedorDistribuidorListProveedorDistribuidor = em.merge(proveedorDistribuidorListProveedorDistribuidor);
                if (oldOrigenOfProveedorDistribuidorListProveedorDistribuidor != null) {
                    oldOrigenOfProveedorDistribuidorListProveedorDistribuidor.getProveedorDistribuidorList().remove(proveedorDistribuidorListProveedorDistribuidor);
                    oldOrigenOfProveedorDistribuidorListProveedorDistribuidor = em.merge(oldOrigenOfProveedorDistribuidorListProveedorDistribuidor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLocalizacion(localizacion.getCod()) != null) {
                throw new PreexistingEntityException("Localizacion " + localizacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localizacion localizacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion persistentLocalizacion = em.find(Localizacion.class, localizacion.getCod());
            Pais paisOld = persistentLocalizacion.getPais();
            Pais paisNew = localizacion.getPais();
            List<ProveedorDistribuidor> proveedorDistribuidorListOld = persistentLocalizacion.getProveedorDistribuidorList();
            List<ProveedorDistribuidor> proveedorDistribuidorListNew = localizacion.getProveedorDistribuidorList();
            if (paisNew != null) {
                paisNew = em.getReference(paisNew.getClass(), paisNew.getCod());
                localizacion.setPais(paisNew);
            }
            List<ProveedorDistribuidor> attachedProveedorDistribuidorListNew = new ArrayList<ProveedorDistribuidor>();
            for (ProveedorDistribuidor proveedorDistribuidorListNewProveedorDistribuidorToAttach : proveedorDistribuidorListNew) {
                proveedorDistribuidorListNewProveedorDistribuidorToAttach = em.getReference(proveedorDistribuidorListNewProveedorDistribuidorToAttach.getClass(), proveedorDistribuidorListNewProveedorDistribuidorToAttach.getCod());
                attachedProveedorDistribuidorListNew.add(proveedorDistribuidorListNewProveedorDistribuidorToAttach);
            }
            proveedorDistribuidorListNew = attachedProveedorDistribuidorListNew;
            localizacion.setProveedorDistribuidorList(proveedorDistribuidorListNew);
            localizacion = em.merge(localizacion);
            if (paisOld != null && !paisOld.equals(paisNew)) {
                paisOld.getLocalizacionList().remove(localizacion);
                paisOld = em.merge(paisOld);
            }
            if (paisNew != null && !paisNew.equals(paisOld)) {
                paisNew.getLocalizacionList().add(localizacion);
                paisNew = em.merge(paisNew);
            }
            for (ProveedorDistribuidor proveedorDistribuidorListOldProveedorDistribuidor : proveedorDistribuidorListOld) {
                if (!proveedorDistribuidorListNew.contains(proveedorDistribuidorListOldProveedorDistribuidor)) {
                    proveedorDistribuidorListOldProveedorDistribuidor.setOrigen(null);
                    proveedorDistribuidorListOldProveedorDistribuidor = em.merge(proveedorDistribuidorListOldProveedorDistribuidor);
                }
            }
            for (ProveedorDistribuidor proveedorDistribuidorListNewProveedorDistribuidor : proveedorDistribuidorListNew) {
                if (!proveedorDistribuidorListOld.contains(proveedorDistribuidorListNewProveedorDistribuidor)) {
                    Localizacion oldOrigenOfProveedorDistribuidorListNewProveedorDistribuidor = proveedorDistribuidorListNewProveedorDistribuidor.getOrigen();
                    proveedorDistribuidorListNewProveedorDistribuidor.setOrigen(localizacion);
                    proveedorDistribuidorListNewProveedorDistribuidor = em.merge(proveedorDistribuidorListNewProveedorDistribuidor);
                    if (oldOrigenOfProveedorDistribuidorListNewProveedorDistribuidor != null && !oldOrigenOfProveedorDistribuidorListNewProveedorDistribuidor.equals(localizacion)) {
                        oldOrigenOfProveedorDistribuidorListNewProveedorDistribuidor.getProveedorDistribuidorList().remove(proveedorDistribuidorListNewProveedorDistribuidor);
                        oldOrigenOfProveedorDistribuidorListNewProveedorDistribuidor = em.merge(oldOrigenOfProveedorDistribuidorListNewProveedorDistribuidor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = localizacion.getCod();
                if (findLocalizacion(id) == null) {
                    throw new NonexistentEntityException("The localizacion with id " + id + " no longer exists.");
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
            Localizacion localizacion;
            try {
                localizacion = em.getReference(Localizacion.class, id);
                localizacion.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localizacion with id " + id + " no longer exists.", enfe);
            }
            Pais pais = localizacion.getPais();
            if (pais != null) {
                pais.getLocalizacionList().remove(localizacion);
                pais = em.merge(pais);
            }
            List<ProveedorDistribuidor> proveedorDistribuidorList = localizacion.getProveedorDistribuidorList();
            for (ProveedorDistribuidor proveedorDistribuidorListProveedorDistribuidor : proveedorDistribuidorList) {
                proveedorDistribuidorListProveedorDistribuidor.setOrigen(null);
                proveedorDistribuidorListProveedorDistribuidor = em.merge(proveedorDistribuidorListProveedorDistribuidor);
            }
            em.remove(localizacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localizacion> findLocalizacionEntities() {
        return findLocalizacionEntities(true, -1, -1);
    }

    public List<Localizacion> findLocalizacionEntities(int maxResults, int firstResult) {
        return findLocalizacionEntities(false, maxResults, firstResult);
    }

    private List<Localizacion> findLocalizacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localizacion.class));
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

    public Localizacion findLocalizacion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localizacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalizacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localizacion> rt = cq.from(Localizacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
