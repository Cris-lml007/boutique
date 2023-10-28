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
import model.Distribucion;
import model.ProveedorDistribuidor;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class ProveedorDistribuidorJpaController implements Serializable {

    public ProveedorDistribuidorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProveedorDistribuidor proveedorDistribuidor) throws PreexistingEntityException, Exception {
        if (proveedorDistribuidor.getSubministroList() == null) {
            proveedorDistribuidor.setSubministroList(new ArrayList<Subministro>());
        }
        if (proveedorDistribuidor.getDistribucionList() == null) {
            proveedorDistribuidor.setDistribucionList(new ArrayList<Distribucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion origen = proveedorDistribuidor.getOrigen();
            if (origen != null) {
                origen = em.getReference(origen.getClass(), origen.getCod());
                proveedorDistribuidor.setOrigen(origen);
            }
            List<Subministro> attachedSubministroList = new ArrayList<Subministro>();
            for (Subministro subministroListSubministroToAttach : proveedorDistribuidor.getSubministroList()) {
                subministroListSubministroToAttach = em.getReference(subministroListSubministroToAttach.getClass(), subministroListSubministroToAttach.getCod());
                attachedSubministroList.add(subministroListSubministroToAttach);
            }
            proveedorDistribuidor.setSubministroList(attachedSubministroList);
            List<Distribucion> attachedDistribucionList = new ArrayList<Distribucion>();
            for (Distribucion distribucionListDistribucionToAttach : proveedorDistribuidor.getDistribucionList()) {
                distribucionListDistribucionToAttach = em.getReference(distribucionListDistribucionToAttach.getClass(), distribucionListDistribucionToAttach.getId());
                attachedDistribucionList.add(distribucionListDistribucionToAttach);
            }
            proveedorDistribuidor.setDistribucionList(attachedDistribucionList);
            em.persist(proveedorDistribuidor);
            if (origen != null) {
                origen.getProveedorDistribuidorList().add(proveedorDistribuidor);
                origen = em.merge(origen);
            }
            for (Subministro subministroListSubministro : proveedorDistribuidor.getSubministroList()) {
                ProveedorDistribuidor oldProveedorOfSubministroListSubministro = subministroListSubministro.getProveedor();
                subministroListSubministro.setProveedor(proveedorDistribuidor);
                subministroListSubministro = em.merge(subministroListSubministro);
                if (oldProveedorOfSubministroListSubministro != null) {
                    oldProveedorOfSubministroListSubministro.getSubministroList().remove(subministroListSubministro);
                    oldProveedorOfSubministroListSubministro = em.merge(oldProveedorOfSubministroListSubministro);
                }
            }
            for (Distribucion distribucionListDistribucion : proveedorDistribuidor.getDistribucionList()) {
                ProveedorDistribuidor oldDestinoOfDistribucionListDistribucion = distribucionListDistribucion.getDestino();
                distribucionListDistribucion.setDestino(proveedorDistribuidor);
                distribucionListDistribucion = em.merge(distribucionListDistribucion);
                if (oldDestinoOfDistribucionListDistribucion != null) {
                    oldDestinoOfDistribucionListDistribucion.getDistribucionList().remove(distribucionListDistribucion);
                    oldDestinoOfDistribucionListDistribucion = em.merge(oldDestinoOfDistribucionListDistribucion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProveedorDistribuidor(proveedorDistribuidor.getCod()) != null) {
                throw new PreexistingEntityException("ProveedorDistribuidor " + proveedorDistribuidor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProveedorDistribuidor proveedorDistribuidor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProveedorDistribuidor persistentProveedorDistribuidor = em.find(ProveedorDistribuidor.class, proveedorDistribuidor.getCod());
            Localizacion origenOld = persistentProveedorDistribuidor.getOrigen();
            Localizacion origenNew = proveedorDistribuidor.getOrigen();
            List<Subministro> subministroListOld = persistentProveedorDistribuidor.getSubministroList();
            List<Subministro> subministroListNew = proveedorDistribuidor.getSubministroList();
            List<Distribucion> distribucionListOld = persistentProveedorDistribuidor.getDistribucionList();
            List<Distribucion> distribucionListNew = proveedorDistribuidor.getDistribucionList();
            if (origenNew != null) {
                origenNew = em.getReference(origenNew.getClass(), origenNew.getCod());
                proveedorDistribuidor.setOrigen(origenNew);
            }
            List<Subministro> attachedSubministroListNew = new ArrayList<Subministro>();
            for (Subministro subministroListNewSubministroToAttach : subministroListNew) {
                subministroListNewSubministroToAttach = em.getReference(subministroListNewSubministroToAttach.getClass(), subministroListNewSubministroToAttach.getCod());
                attachedSubministroListNew.add(subministroListNewSubministroToAttach);
            }
            subministroListNew = attachedSubministroListNew;
            proveedorDistribuidor.setSubministroList(subministroListNew);
            List<Distribucion> attachedDistribucionListNew = new ArrayList<Distribucion>();
            for (Distribucion distribucionListNewDistribucionToAttach : distribucionListNew) {
                distribucionListNewDistribucionToAttach = em.getReference(distribucionListNewDistribucionToAttach.getClass(), distribucionListNewDistribucionToAttach.getId());
                attachedDistribucionListNew.add(distribucionListNewDistribucionToAttach);
            }
            distribucionListNew = attachedDistribucionListNew;
            proveedorDistribuidor.setDistribucionList(distribucionListNew);
            proveedorDistribuidor = em.merge(proveedorDistribuidor);
            if (origenOld != null && !origenOld.equals(origenNew)) {
                origenOld.getProveedorDistribuidorList().remove(proveedorDistribuidor);
                origenOld = em.merge(origenOld);
            }
            if (origenNew != null && !origenNew.equals(origenOld)) {
                origenNew.getProveedorDistribuidorList().add(proveedorDistribuidor);
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
                    ProveedorDistribuidor oldProveedorOfSubministroListNewSubministro = subministroListNewSubministro.getProveedor();
                    subministroListNewSubministro.setProveedor(proveedorDistribuidor);
                    subministroListNewSubministro = em.merge(subministroListNewSubministro);
                    if (oldProveedorOfSubministroListNewSubministro != null && !oldProveedorOfSubministroListNewSubministro.equals(proveedorDistribuidor)) {
                        oldProveedorOfSubministroListNewSubministro.getSubministroList().remove(subministroListNewSubministro);
                        oldProveedorOfSubministroListNewSubministro = em.merge(oldProveedorOfSubministroListNewSubministro);
                    }
                }
            }
            for (Distribucion distribucionListOldDistribucion : distribucionListOld) {
                if (!distribucionListNew.contains(distribucionListOldDistribucion)) {
                    distribucionListOldDistribucion.setDestino(null);
                    distribucionListOldDistribucion = em.merge(distribucionListOldDistribucion);
                }
            }
            for (Distribucion distribucionListNewDistribucion : distribucionListNew) {
                if (!distribucionListOld.contains(distribucionListNewDistribucion)) {
                    ProveedorDistribuidor oldDestinoOfDistribucionListNewDistribucion = distribucionListNewDistribucion.getDestino();
                    distribucionListNewDistribucion.setDestino(proveedorDistribuidor);
                    distribucionListNewDistribucion = em.merge(distribucionListNewDistribucion);
                    if (oldDestinoOfDistribucionListNewDistribucion != null && !oldDestinoOfDistribucionListNewDistribucion.equals(proveedorDistribuidor)) {
                        oldDestinoOfDistribucionListNewDistribucion.getDistribucionList().remove(distribucionListNewDistribucion);
                        oldDestinoOfDistribucionListNewDistribucion = em.merge(oldDestinoOfDistribucionListNewDistribucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedorDistribuidor.getCod();
                if (findProveedorDistribuidor(id) == null) {
                    throw new NonexistentEntityException("The proveedorDistribuidor with id " + id + " no longer exists.");
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
            ProveedorDistribuidor proveedorDistribuidor;
            try {
                proveedorDistribuidor = em.getReference(ProveedorDistribuidor.class, id);
                proveedorDistribuidor.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedorDistribuidor with id " + id + " no longer exists.", enfe);
            }
            Localizacion origen = proveedorDistribuidor.getOrigen();
            if (origen != null) {
                origen.getProveedorDistribuidorList().remove(proveedorDistribuidor);
                origen = em.merge(origen);
            }
            List<Subministro> subministroList = proveedorDistribuidor.getSubministroList();
            for (Subministro subministroListSubministro : subministroList) {
                subministroListSubministro.setProveedor(null);
                subministroListSubministro = em.merge(subministroListSubministro);
            }
            List<Distribucion> distribucionList = proveedorDistribuidor.getDistribucionList();
            for (Distribucion distribucionListDistribucion : distribucionList) {
                distribucionListDistribucion.setDestino(null);
                distribucionListDistribucion = em.merge(distribucionListDistribucion);
            }
            em.remove(proveedorDistribuidor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProveedorDistribuidor> findProveedorDistribuidorEntities() {
        return findProveedorDistribuidorEntities(true, -1, -1);
    }

    public List<ProveedorDistribuidor> findProveedorDistribuidorEntities(int maxResults, int firstResult) {
        return findProveedorDistribuidorEntities(false, maxResults, firstResult);
    }

    private List<ProveedorDistribuidor> findProveedorDistribuidorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProveedorDistribuidor.class));
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

    public ProveedorDistribuidor findProveedorDistribuidor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProveedorDistribuidor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorDistribuidorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProveedorDistribuidor> rt = cq.from(ProveedorDistribuidor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
