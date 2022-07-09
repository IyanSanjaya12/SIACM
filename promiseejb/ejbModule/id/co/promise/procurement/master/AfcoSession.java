/**
 * fdf
 */
package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class AfcoSession extends AbstractFacadeWithAudit<Afco> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	@EJB
	private OrganisasiSession organisasiSession;
	@EJB
	private RoleUserSession roleUserSession;

	public AfcoSession() {
		super(Afco.class);
	}

	public Afco getAfco(int id) {
		return super.find(id);
	}

	public Afco findByOrganisasiId(int organisasiId) {
		Query q = em.createNamedQuery("Afco.findByOrganisasiId");
		q.setParameter("organisasiId", organisasiId);
		List<Afco> rsAfco = q.getResultList();
		if (rsAfco.size() == 0)
			return null;
		else
			return rsAfco.get(0);

	}

	public Afco findByOrganisasiParentId(int organisasiId) {
		Organisasi organiasi = organisasiSession.find(organisasiId);
		return findByOrganisasiId(organiasi.getParentId());
	}

	@SuppressWarnings("unchecked")
	public List<Afco> getAfcoList() {
		Query q = em.createNamedQuery("Afco.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Afco> getListByCompanyName(String companyName) {
		Query q = em.createNamedQuery("Afco.findByCompanyName");
		q.setParameter("companyName", companyName);
		return q.getResultList();
	}

	public Afco insertAfco(Afco afco, Token token) {
		afco.setCreated(new Date());
		afco.setIsDelete(0);
		super.create(afco, AuditHelper.OPERATION_CREATE, token);
		return afco;
	}

	public Afco updateAfco(Afco afco, Token token) {
		afco.setUpdated(new Date());
		super.edit(afco, AuditHelper.OPERATION_UPDATE, token);
		return afco;
	}

	public Afco deleteAfco(int id, Token token) {
		Afco afco = super.find(id);
		afco.setIsDelete(1);
		afco.setDeleted(new Date());
		super.edit(afco, AuditHelper.OPERATION_DELETE, token);
		return afco;
	}

	public Afco deleteRowAfco(int id, Token token) {
		Afco afco = super.find(id);
		super.remove(afco, AuditHelper.OPERATION_ROW_DELETE, token);
		return afco;
	}

	@SuppressWarnings("unchecked")
	public List<Afco> getAfcoByCompanyName(String companyName) {
		Query q = em.createNamedQuery("Afco.findByCompanyName");
		q.setParameter("companyName", companyName);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Afco> getAfcoByOrganisasiUserId(Integer userId) {
		Query q = em.createQuery("select af from Afco af left join af.organisasi org1, "
				+ "RoleUser ru left join ru.organisasi org2 left join ru.user us "
				+ "where af.isDelete = 0 and org1.id = org2.id and us.id = :userId ");
		q.setParameter("userId", userId);
		return q.getResultList();
	}

	public Afco getAfcoByOrganisasiIdAbstract(int organisasiId) {
		List<Integer> organiasiIDList = new ArrayList<Integer>();
		organiasiIDList.add(organisasiId);
		List<Organisasi> organisasiList = organisasiSession.getOrganisasiListByParentId(organisasiId);
		for (Organisasi organisasi : organisasiList) {
			organiasiIDList.add(organisasi.getId());
			// level2 kebawah
			List<Organisasi> organiasiChild01List = organisasiSession
					.getOrganisasiListByParentId(organisasi.getParentId());
			for (Organisasi organisasi2 : organiasiChild01List) {
				organiasiIDList.add(organisasi2.getId());
			}
		}
		Organisasi organisasiParentL1 = organisasiSession.getOrganisasi(organisasiId);
		if (organisasiParentL1.getParentId() != 1) {
			// level 2
			organiasiIDList.add(organisasiParentL1.getParentId());
			Organisasi organisasiParentL2 = organisasiSession.getOrganisasi(organisasiParentL1.getParentId());
			// level 3
			if (organisasiParentL2.getParentId() != 1) {
				organiasiIDList.add(organisasiParentL2.getParentId());
			}
		}
		// grouping organisasiID
		Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
		List<Integer> organisasiIDNewList = new ArrayList<Integer>();
		int index = 0;
		for (Integer organisasiID : organiasiIDList) {
			if (!hashMap.containsValue(organisasiID)) {
				hashMap.put(index, organisasiID);
				organisasiIDNewList.add(organisasiID);
				index++;
			}
		}

		Query q = em.createNamedQuery("Afco.findListByOrganisasiId");
		q.setParameter("organisasiIdList", organisasiIDNewList);
		List<Afco> afcoList =  q.getResultList();
		if(afcoList.size()>0){
			return afcoList.get(0);
		} else {
			return null;
		}
	}

	public List<Afco> getAfcoByToken(Token token) {
		List<Integer> organiasiIDList = new ArrayList<Integer>();
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(token.getUser().getId());
		for (RoleUser roleUser : roleUserList) {
			organiasiIDList.add(roleUser.getOrganisasi().getId());
			// get child organisasi
			// level1 kebawah
			List<Organisasi> organisasiList = organisasiSession
					.getOrganisasiListByParentId(roleUser.getOrganisasi().getId());
			for (Organisasi organisasi : organisasiList) {
				organiasiIDList.add(organisasi.getId());
				// level2 kebawah
				List<Organisasi> organiasiChild01List = organisasiSession
						.getOrganisasiListByParentId(organisasi.getParentId());
				for (Organisasi organisasi2 : organiasiChild01List) {
					organiasiIDList.add(organisasi2.getId());
				}
			}

			// getParent organiasi
			// level 1 ke atas
			if (roleUser.getOrganisasi().getParentId() != 1) {
				// jika bukan top organisasi
				organiasiIDList.add(roleUser.getOrganisasi().getParentId());
				Organisasi organisasiParentL1 = organisasiSession.getOrganisasi(roleUser.getOrganisasi().getParentId());
				if (organisasiParentL1.getParentId() != 1) {
					// level 2
					organiasiIDList.add(organisasiParentL1.getParentId());
					Organisasi organisasiParentL2 = organisasiSession.getOrganisasi(organisasiParentL1.getParentId());
					// level 3
					if (organisasiParentL2.getParentId() != 1) {
						organiasiIDList.add(organisasiParentL2.getParentId());
					}
				}
			}
		}
		// grouping organisasiID
		Map<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
		List<Integer> organisasiIDNewList = new ArrayList<Integer>();
		int index = 0;
		for (Integer organisasiID : organiasiIDList) {
			if (!hashMap.containsValue(organisasiID)) {
				hashMap.put(index, organisasiID);
				organisasiIDNewList.add(organisasiID);
				index++;
			}
		}

		Query q = em.createNamedQuery("Afco.findListByOrganisasiId");
		q.setParameter("organisasiIdList", organisasiIDNewList);
		return q.getResultList();
	}

	public List<Afco> findByOrganisasiList(List<Integer> organisasiIDNewList) {
		Query q = em.createNamedQuery("Afco.findListByOrganisasiId");
		q.setParameter("organisasiIdList", organisasiIDNewList);
		return q.getResultList();
	}
}
