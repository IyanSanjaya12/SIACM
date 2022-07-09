package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;

import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class JabatanSession extends AbstractFacadeWithAudit<Jabatan> {
	final static Logger log = Logger.getLogger(JabatanSession.class);
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public JabatanSession() {
		super(Jabatan.class);
	}

	public Jabatan getJabatan(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<Jabatan> getJabatanList() {
		Query q = em.createNamedQuery("Jabatan.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Jabatan> getByOrganisasiList(List<Organisasi> organisasiList) {
		Query q = em.createNamedQuery("Jabatan.getByOrganisasiIdList");
		q.setParameter("organisasiList", organisasiList);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getJabatanAndOrganisasiByOrganisasiIdList(List<Organisasi> organisasiList) {
		Query q = em.createNamedQuery("Jabatan.getJabatanAndOrganisasiByOrganisasiIdList");
		q.setParameter("organisasiList", organisasiList);
		q.setParameter("appCode", "PM");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getJabatanAndOrganisasiByOrganisasiIdListAndAdditional(List<Organisasi> organisasiList, Organisasi organisasi) {
		Query q = em.createNamedQuery("Jabatan.getJabatanAndOrganisasiByOrganisasiIdListAndAdditional");
		q.setParameter("organisasiList", organisasiList);
		q.setParameter("organisasi", organisasi);
		q.setParameter("appCode", "PM");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getAdditionalApprovalList( Organisasi organisasi) {
		Query q = em.createNamedQuery("Jabatan.getAdditionalApproval");
		q.setParameter("organisasi", organisasi);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Jabatan> getNotRegisteredList(Integer organisasiId) {
		Query q = em.createNamedQuery("Jabatan.getNotRegisteredList");
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Boolean checkNamaJabatan(String nama, String toDo, Integer JabatanId) {
		Query q = em.createNamedQuery("Jabatan.findNama");
		q.setParameter("nama", nama);
		List<Jabatan> jabatanList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (jabatanList != null && jabatanList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (jabatanList != null && jabatanList.size() > 0) {
				if (JabatanId.equals(jabatanList.get(0).getId())) {
					isSave = true;
				} else {
					isSave = false;
				}
			} else {
				isSave = true;
			}
		}

		return isSave;

	}

	@SuppressWarnings("unchecked")
	public Jabatan getJabatanByNama(String namaJabatan) {
		Query q = em
				.createQuery("select jabatan from Jabatan jabatan where jabatan.nama = :namaJabatan and jabatan.isDelete = 0");
		q.setParameter("namaJabatan", namaJabatan);
		List<Jabatan> jabatanList = q.getResultList();
		if (jabatanList != null && jabatanList.size() > 0) {
			return jabatanList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Jabatan getJabatanByJabatanIdEoffice(Integer jabatanIdEoffice) {
		Query q = em.createNamedQuery("Jabatan.getJabatanByJabatanIdEoffice");
		q.setParameter("jabatanIdEoffice", jabatanIdEoffice);
		List<Jabatan> jabatanList = q.getResultList();
		Jabatan jabatan = null;
		try{
			jabatan = jabatanList.get(0);
		}catch (Exception e){
			log.error("Error : "+e);
		}
		 return jabatan ;
	}

	@SuppressWarnings("unchecked")
	public List<Jabatan> getJabatanListByOrganisasi(Integer organisasiId) {
		Query q = em.createNamedQuery("Jabatan.getJabatanListByOrganisasi");
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}
	
	public Jabatan insertJabatan(Jabatan jabatan, Token token) {
		jabatan.setCreated(new Date());
		jabatan.setIsDelete(0);
		super.create(jabatan, AuditHelper.OPERATION_CREATE, token);
		return jabatan;
	}

	public Jabatan updateJabatan(Jabatan jabatan, Token token) {
		jabatan.setUpdated(new Date());
		super.edit(jabatan, AuditHelper.OPERATION_UPDATE, token);
		return jabatan;
	}

	public Jabatan deleteJabatan(int id, Token token) {
		Jabatan jabatan = super.find(id);
		jabatan.setIsDelete(1);
		jabatan.setDeleted(new Date());

		super.edit(jabatan, AuditHelper.OPERATION_DELETE, token);
		return jabatan;
	}

	public Jabatan deleteRowJabatan(int id, Token token) {
		Jabatan jabatan = super.find(id);
		super.remove(jabatan, AuditHelper.OPERATION_ROW_DELETE, token);
		return jabatan;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
}
