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
import model.Subministro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Almacen;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class AlmacenJpaController implements Serializable {

    public AlmacenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Almacen almacen) throws PreexistingEntityException, Exception {
        if (almacen.getSubministroList() == null) {
            almacen.setSubministroList(new ArrayList<Subministro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion origen = almacen.getOrigen();
            if (origen != null) {
                origen = em.getReference(origen.getClass(), origen.getCod());
                almacen.setOrigen(origen);
            }
            List<Subministro> attachedSubministroList = new ArrayList<Subministro>();
            for (Subministro subministroListSubministroToAttach : almacen.getSubministroList()) {
                subministroListSubministroToAttach = em.getReference(subministroListSubministroToAttach.getClass(), subministroListSubministroToAttach.getCod());
                attachedSubministroList.add(subministroListSubministroToAttach);
            }
            almacen.setSubministroList(attachedSubministroList);
            em.persist(almacen);
            if (origen != null) {
                origen.getAlmacenList().add(almacen);
                origen = em.merge(origen);
            }
            for (Subministro subministroListSubministro : almacen.getSubministroList()) {
                Almacen oldAlmacenOfSubministroListSubministro = subministroListSubministro.getAlmacen();
                subministroListSubministro.setAlmacen(almacen);
                subministroListSubministro = em.merge(subministroListSubministro);
                if (oldAlmacenOfSubministroListSubministro != null) {
                    oldAlmacenOfSubministroListSubministro.getSubministroList().remove(subministroListSubministro);
                    oldAlmacenOfSubministroListSubministro = em.merge(oldAlmacenOfSubministroListSubministro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlmacen(almacen.getCod()) != null) {
                throw new PreexistingEntityException("Almacen " + almacen + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Almacen almacen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Almacen persistentAlmacen = em.find(Almacen.class, almacen.getCod());
            Localizacion origenOld = persistentAlmacen.getOrigen();
            Localizacion origenNew = almacen.getOrigen();
            List<Subministro> subministroListOld = persistentAlmacen.getSubministroList();
            List<Subministro> subministroListNew = almacen.getSubministroList();
            if (origenNew != null) {
                origenNew = em.getReference(origenNew.getClass(), origenNew.getCod());
                almacen.setOrigen(origenNew);
            }
            List<Subministro> attachedSubministroListNew = new ArrayList<Subministro>();
            for (Subministro subministroListNewSubministroToAttach : subministroListNew) {
                subministroListNewSubministroToAttach = em.getReference(subministroListNewSubministroToAttach.getClass(), subministroListNewSubministroToAttach.getCod());
                attachedSubministroListNew.add(subministroListNewSubministroToAttach);
            }
            subministroListNew = attachedSubministroListNew;
            almacen.setSubministroList(subministroListNew);
            almacen = em.merge(almacen);
            if (origenOld != null && !origenOld.equals(origenNew)) {
                origenOld.getAlmacenList().remove(almacen);
                origenOld = em.merge(origenOld);
            }
            if (origenNew != null && !origenNew.equals(origenOld)) {
                origenNew.getAlmacenList().add(almacen);
                origenNew = em.merge(origenNew);
            }
            for (Subministro subministroListOldSubministro : subministroListOld) {
                if (!subministroListNew.contains(subministroListOldSubministro)) {
                    subministroListOldSubministro.setAlmacen(null);
                    subministroListOldSubministro = em.merge(subministroListOldSubministro);
                }
            }
            for (Subministro subministroListNewSubministro : subministroListNew) {
                if (!subministroListOld.contains(subministroListNewSubministro)) {
                    Almacen oldAlmacenOfSubministroListNewSubministro = subministroListNewSubministro.getAlmacen();
                    subministroListNewSubministro.setAlmacen(almacen);
                    subministroListNewSubministro = em.merge(subministroListNewSubministro);
                    if (oldAlmacenOfSubministroListNewSubministro != null && !oldAlmacenOfSubministroListNewSubministro.equals(almacen)) {
                        oldAlmacenOfSubministroListNewSubministro.getSubministroList().remove(subministroListNewSubministro);
                        oldAlmacenOfSubministroListNewSubministro = em.merge(oldAlmacenOfSubministroListNewSubministro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = almacen.getCod();
                if (findAlmacen(id) == null) {
                    throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.");
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
            Almacen almacen;
            try {
                almacen = em.getReference(Almacen.class, id);
                almacen.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.", enfe);
            }
            Localizacion origen = almacen.getOrigen();
            if (origen != null) {
                origen.getAlmacenList().remove(almacen);
                origen = em.merge(origen);
            }
            List<Subministro> subministroList = almacen.getSubministroList();
            for (Subministro subministroListSubministro : subministroList) {
                subministroListSubministro.setAlmacen(null);
                subministroListSubministro = em.merge(subministroListSubministro);
            }
            em.remove(almacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Almacen> findAlmacenEntities() {
        return findAlmacenEntities(true, -1, -1);
    }

    public List<Almacen> findAlmacenEntities(int maxResults, int firstResult) {
        return findAlmacenEntities(false, maxResults, firstResult);
    }

    private List<Almacen> findAlmacenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Almacen.class));
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

    public Almacen findAlmacen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacen.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlmacenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Almacen> rt = cq.from(Almacen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
