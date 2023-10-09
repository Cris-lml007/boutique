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
import model.DetalleSub;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.DetalleDis;
import model.Item;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class ItemJpaController implements Serializable {

    public ItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Item item) throws PreexistingEntityException, Exception {
        if (item.getDetalleSubList() == null) {
            item.setDetalleSubList(new ArrayList<DetalleSub>());
        }
        if (item.getDetalleDisList() == null) {
            item.setDetalleDisList(new ArrayList<DetalleDis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetalleSub> attachedDetalleSubList = new ArrayList<DetalleSub>();
            for (DetalleSub detalleSubListDetalleSubToAttach : item.getDetalleSubList()) {
                detalleSubListDetalleSubToAttach = em.getReference(detalleSubListDetalleSubToAttach.getClass(), detalleSubListDetalleSubToAttach.getId());
                attachedDetalleSubList.add(detalleSubListDetalleSubToAttach);
            }
            item.setDetalleSubList(attachedDetalleSubList);
            List<DetalleDis> attachedDetalleDisList = new ArrayList<DetalleDis>();
            for (DetalleDis detalleDisListDetalleDisToAttach : item.getDetalleDisList()) {
                detalleDisListDetalleDisToAttach = em.getReference(detalleDisListDetalleDisToAttach.getClass(), detalleDisListDetalleDisToAttach.getId());
                attachedDetalleDisList.add(detalleDisListDetalleDisToAttach);
            }
            item.setDetalleDisList(attachedDetalleDisList);
            em.persist(item);
            for (DetalleSub detalleSubListDetalleSub : item.getDetalleSubList()) {
                Item oldProductoOfDetalleSubListDetalleSub = detalleSubListDetalleSub.getProducto();
                detalleSubListDetalleSub.setProducto(item);
                detalleSubListDetalleSub = em.merge(detalleSubListDetalleSub);
                if (oldProductoOfDetalleSubListDetalleSub != null) {
                    oldProductoOfDetalleSubListDetalleSub.getDetalleSubList().remove(detalleSubListDetalleSub);
                    oldProductoOfDetalleSubListDetalleSub = em.merge(oldProductoOfDetalleSubListDetalleSub);
                }
            }
            for (DetalleDis detalleDisListDetalleDis : item.getDetalleDisList()) {
                Item oldProductoOfDetalleDisListDetalleDis = detalleDisListDetalleDis.getProducto();
                detalleDisListDetalleDis.setProducto(item);
                detalleDisListDetalleDis = em.merge(detalleDisListDetalleDis);
                if (oldProductoOfDetalleDisListDetalleDis != null) {
                    oldProductoOfDetalleDisListDetalleDis.getDetalleDisList().remove(detalleDisListDetalleDis);
                    oldProductoOfDetalleDisListDetalleDis = em.merge(oldProductoOfDetalleDisListDetalleDis);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findItem(item.getCod()) != null) {
                throw new PreexistingEntityException("Item " + item + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Item item) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item persistentItem = em.find(Item.class, item.getCod());
            List<DetalleSub> detalleSubListOld = persistentItem.getDetalleSubList();
            List<DetalleSub> detalleSubListNew = item.getDetalleSubList();
            List<DetalleDis> detalleDisListOld = persistentItem.getDetalleDisList();
            List<DetalleDis> detalleDisListNew = item.getDetalleDisList();
            List<DetalleSub> attachedDetalleSubListNew = new ArrayList<DetalleSub>();
            for (DetalleSub detalleSubListNewDetalleSubToAttach : detalleSubListNew) {
                detalleSubListNewDetalleSubToAttach = em.getReference(detalleSubListNewDetalleSubToAttach.getClass(), detalleSubListNewDetalleSubToAttach.getId());
                attachedDetalleSubListNew.add(detalleSubListNewDetalleSubToAttach);
            }
            detalleSubListNew = attachedDetalleSubListNew;
            item.setDetalleSubList(detalleSubListNew);
            List<DetalleDis> attachedDetalleDisListNew = new ArrayList<DetalleDis>();
            for (DetalleDis detalleDisListNewDetalleDisToAttach : detalleDisListNew) {
                detalleDisListNewDetalleDisToAttach = em.getReference(detalleDisListNewDetalleDisToAttach.getClass(), detalleDisListNewDetalleDisToAttach.getId());
                attachedDetalleDisListNew.add(detalleDisListNewDetalleDisToAttach);
            }
            detalleDisListNew = attachedDetalleDisListNew;
            item.setDetalleDisList(detalleDisListNew);
            item = em.merge(item);
            for (DetalleSub detalleSubListOldDetalleSub : detalleSubListOld) {
                if (!detalleSubListNew.contains(detalleSubListOldDetalleSub)) {
                    detalleSubListOldDetalleSub.setProducto(null);
                    detalleSubListOldDetalleSub = em.merge(detalleSubListOldDetalleSub);
                }
            }
            for (DetalleSub detalleSubListNewDetalleSub : detalleSubListNew) {
                if (!detalleSubListOld.contains(detalleSubListNewDetalleSub)) {
                    Item oldProductoOfDetalleSubListNewDetalleSub = detalleSubListNewDetalleSub.getProducto();
                    detalleSubListNewDetalleSub.setProducto(item);
                    detalleSubListNewDetalleSub = em.merge(detalleSubListNewDetalleSub);
                    if (oldProductoOfDetalleSubListNewDetalleSub != null && !oldProductoOfDetalleSubListNewDetalleSub.equals(item)) {
                        oldProductoOfDetalleSubListNewDetalleSub.getDetalleSubList().remove(detalleSubListNewDetalleSub);
                        oldProductoOfDetalleSubListNewDetalleSub = em.merge(oldProductoOfDetalleSubListNewDetalleSub);
                    }
                }
            }
            for (DetalleDis detalleDisListOldDetalleDis : detalleDisListOld) {
                if (!detalleDisListNew.contains(detalleDisListOldDetalleDis)) {
                    detalleDisListOldDetalleDis.setProducto(null);
                    detalleDisListOldDetalleDis = em.merge(detalleDisListOldDetalleDis);
                }
            }
            for (DetalleDis detalleDisListNewDetalleDis : detalleDisListNew) {
                if (!detalleDisListOld.contains(detalleDisListNewDetalleDis)) {
                    Item oldProductoOfDetalleDisListNewDetalleDis = detalleDisListNewDetalleDis.getProducto();
                    detalleDisListNewDetalleDis.setProducto(item);
                    detalleDisListNewDetalleDis = em.merge(detalleDisListNewDetalleDis);
                    if (oldProductoOfDetalleDisListNewDetalleDis != null && !oldProductoOfDetalleDisListNewDetalleDis.equals(item)) {
                        oldProductoOfDetalleDisListNewDetalleDis.getDetalleDisList().remove(detalleDisListNewDetalleDis);
                        oldProductoOfDetalleDisListNewDetalleDis = em.merge(oldProductoOfDetalleDisListNewDetalleDis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = item.getCod();
                if (findItem(id) == null) {
                    throw new NonexistentEntityException("The item with id " + id + " no longer exists.");
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
            Item item;
            try {
                item = em.getReference(Item.class, id);
                item.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The item with id " + id + " no longer exists.", enfe);
            }
            List<DetalleSub> detalleSubList = item.getDetalleSubList();
            for (DetalleSub detalleSubListDetalleSub : detalleSubList) {
                detalleSubListDetalleSub.setProducto(null);
                detalleSubListDetalleSub = em.merge(detalleSubListDetalleSub);
            }
            List<DetalleDis> detalleDisList = item.getDetalleDisList();
            for (DetalleDis detalleDisListDetalleDis : detalleDisList) {
                detalleDisListDetalleDis.setProducto(null);
                detalleDisListDetalleDis = em.merge(detalleDisListDetalleDis);
            }
            em.remove(item);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Item> findItemEntities() {
        return findItemEntities(true, -1, -1);
    }

    public List<Item> findItemEntities(int maxResults, int firstResult) {
        return findItemEntities(false, maxResults, firstResult);
    }

    private List<Item> findItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Item.class));
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

    public Item findItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Item.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Item> rt = cq.from(Item.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
