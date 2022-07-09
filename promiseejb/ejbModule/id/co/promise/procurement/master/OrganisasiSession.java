package id.co.promise.procurement.master;

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

import org.jboss.logging.Logger;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class OrganisasiSession extends AbstractFacadeWithAudit<Organisasi> {
	final static Logger log = Logger.getLogger(OrganisasiSession.class);
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private RoleUserSession roleUserSession;
	
	@EJB
	private OrganisasiSession organisasiSession;
	
	public OrganisasiSession() {
		super(Organisasi.class);
	}
	

	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiAll() {
		Query q = em.createNamedQuery("Organisasi.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Organisasi getByNama(String nama) {
		Query q = em.createNamedQuery("Organisasi.getByNama");
		q.setParameter("nama", nama);
		List<Organisasi> organisasiList = q.getResultList();
		Organisasi organisasi = null;
		try{
			organisasi = organisasiList.get(0);
		}catch (Exception e){
			log.error("error :"+ e);
		}
		 return organisasi ;
	}
	
	@SuppressWarnings("unchecked")
	public Organisasi getByUnitIdEoffice(Integer unitIdEoffice) {
		Query q = em.createNamedQuery("Organisasi.getByUnitIdEoffice");
		q.setParameter("unitIdEoffice", unitIdEoffice);
		List<Organisasi> organisasiList = q.getResultList();
		Organisasi organisasi = null;
		try{
			organisasi = organisasiList.get(0);
		}catch (Exception e){
			log.error("error :"+ e);
		}
		 return organisasi ;
	}

	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiListByParentId(Integer parentId) {
		Query q = em.createNamedQuery("Organisasi.findByParentId")
				.setParameter("parentId", parentId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiParentList() {
		String query = "SELECT organisasi FROM Organisasi organisasi where (organisasi.parentId in (SELECT organisasi from organisasi where (organisasi.parentId is null))) AND organisasi.isDelete = 0  ";
		Query q = getEntityManager().createQuery(query); 
		return q.getResultList();
	}
	
	public List<Organisasi> getAllChildListByOrganisasi(Integer organisasiId) {
		Boolean lanjut = true;
		List<Organisasi> allChildList = new ArrayList<Organisasi>();
		List<Organisasi> organisasiList= getOrganisasiListByParentId(organisasiId);
		List<Organisasi> tempList = new ArrayList<Organisasi>();
		List<Organisasi> childList = new ArrayList<Organisasi>();
		List<Organisasi> childTempList = new ArrayList<Organisasi>();
		if(organisasiList.size() == 0){
			lanjut = false;
		}else{
			allChildList = new ArrayList<Organisasi>(organisasiList);
		}
		while(lanjut){
			tempList = new ArrayList<Organisasi>(organisasiList);
			lanjut = false;
			for(Organisasi org : tempList){
				childList = new ArrayList<Organisasi>(getOrganisasiListByParentId(org.getId()));
				if(childList.size() > 0){
					lanjut = true;
					childTempList.addAll(childList);
					allChildList.addAll(childList);
				}
				else {
					lanjut = false;
				}
			}
			organisasiList = childTempList;
		}
		
		
		return allChildList;
	}
	
	public List<Organisasi> getAllParentListByChild(Integer childId) {
		Boolean lanjut = true;
		List<Organisasi> allParentList = new ArrayList<Organisasi>();
		Organisasi tempOrganisasi = new Organisasi();
		Organisasi parentOrganisasi = new Organisasi();
		Organisasi organisasi = getOrganisasi(childId);
		allParentList.add(organisasi);
		if (organisasi.getParentId()!=null) {
			tempOrganisasi = organisasi;
		}
		else {
			lanjut = false;
		}
		while(lanjut) {
			lanjut = false;
			if(tempOrganisasi.getParentId()!=null) {
				lanjut = true;
				parentOrganisasi = getOrganisasi(tempOrganisasi.getParentId());
				allParentList.add(parentOrganisasi);
				tempOrganisasi = parentOrganisasi;
			}
		}
		return allParentList;
	}
	
	public List<Organisasi> getParentListByChild(Integer childId) {
		List<Organisasi> allParentList = new ArrayList<Organisasi>();
		Organisasi organisasiChild = find(childId);
		Integer parentId = organisasiChild.getParentId();
		allParentList.add(organisasiChild);
		while (parentId != null) {
			Organisasi organisasi = new Organisasi();
			organisasi = find(parentId) ;
			parentId = organisasi.getParentId();
			allParentList.add(organisasi);
		}	
		return allParentList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getSelfAndChildByParentId(int parentId) {
		Query q = em.createNamedQuery("Organisasi.findSelfAndChildByParentId")
				.setParameter("parentId", parentId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiListAfco(){
		Query q = em.createNamedQuery("Organisasi.findParentAfco");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiLevel2List(){
		Query q = em.createNamedQuery("Organisasi.findOrganisasiLevel2List");
		return q.getResultList();
	}
	
	

	public Organisasi getOrganisasi(int id) {
		return super.find(id);
	}
	
	public Organisasi getOrganisasiByToken(Token token){
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(token.getUser().getId());
		RoleUser roleUser=roleUserList.get(0);
		
		return roleUser.getOrganisasi();
	}
	
	public Organisasi getAfcoOrganisasiByUserId(Integer id){
		List<RoleUser> roleUserList = roleUserSession.getRoleUserByUserId(id);
		RoleUser roleUser=null;
		
		try{
			roleUser=roleUserList.get(0);
		}catch(Exception e){
			log.error("roleUser tidak ada ");
			return null;
		}
		
		Organisasi organisasi = roleUser.getOrganisasi();
		Organisasi afco = null;
		Integer tampParentId = organisasi.getParentId()==null?0 : organisasi.getParentId();
		if(tampParentId.equals(1) || organisasi.getId() == 1){			
			afco = organisasi;			
		}else{
			if(organisasi.getParentId() != null) {
				afco=organisasiSession.find(organisasi.getParentId());				
			}
		}
		
		
		
		return afco;
	}
	
	public Organisasi getAfcoOrganisasiByOrganisasiId(Integer organisasiId){
		
		Organisasi organisasi = organisasiSession.find(organisasiId);
		Organisasi afco = null;
		Boolean lanjut= false;
		
		do{
		if(organisasi.getParentId()==null){			
			afco = organisasi;
			lanjut = true;
		}else{
			organisasi=organisasiSession.find(organisasi.getParentId());
		}
		}while(!lanjut);
		
		return afco;
	}
	
	public Organisasi getAfcoOrganisasiByParentId(Integer organisasiId){
			
		Organisasi organisasi = organisasiSession.find(organisasiId);
		
		Organisasi organisasiLevel = organisasiSession.find(organisasi.getParentId());
		
		if(organisasiLevel.getParentId() != null) {
			organisasi=organisasiSession.find(organisasi.getParentId());
		}
		
		return organisasi;
	}

	public Boolean hasChildren(List<Organisasi> listOrg, int orgId) {
		for (Organisasi org : listOrg) {
			if (org.getParentId() == orgId)
				return true;
		}
		return false;
	}

	public String buildOrganisasiStruktur(List<Organisasi> listOrg, int parentId) {
		String str = "[";
		int index = 0;
		for (Organisasi org : listOrg) {
			if (org.getIsDelete() == 0) {
				if (org.getParentId() == parentId) {
					if (index > 0) {
						str += ",";
					}
					str += "{ \"id\" : " + org.getId() + ", \"label\" : \""
							+ org.getNama() + "\", \"created\" : \""
							+ org.getCreated() + "\", \"parentId\" : \""
							+ org.getParentId() + "\"";
					if (hasChildren(listOrg, org.getId())) {
						str += ", \"children\" : ";
						str += buildOrganisasiStruktur(listOrg, org.getId());
					}
					str += "}";
					index++;
				}
			}
		}
		str += "]";
		return str;
	}

	public String getOrganisasiAllJSON() {
		String str = "{ \"data\" : ";
		str += buildOrganisasiStruktur(findAll(), 0);
		str += "}";
		return str;
	}

	public String findOrganisasiStruktur(List<Organisasi> listOrg,
			int parentId, int id) {
		String str = "[";
		int index = 0;
		for (Organisasi org : listOrg) {
			if (org.getIsDelete() == 0) {
				if (org.getParentId() == parentId && org.getId() == id) {
					if (index > 0) {
						str += ",";
					}
					str += "{ \"id\" : " + org.getId() + ", \"label\" : \""
							+ org.getNama() + "\", \"parentId\" : \""
							+ org.getParentId() + "\"";
					if (hasChildren(listOrg, org.getId())) {
						str += ", \"children\" : ";
						str += buildOrganisasiStruktur(listOrg, org.getId());
					}
					str += "}";
					index++;
				}
			}
		}
		str += "]";
		return str;
	}

	public String findOrganisasiAllJSON(int parentId, int id) {
		String str = findOrganisasiStruktur(findAll(), parentId, id);
		return str;
	}

	public Organisasi createOrganisasi(Organisasi org, Token token) {
		org.setCreated(new Date());
		org.setIsDelete(0);
		super.create(org, AuditHelper.OPERATION_CREATE, token);
		return org;
	}

	public Organisasi editOrganisasi(Organisasi org, Token token) {
		org.setUpdated(new Date());
		super.edit(org, AuditHelper.OPERATION_UPDATE, token);
		return org;
	}

	public Organisasi delete(int organisasiId, Token token) {
		Organisasi org = super.find(organisasiId);
		org.setIsDelete(1);
		super.edit(org, AuditHelper.OPERATION_DELETE, token);
		return org;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiByNamePartial(String name) {
		Query q = em.createNamedQuery("Organisasi.findByNamePartial");
		q.setParameter("name", "%"+name+"%");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiByName(String name) {
		Query q = em.createNamedQuery("Organisasi.findByNamePartial");
		q.setParameter("name",name);
		return q.getResultList();
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}

	/**
	* Insert element at front of the sequence.
	* @param pElement the element to add 
	* @return the element is inserted as the first element of
	* the sequence
	* @exception java.lang.NullPointerException
	* if element is detected null
	*/
	@SuppressWarnings("unchecked")
	public List<Organisasi> findByName(String name) {
		Query q = em.createNamedQuery("Organisasi.findByNamePartial");
		if(name.equalsIgnoreCase("*"))
			q.setParameter("name", "%");		
		else
			q.setParameter("name", "%"+name.toUpperCase()+"%");			
				
		q.setMaxResults(10);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getListOrganisasiByToken(Token token) {
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
				if (organisasiParentL1.getParentId() != null && organisasiParentL1.getParentId() != 1) {
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

		Query q = em.createNamedQuery("Organisasi.findListByOrganisasiId");
		q.setParameter("organisasiIdList", organisasiIDNewList);
		return q.getResultList();
	}
	
	public Organisasi getRootParentByOrganisasi(Organisasi organisasi) {
		while(organisasi.getParentId() != null) {
			organisasi = find(organisasi.getParentId());
		}
		return organisasi ;
	}
	
	public Organisasi getRootParentByOrganisasi(Integer organisasiId) {
		Organisasi organisasi = find(organisasiId);
		while(organisasi.getParentId() != null) {
			organisasi = find(organisasi.getParentId());
		}
		return organisasi ;
	}


	@SuppressWarnings("unchecked")
	public List<Organisasi> getListNotSelect(List<Integer> selectList) {
		Query q = em.createNamedQuery("Organisasi.getListNotSelect");
		q.setParameter("selectList", selectList);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Organisasi> getOrganisasiListParentIdIsNull() {
		Query query = em.createNamedQuery("Organisasi.getOrganisasiListParentIdIsNull");
		return query.getResultList();
	}
	
}
