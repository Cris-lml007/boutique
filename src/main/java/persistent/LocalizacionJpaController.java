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
import model.Proveedor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Destino;
import model.Almacen;
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
        if (localizacion.getProveedorList() == null) {
            localizacion.setProveedorList(new ArrayList<Proveedor>());
        }
        if (localizacion.getDestinoList() == null) {
            localizacion.setDestinoList(new ArrayList<Destino>());
        }
        if (localizacion.getAlmacenList() == null) {
            localizacion.setAlmacenList(new ArrayList<Almacen>());
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
            List<Proveedor> attachedProveedorList = new ArrayList<Proveedor>();
            for (Proveedor proveedorListProveedorToAttach : localizacion.getProveedorList()) {
                proveedorListProveedorToAttach = em.getReference(proveedorListProveedorToAttach.getClass(), proveedorListProveedorToAttach.getCod());
                attachedProveedorList.add(proveedorListProveedorToAttach);
            }
            localizacion.setProveedorList(attachedProveedorList);
            List<Destino> attachedDestinoList = new ArrayList<Destino>();
            for (Destino destinoListDestinoToAttach : localizacion.getDestinoList()) {
                destinoListDestinoToAttach = em.getReference(destinoListDestinoToAttach.getClass(), destinoListDestinoToAttach.getId());
                attachedDestinoList.add(destinoListDestinoToAttach);
            }
            localizacion.setDestinoList(attachedDestinoList);
            List<Almacen> attachedAlmacenList = new ArrayList<Almacen>();
            for (Almacen almacenListAlmacenToAttach : localizacion.getAlmacenList()) {
                almacenListAlmacenToAttach = em.getReference(almacenListAlmacenToAttach.getClass(), almacenListAlmacenToAttach.getCod());
                attachedAlmacenList.add(almacenListAlmacenToAttach);
            }
            localizacion.setAlmacenList(attachedAlmacenList);
            em.persist(localizacion);
            if (pais != null) {
                pais.getLocalizacionList().add(localizacion);
                pais = em.merge(pais);
            }
            for (Proveedor proveedorListProveedor : localizacion.getProveedorList()) {
                Localizacion oldOrigenOfProveedorListProveedor = proveedorListProveedor.getOrigen();
                proveedorListProveedor.setOrigen(localizacion);
                proveedorListProveedor = em.merge(proveedorListProveedor);
                if (oldOrigenOfProveedorListProveedor != null) {
                    oldOrigenOfProveedorListProveedor.getProveedorList().remove(proveedorListProveedor);
                    oldOrigenOfProveedorListProveedor = em.merge(oldOrigenOfProveedorListProveedor);
                }
            }
            for (Destino destinoListDestino : localizacion.getDestinoList()) {
                Localizacion oldOrigenOfDestinoListDestino = destinoListDestino.getOrigen();
                destinoListDestino.setOrigen(localizacion);
                destinoListDestino = em.merge(destinoListDestino);
                if (oldOrigenOfDestinoListDestino != null) {
                    oldOrigenOfDestinoListDestino.getDestinoList().remove(destinoListDestino);
                    oldOrigenOfDestinoListDestino = em.merge(oldOrigenOfDestinoListDestino);
                }
            }
            for (Almacen almacenListAlmacen : localizacion.getAlmacenList()) {
                Localizacion oldOrigenOfAlmacenListAlmacen = almacenListAlmacen.getOrigen();
                almacenListAlmacen.setOrigen(localizacion);
                almacenListAlmacen = em.merge(almacenListAlmacen);
                if (oldOrigenOfAlmacenListAlmacen != null) {
                    oldOrigenOfAlmacenListAlmacen.getAlmacenList().remove(almacenListAlmacen);
                    oldOrigenOfAlmacenListAlmacen = em.merge(oldOrigenOfAlmacenListAlmacen);
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
            List<Proveedor> proveedorListOld = persistentLocalizacion.getProveedorList();
            List<Proveedor> proveedorListNew = localizacion.getProveedorList();
            List<Destino> destinoListOld = persistentLocalizacion.getDestinoList();
            List<Destino> destinoListNew = localizacion.getDestinoList();
            List<Almacen> almacenListOld = persistentLocalizacion.getAlmacenList();
            List<Almacen> almacenListNew = localizacion.getAlmacenList();
            if (paisNew != null) {
                paisNew = em.getReference(paisNew.getClass(), paisNew.getCod());
                localizacion.setPais(paisNew);
            }
            List<Proveedor> attachedProveedorListNew = new ArrayList<Proveedor>();
            for (Proveedor proveedorListNewProveedorToAttach : proveedorListNew) {
                proveedorListNewProveedorToAttach = em.getReference(proveedorListNewProveedorToAttach.getClass(), proveedorListNewProveedorToAttach.getCod());
                attachedProveedorListNew.add(proveedorListNewProveedorToAttach);
            }
            proveedorListNew = attachedProveedorListNew;
            localizacion.setProveedorList(proveedorListNew);
            List<Destino> attachedDestinoListNew = new ArrayList<Destino>();
            for (Destino destinoListNewDestinoToAttach : destinoListNew) {
                destinoListNewDestinoToAttach = em.getReference(destinoListNewDestinoToAttach.getClass(), destinoListNewDestinoToAttach.getId());
                attachedDestinoListNew.add(destinoListNewDestinoToAttach);
            }
            destinoListNew = attachedDestinoListNew;
            localizacion.setDestinoList(destinoListNew);
            List<Almacen> attachedAlmacenListNew = new ArrayList<Almacen>();
            for (Almacen almacenListNewAlmacenToAttach : almacenListNew) {
                almacenListNewAlmacenToAttach = em.getReference(almacenListNewAlmacenToAttach.getClass(), almacenListNewAlmacenToAttach.getCod());
                attachedAlmacenListNew.add(almacenListNewAlmacenToAttach);
            }
            almacenListNew = attachedAlmacenListNew;
            localizacion.setAlmacenList(almacenListNew);
            localizacion = em.merge(localizacion);
            if (paisOld != null && !paisOld.equals(paisNew)) {
                paisOld.getLocalizacionList().remove(localizacion);
                paisOld = em.merge(paisOld);
            }
            if (paisNew != null && !paisNew.equals(paisOld)) {
                paisNew.getLocalizacionList().add(localizacion);
                paisNew = em.merge(paisNew);
            }
            for (Proveedor proveedorListOldProveedor : proveedorListOld) {
                if (!proveedorListNew.contains(proveedorListOldProveedor)) {
                    proveedorListOldProveedor.setOrigen(null);
                    proveedorListOldProveedor = em.merge(proveedorListOldProveedor);
                }
            }
            for (Proveedor proveedorListNewProveedor : proveedorListNew) {
                if (!proveedorListOld.contains(proveedorListNewProveedor)) {
                    Localizacion oldOrigenOfProveedorListNewProveedor = proveedorListNewProveedor.getOrigen();
                    proveedorListNewProveedor.setOrigen(localizacion);
                    proveedorListNewProveedor = em.merge(proveedorListNewProveedor);
                    if (oldOrigenOfProveedorListNewProveedor != null && !oldOrigenOfProveedorListNewProveedor.equals(localizacion)) {
                        oldOrigenOfProveedorListNewProveedor.getProveedorList().remove(proveedorListNewProveedor);
                        oldOrigenOfProveedorListNewProveedor = em.merge(oldOrigenOfProveedorListNewProveedor);
                    }
                }
            }
            for (Destino destinoListOldDestino : destinoListOld) {
                if (!destinoListNew.contains(destinoListOldDestino)) {
                    destinoListOldDestino.setOrigen(null);
                    destinoListOldDestino = em.merge(destinoListOldDestino);
                }
            }
            for (Destino destinoListNewDestino : destinoListNew) {
                if (!destinoListOld.contains(destinoListNewDestino)) {
                    Localizacion oldOrigenOfDestinoListNewDestino = destinoListNewDestino.getOrigen();
                    destinoListNewDestino.setOrigen(localizacion);
                    destinoListNewDestino = em.merge(destinoListNewDestino);
                    if (oldOrigenOfDestinoListNewDestino != null && !oldOrigenOfDestinoListNewDestino.equals(localizacion)) {
                        oldOrigenOfDestinoListNewDestino.getDestinoList().remove(destinoListNewDestino);
                        oldOrigenOfDestinoListNewDestino = em.merge(oldOrigenOfDestinoListNewDestino);
                    }
                }
            }
            for (Almacen almacenListOldAlmacen : almacenListOld) {
                if (!almacenListNew.contains(almacenListOldAlmacen)) {
                    almacenListOldAlmacen.setOrigen(null);
                    almacenListOldAlmacen = em.merge(almacenListOldAlmacen);
                }
            }
            for (Almacen almacenListNewAlmacen : almacenListNew) {
                if (!almacenListOld.contains(almacenListNewAlmacen)) {
                    Localizacion oldOrigenOfAlmacenListNewAlmacen = almacenListNewAlmacen.getOrigen();
                    almacenListNewAlmacen.setOrigen(localizacion);
                    almacenListNewAlmacen = em.merge(almacenListNewAlmacen);
                    if (oldOrigenOfAlmacenListNewAlmacen != null && !oldOrigenOfAlmacenListNewAlmacen.equals(localizacion)) {
                        oldOrigenOfAlmacenListNewAlmacen.getAlmacenList().remove(almacenListNewAlmacen);
                        oldOrigenOfAlmacenListNewAlmacen = em.merge(oldOrigenOfAlmacenListNewAlmacen);
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
            List<Proveedor> proveedorList = localizacion.getProveedorList();
            for (Proveedor proveedorListProveedor : proveedorList) {
                proveedorListProveedor.setOrigen(null);
                proveedorListProveedor = em.merge(proveedorListProveedor);
            }
            List<Destino> destinoList = localizacion.getDestinoList();
            for (Destino destinoListDestino : destinoList) {
                destinoListDestino.setOrigen(null);
                destinoListDestino = em.merge(destinoListDestino);
            }
            List<Almacen> almacenList = localizacion.getAlmacenList();
            for (Almacen almacenListAlmacen : almacenList) {
                almacenListAlmacen.setOrigen(null);
                almacenListAlmacen = em.merge(almacenListAlmacen);
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
