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
import model.Proveedor;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws PreexistingEntityException, Exception {
        if (proveedor.getSubministroList() == null) {
            proveedor.setSubministroList(new ArrayList<Subministro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion origen = proveedor.getOrigen();
            if (origen != null) {
                origen = em.getReference(origen.getClass(), origen.getCod());
                proveedor.setOrigen(origen);
            }
            List<Subministro> attachedSubministroList = new ArrayList<Subministro>();
            for (Subministro subministroListSubministroToAttach : proveedor.getSubministroList()) {
                subministroListSubministroToAttach = em.getReference(subministroListSubministroToAttach.getClass(), subministroListSubministroToAttach.getCod());
                attachedSubministroList.add(subministroListSubministroToAttach);
            }
            proveedor.setSubministroList(attachedSubministroList);
            em.persist(proveedor);
            if (origen != null) {
                origen.getProveedorList().add(proveedor);
                origen = em.merge(origen);
            }
            for (Subministro subministroListSubministro : proveedor.getSubministroList()) {
                Proveedor oldProveedorOfSubministroListSubministro = subministroListSubministro.getProveedor();
                subministroListSubministro.setProveedor(proveedor);
                subministroListSubministro = em.merge(subministroListSubministro);
                if (oldProveedorOfSubministroListSubministro != null) {
                    oldProveedorOfSubministroListSubministro.getSubministroList().remove(subministroListSubministro);
                    oldProveedorOfSubministroListSubministro = em.merge(oldProveedorOfSubministroListSubministro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedor(proveedor.getCod()) != null) {
                throw new PreexistingEntityException("Proveedor " + proveedor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getCod());
            Localizacion origenOld = persistentProveedor.getOrigen();
            Localizacion origenNew = proveedor.getOrigen();
            List<Subministro> subministroListOld = persistentProveedor.getSubministroList();
            List<Subministro> subministroListNew = proveedor.getSubministroList();
            if (origenNew != null) {
                origenNew = em.getReference(origenNew.getClass(), origenNew.getCod());
                proveedor.setOrigen(origenNew);
            }
            List<Subministro> attachedSubministroListNew = new ArrayList<Subministro>();
            for (Subministro subministroListNewSubministroToAttach : subministroListNew) {
                subministroListNewSubministroToAttach = em.getReference(subministroListNewSubministroToAttach.getClass(), subministroListNewSubministroToAttach.getCod());
                attachedSubministroListNew.add(subministroListNewSubministroToAttach);
            }
            subministroListNew = attachedSubministroListNew;
            proveedor.setSubministroList(subministroListNew);
            proveedor = em.merge(proveedor);
            if (origenOld != null && !origenOld.equals(origenNew)) {
                origenOld.getProveedorList().remove(proveedor);
                origenOld = em.merge(origenOld);
            }
            if (origenNew != null && !origenNew.equals(origenOld)) {
                origenNew.getProveedorList().add(proveedor);
                origenNew = em.merge(origenNew);
            }
            for (Subministro subministroListOldSubministro : subministroListOld) {
                if (!subministroListNew.contains(subministroListOldSubministro)) {
                    subministroListOldSubministro.setProveedor(null);
                    subministroListOldSubministro = em.merge(subministroListOldSubministro);
                }
            }
            for (Subministro subministroListNewSubministro : subministroListNew) {
                if (!subministroListOld.contains(subministroListNewSubministro)) {
                    Proveedor oldProveedorOfSubministroListNewSubministro = subministroListNewSubministro.getProveedor();
                    subministroListNewSubministro.setProveedor(proveedor);
                    subministroListNewSubministro = em.merge(subministroListNewSubministro);
                    if (oldProveedorOfSubministroListNewSubministro != null && !oldProveedorOfSubministroListNewSubministro.equals(proveedor)) {
                        oldProveedorOfSubministroListNewSubministro.getSubministroList().remove(subministroListNewSubministro);
                        oldProveedorOfSubministroListNewSubministro = em.merge(oldProveedorOfSubministroListNewSubministro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedor.getCod();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            Localizacion origen = proveedor.getOrigen();
            if (origen != null) {
                origen.getProveedorList().remove(proveedor);
                origen = em.merge(origen);
            }
            List<Subministro> subministroList = proveedor.getSubministroList();
            for (Subministro subministroListSubministro : subministroList) {
                subministroListSubministro.setProveedor(null);
                subministroListSubministro = em.merge(subministroListSubministro);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
