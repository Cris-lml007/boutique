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
import model.Distribucion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Destino;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class DestinoJpaController implements Serializable {

    public DestinoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Destino destino) throws PreexistingEntityException, Exception {
        if (destino.getDistribucionList() == null) {
            destino.setDistribucionList(new ArrayList<Distribucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localizacion origen = destino.getOrigen();
            if (origen != null) {
                origen = em.getReference(origen.getClass(), origen.getCod());
                destino.setOrigen(origen);
            }
            List<Distribucion> attachedDistribucionList = new ArrayList<Distribucion>();
            for (Distribucion distribucionListDistribucionToAttach : destino.getDistribucionList()) {
                distribucionListDistribucionToAttach = em.getReference(distribucionListDistribucionToAttach.getClass(), distribucionListDistribucionToAttach.getId());
                attachedDistribucionList.add(distribucionListDistribucionToAttach);
            }
            destino.setDistribucionList(attachedDistribucionList);
            em.persist(destino);
            if (origen != null) {
                origen.getDestinoList().add(destino);
                origen = em.merge(origen);
            }
            for (Distribucion distribucionListDistribucion : destino.getDistribucionList()) {
                Destino oldDestinoOfDistribucionListDistribucion = distribucionListDistribucion.getDestino();
                distribucionListDistribucion.setDestino(destino);
                distribucionListDistribucion = em.merge(distribucionListDistribucion);
                if (oldDestinoOfDistribucionListDistribucion != null) {
                    oldDestinoOfDistribucionListDistribucion.getDistribucionList().remove(distribucionListDistribucion);
                    oldDestinoOfDistribucionListDistribucion = em.merge(oldDestinoOfDistribucionListDistribucion);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDestino(destino.getId()) != null) {
                throw new PreexistingEntityException("Destino " + destino + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Destino destino) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Destino persistentDestino = em.find(Destino.class, destino.getId());
            Localizacion origenOld = persistentDestino.getOrigen();
            Localizacion origenNew = destino.getOrigen();
            List<Distribucion> distribucionListOld = persistentDestino.getDistribucionList();
            List<Distribucion> distribucionListNew = destino.getDistribucionList();
            if (origenNew != null) {
                origenNew = em.getReference(origenNew.getClass(), origenNew.getCod());
                destino.setOrigen(origenNew);
            }
            List<Distribucion> attachedDistribucionListNew = new ArrayList<Distribucion>();
            for (Distribucion distribucionListNewDistribucionToAttach : distribucionListNew) {
                distribucionListNewDistribucionToAttach = em.getReference(distribucionListNewDistribucionToAttach.getClass(), distribucionListNewDistribucionToAttach.getId());
                attachedDistribucionListNew.add(distribucionListNewDistribucionToAttach);
            }
            distribucionListNew = attachedDistribucionListNew;
            destino.setDistribucionList(distribucionListNew);
            destino = em.merge(destino);
            if (origenOld != null && !origenOld.equals(origenNew)) {
                origenOld.getDestinoList().remove(destino);
                origenOld = em.merge(origenOld);
            }
            if (origenNew != null && !origenNew.equals(origenOld)) {
                origenNew.getDestinoList().add(destino);
                origenNew = em.merge(origenNew);
            }
            for (Distribucion distribucionListOldDistribucion : distribucionListOld) {
                if (!distribucionListNew.contains(distribucionListOldDistribucion)) {
                    distribucionListOldDistribucion.setDestino(null);
                    distribucionListOldDistribucion = em.merge(distribucionListOldDistribucion);
                }
            }
            for (Distribucion distribucionListNewDistribucion : distribucionListNew) {
                if (!distribucionListOld.contains(distribucionListNewDistribucion)) {
                    Destino oldDestinoOfDistribucionListNewDistribucion = distribucionListNewDistribucion.getDestino();
                    distribucionListNewDistribucion.setDestino(destino);
                    distribucionListNewDistribucion = em.merge(distribucionListNewDistribucion);
                    if (oldDestinoOfDistribucionListNewDistribucion != null && !oldDestinoOfDistribucionListNewDistribucion.equals(destino)) {
                        oldDestinoOfDistribucionListNewDistribucion.getDistribucionList().remove(distribucionListNewDistribucion);
                        oldDestinoOfDistribucionListNewDistribucion = em.merge(oldDestinoOfDistribucionListNewDistribucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = destino.getId();
                if (findDestino(id) == null) {
                    throw new NonexistentEntityException("The destino with id " + id + " no longer exists.");
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
            Destino destino;
            try {
                destino = em.getReference(Destino.class, id);
                destino.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The destino with id " + id + " no longer exists.", enfe);
            }
            Localizacion origen = destino.getOrigen();
            if (origen != null) {
                origen.getDestinoList().remove(destino);
                origen = em.merge(origen);
            }
            List<Distribucion> distribucionList = destino.getDistribucionList();
            for (Distribucion distribucionListDistribucion : distribucionList) {
                distribucionListDistribucion.setDestino(null);
                distribucionListDistribucion = em.merge(distribucionListDistribucion);
            }
            em.remove(destino);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Destino> findDestinoEntities() {
        return findDestinoEntities(true, -1, -1);
    }

    public List<Destino> findDestinoEntities(int maxResults, int firstResult) {
        return findDestinoEntities(false, maxResults, firstResult);
    }

    private List<Destino> findDestinoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Destino.class));
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

    public Destino findDestino(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Destino.class, id);
        } finally {
            em.close();
        }
    }

    public int getDestinoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Destino> rt = cq.from(Destino.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
