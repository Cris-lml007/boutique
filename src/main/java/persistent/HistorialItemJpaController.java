/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Item;
import model.Empleado;
import model.HistorialItem;
import persistent.exceptions.NonexistentEntityException;

/**
 *
 * @author metallica
 */
public class HistorialItemJpaController implements Serializable {

    public HistorialItemJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(HistorialItem historialItem) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Item item = historialItem.getItem();
            if (item != null) {
                item = em.getReference(item.getClass(), item.getCod());
                historialItem.setItem(item);
            }
            Empleado empleado = historialItem.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getCi());
                historialItem.setEmpleado(empleado);
            }
            em.persist(historialItem);
            if (item != null) {
                item.getHistorialItemList().add(historialItem);
                item = em.merge(item);
            }
            if (empleado != null) {
                empleado.getHistorialItemList().add(historialItem);
                empleado = em.merge(empleado);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HistorialItem historialItem) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HistorialItem persistentHistorialItem = em.find(HistorialItem.class, historialItem.getId());
            Item itemOld = persistentHistorialItem.getItem();
            Item itemNew = historialItem.getItem();
            Empleado empleadoOld = persistentHistorialItem.getEmpleado();
            Empleado empleadoNew = historialItem.getEmpleado();
            if (itemNew != null) {
                itemNew = em.getReference(itemNew.getClass(), itemNew.getCod());
                historialItem.setItem(itemNew);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getCi());
                historialItem.setEmpleado(empleadoNew);
            }
            historialItem = em.merge(historialItem);
            if (itemOld != null && !itemOld.equals(itemNew)) {
                itemOld.getHistorialItemList().remove(historialItem);
                itemOld = em.merge(itemOld);
            }
            if (itemNew != null && !itemNew.equals(itemOld)) {
                itemNew.getHistorialItemList().add(historialItem);
                itemNew = em.merge(itemNew);
            }
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                empleadoOld.getHistorialItemList().remove(historialItem);
                empleadoOld = em.merge(empleadoOld);
            }
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                empleadoNew.getHistorialItemList().add(historialItem);
                empleadoNew = em.merge(empleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historialItem.getId();
                if (findHistorialItem(id) == null) {
                    throw new NonexistentEntityException("The historialItem with id " + id + " no longer exists.");
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
            HistorialItem historialItem;
            try {
                historialItem = em.getReference(HistorialItem.class, id);
                historialItem.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historialItem with id " + id + " no longer exists.", enfe);
            }
            Item item = historialItem.getItem();
            if (item != null) {
                item.getHistorialItemList().remove(historialItem);
                item = em.merge(item);
            }
            Empleado empleado = historialItem.getEmpleado();
            if (empleado != null) {
                empleado.getHistorialItemList().remove(historialItem);
                empleado = em.merge(empleado);
            }
            em.remove(historialItem);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HistorialItem> findHistorialItemEntities() {
        return findHistorialItemEntities(true, -1, -1);
    }

    public List<HistorialItem> findHistorialItemEntities(int maxResults, int firstResult) {
        return findHistorialItemEntities(false, maxResults, firstResult);
    }

    private List<HistorialItem> findHistorialItemEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HistorialItem.class));
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

    public HistorialItem findHistorialItem(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HistorialItem.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistorialItemCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HistorialItem> rt = cq.from(HistorialItem.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<HistorialItem>QuerySQL(String sql,Map<String,Object>parameters){
        EntityManager em = getEntityManager();
        List<HistorialItem>l=new ArrayList<>();
        try{
            Query query=em.createNativeQuery(sql,HistorialItem.class);
            Iterator it = parameters.keySet().iterator();
            while(it.hasNext()){
                String key=it.next().toString();
                System.out.println("la llave es: "+key + "= "+parameters.get(key));
                query.setParameter(key, parameters.get(key));
            }
            l=query.getResultList();
        }catch(Exception e){
            System.out.println("error al buscar sql: "+e);
        }finally{
            return l;
        }
    }
    
    public List<HistorialItem>findHistorialDate(java.sql.Date a){
        return findHistorialDate(a, a);
    }
    
    public List<HistorialItem> findHistorialDate(java.sql.Date a,java.sql.Date b){
        EntityManager em=getEntityManager();
        List <HistorialItem> l =new ArrayList();
        try{
            String sql="SELECT u.* FROM HISTORIAL_ITEM u WHERE DATE(u.FECHA) >= DATE( ?f1 ) AND DATE(u.FECHA) <= DATE( ?f2 )";
            Query query=em.createNativeQuery(sql,HistorialItem.class);
            query.setParameter("f1", a);
            query.setParameter("f2",b);
            l=query.getResultList();
        }catch(Exception e){
            System.out.println("hubo un error al recuperar por fecha: "+e);
        }finally{
            return l;
        }
    }
    
}
