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
import model.Subministro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Distribucion;
import model.Empleado;
import model.Estado;
import model.HistorialItem;
import model.Rol;
import model.md5;
import persistent.exceptions.IllegalOrphanException;
import persistent.exceptions.NonexistentEntityException;
import persistent.exceptions.PreexistingEntityException;

/**
 *
 * @author metallica
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, Exception {
        if (empleado.getSubministroList() == null) {
            empleado.setSubministroList(new ArrayList<Subministro>());
        }
        if (empleado.getDistribucionList() == null) {
            empleado.setDistribucionList(new ArrayList<Distribucion>());
        }
        if (empleado.getDistribucionList1() == null) {
            empleado.setDistribucionList1(new ArrayList<Distribucion>());
        }
        if (empleado.getHistorialItemList() == null) {
            empleado.setHistorialItemList(new ArrayList<HistorialItem>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Subministro> attachedSubministroList = new ArrayList<Subministro>();
            for (Subministro subministroListSubministroToAttach : empleado.getSubministroList()) {
                subministroListSubministroToAttach = em.getReference(subministroListSubministroToAttach.getClass(), subministroListSubministroToAttach.getCod());
                attachedSubministroList.add(subministroListSubministroToAttach);
            }
            empleado.setSubministroList(attachedSubministroList);
            List<Distribucion> attachedDistribucionList = new ArrayList<Distribucion>();
            for (Distribucion distribucionListDistribucionToAttach : empleado.getDistribucionList()) {
                distribucionListDistribucionToAttach = em.getReference(distribucionListDistribucionToAttach.getClass(), distribucionListDistribucionToAttach.getId());
                attachedDistribucionList.add(distribucionListDistribucionToAttach);
            }
            empleado.setDistribucionList(attachedDistribucionList);
            List<Distribucion> attachedDistribucionList1 = new ArrayList<Distribucion>();
            for (Distribucion distribucionList1DistribucionToAttach : empleado.getDistribucionList1()) {
                distribucionList1DistribucionToAttach = em.getReference(distribucionList1DistribucionToAttach.getClass(), distribucionList1DistribucionToAttach.getId());
                attachedDistribucionList1.add(distribucionList1DistribucionToAttach);
            }
            empleado.setDistribucionList1(attachedDistribucionList1);
            List<HistorialItem> attachedHistorialItemList = new ArrayList<HistorialItem>();
            for (HistorialItem historialItemListHistorialItemToAttach : empleado.getHistorialItemList()) {
                historialItemListHistorialItemToAttach = em.getReference(historialItemListHistorialItemToAttach.getClass(), historialItemListHistorialItemToAttach.getId());
                attachedHistorialItemList.add(historialItemListHistorialItemToAttach);
            }
            empleado.setHistorialItemList(attachedHistorialItemList);
            em.persist(empleado);
            for (Subministro subministroListSubministro : empleado.getSubministroList()) {
                Empleado oldEmpleadoOfSubministroListSubministro = subministroListSubministro.getEmpleado();
                subministroListSubministro.setEmpleado(empleado);
                subministroListSubministro = em.merge(subministroListSubministro);
                if (oldEmpleadoOfSubministroListSubministro != null) {
                    oldEmpleadoOfSubministroListSubministro.getSubministroList().remove(subministroListSubministro);
                    oldEmpleadoOfSubministroListSubministro = em.merge(oldEmpleadoOfSubministroListSubministro);
                }
            }
            for (Distribucion distribucionListDistribucion : empleado.getDistribucionList()) {
                Empleado oldEmpleadoOfDistribucionListDistribucion = distribucionListDistribucion.getEmpleado();
                distribucionListDistribucion.setEmpleado(empleado);
                distribucionListDistribucion = em.merge(distribucionListDistribucion);
                if (oldEmpleadoOfDistribucionListDistribucion != null) {
                    oldEmpleadoOfDistribucionListDistribucion.getDistribucionList().remove(distribucionListDistribucion);
                    oldEmpleadoOfDistribucionListDistribucion = em.merge(oldEmpleadoOfDistribucionListDistribucion);
                }
            }
            for (Distribucion distribucionList1Distribucion : empleado.getDistribucionList1()) {
                Empleado oldEncargadoOfDistribucionList1Distribucion = distribucionList1Distribucion.getEncargado();
                distribucionList1Distribucion.setEncargado(empleado);
                distribucionList1Distribucion = em.merge(distribucionList1Distribucion);
                if (oldEncargadoOfDistribucionList1Distribucion != null) {
                    oldEncargadoOfDistribucionList1Distribucion.getDistribucionList1().remove(distribucionList1Distribucion);
                    oldEncargadoOfDistribucionList1Distribucion = em.merge(oldEncargadoOfDistribucionList1Distribucion);
                }
            }
            for (HistorialItem historialItemListHistorialItem : empleado.getHistorialItemList()) {
                Empleado oldEmpleadoOfHistorialItemListHistorialItem = historialItemListHistorialItem.getEmpleado();
                historialItemListHistorialItem.setEmpleado(empleado);
                historialItemListHistorialItem = em.merge(historialItemListHistorialItem);
                if (oldEmpleadoOfHistorialItemListHistorialItem != null) {
                    oldEmpleadoOfHistorialItemListHistorialItem.getHistorialItemList().remove(historialItemListHistorialItem);
                    oldEmpleadoOfHistorialItemListHistorialItem = em.merge(oldEmpleadoOfHistorialItemListHistorialItem);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getCi()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getCi());
            List<Subministro> subministroListOld = persistentEmpleado.getSubministroList();
            List<Subministro> subministroListNew = empleado.getSubministroList();
            List<Distribucion> distribucionListOld = persistentEmpleado.getDistribucionList();
            List<Distribucion> distribucionListNew = empleado.getDistribucionList();
            List<Distribucion> distribucionList1Old = persistentEmpleado.getDistribucionList1();
            List<Distribucion> distribucionList1New = empleado.getDistribucionList1();
            List<HistorialItem> historialItemListOld = persistentEmpleado.getHistorialItemList();
            List<HistorialItem> historialItemListNew = empleado.getHistorialItemList();
            List<String> illegalOrphanMessages = null;
            for (HistorialItem historialItemListOldHistorialItem : historialItemListOld) {
                if (!historialItemListNew.contains(historialItemListOldHistorialItem)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain HistorialItem " + historialItemListOldHistorialItem + " since its empleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Subministro> attachedSubministroListNew = new ArrayList<Subministro>();
            for (Subministro subministroListNewSubministroToAttach : subministroListNew) {
                subministroListNewSubministroToAttach = em.getReference(subministroListNewSubministroToAttach.getClass(), subministroListNewSubministroToAttach.getCod());
                attachedSubministroListNew.add(subministroListNewSubministroToAttach);
            }
            subministroListNew = attachedSubministroListNew;
            empleado.setSubministroList(subministroListNew);
            List<Distribucion> attachedDistribucionListNew = new ArrayList<Distribucion>();
            for (Distribucion distribucionListNewDistribucionToAttach : distribucionListNew) {
                distribucionListNewDistribucionToAttach = em.getReference(distribucionListNewDistribucionToAttach.getClass(), distribucionListNewDistribucionToAttach.getId());
                attachedDistribucionListNew.add(distribucionListNewDistribucionToAttach);
            }
            distribucionListNew = attachedDistribucionListNew;
            empleado.setDistribucionList(distribucionListNew);
            List<Distribucion> attachedDistribucionList1New = new ArrayList<Distribucion>();
            for (Distribucion distribucionList1NewDistribucionToAttach : distribucionList1New) {
                distribucionList1NewDistribucionToAttach = em.getReference(distribucionList1NewDistribucionToAttach.getClass(), distribucionList1NewDistribucionToAttach.getId());
                attachedDistribucionList1New.add(distribucionList1NewDistribucionToAttach);
            }
            distribucionList1New = attachedDistribucionList1New;
            empleado.setDistribucionList1(distribucionList1New);
            List<HistorialItem> attachedHistorialItemListNew = new ArrayList<HistorialItem>();
            for (HistorialItem historialItemListNewHistorialItemToAttach : historialItemListNew) {
                historialItemListNewHistorialItemToAttach = em.getReference(historialItemListNewHistorialItemToAttach.getClass(), historialItemListNewHistorialItemToAttach.getId());
                attachedHistorialItemListNew.add(historialItemListNewHistorialItemToAttach);
            }
            historialItemListNew = attachedHistorialItemListNew;
            empleado.setHistorialItemList(historialItemListNew);
            empleado = em.merge(empleado);
            for (Subministro subministroListOldSubministro : subministroListOld) {
                if (!subministroListNew.contains(subministroListOldSubministro)) {
                    subministroListOldSubministro.setEmpleado(null);
                    subministroListOldSubministro = em.merge(subministroListOldSubministro);
                }
            }
            for (Subministro subministroListNewSubministro : subministroListNew) {
                if (!subministroListOld.contains(subministroListNewSubministro)) {
                    Empleado oldEmpleadoOfSubministroListNewSubministro = subministroListNewSubministro.getEmpleado();
                    subministroListNewSubministro.setEmpleado(empleado);
                    subministroListNewSubministro = em.merge(subministroListNewSubministro);
                    if (oldEmpleadoOfSubministroListNewSubministro != null && !oldEmpleadoOfSubministroListNewSubministro.equals(empleado)) {
                        oldEmpleadoOfSubministroListNewSubministro.getSubministroList().remove(subministroListNewSubministro);
                        oldEmpleadoOfSubministroListNewSubministro = em.merge(oldEmpleadoOfSubministroListNewSubministro);
                    }
                }
            }
            for (Distribucion distribucionListOldDistribucion : distribucionListOld) {
                if (!distribucionListNew.contains(distribucionListOldDistribucion)) {
                    distribucionListOldDistribucion.setEmpleado(null);
                    distribucionListOldDistribucion = em.merge(distribucionListOldDistribucion);
                }
            }
            for (Distribucion distribucionListNewDistribucion : distribucionListNew) {
                if (!distribucionListOld.contains(distribucionListNewDistribucion)) {
                    Empleado oldEmpleadoOfDistribucionListNewDistribucion = distribucionListNewDistribucion.getEmpleado();
                    distribucionListNewDistribucion.setEmpleado(empleado);
                    distribucionListNewDistribucion = em.merge(distribucionListNewDistribucion);
                    if (oldEmpleadoOfDistribucionListNewDistribucion != null && !oldEmpleadoOfDistribucionListNewDistribucion.equals(empleado)) {
                        oldEmpleadoOfDistribucionListNewDistribucion.getDistribucionList().remove(distribucionListNewDistribucion);
                        oldEmpleadoOfDistribucionListNewDistribucion = em.merge(oldEmpleadoOfDistribucionListNewDistribucion);
                    }
                }
            }
            for (Distribucion distribucionList1OldDistribucion : distribucionList1Old) {
                if (!distribucionList1New.contains(distribucionList1OldDistribucion)) {
                    distribucionList1OldDistribucion.setEncargado(null);
                    distribucionList1OldDistribucion = em.merge(distribucionList1OldDistribucion);
                }
            }
            for (Distribucion distribucionList1NewDistribucion : distribucionList1New) {
                if (!distribucionList1Old.contains(distribucionList1NewDistribucion)) {
                    Empleado oldEncargadoOfDistribucionList1NewDistribucion = distribucionList1NewDistribucion.getEncargado();
                    distribucionList1NewDistribucion.setEncargado(empleado);
                    distribucionList1NewDistribucion = em.merge(distribucionList1NewDistribucion);
                    if (oldEncargadoOfDistribucionList1NewDistribucion != null && !oldEncargadoOfDistribucionList1NewDistribucion.equals(empleado)) {
                        oldEncargadoOfDistribucionList1NewDistribucion.getDistribucionList1().remove(distribucionList1NewDistribucion);
                        oldEncargadoOfDistribucionList1NewDistribucion = em.merge(oldEncargadoOfDistribucionList1NewDistribucion);
                    }
                }
            }
            for (HistorialItem historialItemListNewHistorialItem : historialItemListNew) {
                if (!historialItemListOld.contains(historialItemListNewHistorialItem)) {
                    Empleado oldEmpleadoOfHistorialItemListNewHistorialItem = historialItemListNewHistorialItem.getEmpleado();
                    historialItemListNewHistorialItem.setEmpleado(empleado);
                    historialItemListNewHistorialItem = em.merge(historialItemListNewHistorialItem);
                    if (oldEmpleadoOfHistorialItemListNewHistorialItem != null && !oldEmpleadoOfHistorialItemListNewHistorialItem.equals(empleado)) {
                        oldEmpleadoOfHistorialItemListNewHistorialItem.getHistorialItemList().remove(historialItemListNewHistorialItem);
                        oldEmpleadoOfHistorialItemListNewHistorialItem = em.merge(oldEmpleadoOfHistorialItemListNewHistorialItem);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getCi();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getCi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<HistorialItem> historialItemListOrphanCheck = empleado.getHistorialItemList();
            for (HistorialItem historialItemListOrphanCheckHistorialItem : historialItemListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the HistorialItem " + historialItemListOrphanCheckHistorialItem + " in its historialItemList field has a non-nullable empleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Subministro> subministroList = empleado.getSubministroList();
            for (Subministro subministroListSubministro : subministroList) {
                subministroListSubministro.setEmpleado(null);
                subministroListSubministro = em.merge(subministroListSubministro);
            }
            List<Distribucion> distribucionList = empleado.getDistribucionList();
            for (Distribucion distribucionListDistribucion : distribucionList) {
                distribucionListDistribucion.setEmpleado(null);
                distribucionListDistribucion = em.merge(distribucionListDistribucion);
            }
            List<Distribucion> distribucionList1 = empleado.getDistribucionList1();
            for (Distribucion distribucionList1Distribucion : distribucionList1) {
                distribucionList1Distribucion.setEncargado(null);
                distribucionList1Distribucion = em.merge(distribucionList1Distribucion);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List<Empleado> findEmpleadoEntities(boolean active){
        if(active){
            EntityManager e=getEntityManager();
            List <Empleado> l=new ArrayList<>();
            String sql="SELECT u FROM Empleado u WHERE u.activo = :p";
            Query query=e.createQuery(sql);
            query.setParameter("p", Estado.activo);
            l=query.getResultList();
            return l;
        }else return findEmpleadoEntities();
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Empleado login(String usuario,String contraseña){
        EntityManager em=getEntityManager();
        List<Empleado> emp=new ArrayList<>();
        try{
            Query query=em.createQuery("SELECT u FROM Empleado u WHERE u.usuario = :us AND u.contraseña = :pass AND u.rol IN ( :rol1 , :rol2) AND u.activo = :a");
            query.setParameter("us", usuario);
            query.setParameter("pass", md5.getMD5Hash(contraseña));
            query.setParameter("rol1", Rol.administrativo);
            query.setParameter("rol2", Rol.gerente);
            query.setParameter("a", Estado.activo);
            emp=query.getResultList();
        }finally{
            return (!emp.isEmpty() ? emp.get(0) : null);
        }
    }
    
}
