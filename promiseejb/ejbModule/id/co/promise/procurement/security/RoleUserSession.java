package id.co.promise.procurement.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Role;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.Constant;

@Stateless
@LocalBean
public class RoleUserSession extends AbstractFacade<RoleUser> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@EJB
	OrganisasiSession organisasiSession;
	@EJB
	TokenSession tokenSession;

	public RoleUser getRoleUserId(int id) {
		return super.find(id);
	}
	
	public RoleUserSession() {
		super(RoleUser.class);
	}
	public List<RoleUser> getList() {
		Query q = em.createNamedQuery("RoleUser.find");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByUserId(Integer userId) {
		Query q = em.createNamedQuery("RoleUser.findByUserId");
		q.setParameter("userId", userId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getListByJabatanId(Integer jabatanId) {
		Query q = em.createNamedQuery("RoleUser.findByJabatanId");
		q.setParameter("jabatanId", jabatanId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getUserByJabatanAndOrganisasi(Integer jabatanId, Integer organisasiId) {
		Query q = em.createNamedQuery("RoleUser.findByJabatanIdAndOrganisasiId");
		q.setParameter("jabatanId", jabatanId);
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getUserAndOrganisasi(Integer userId, Integer organisasiId) {
		Query q = em.createNamedQuery("RoleUser.findByUserIdAndOrganisasiId");
		q.setParameter("userId", userId);
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public RoleUser getByUserId(Integer userId) {
		Query q = em.createNamedQuery("RoleUser.findByUserId");
		q.setParameter("userId", userId);
		List<RoleUser> listRoleUser = q.getResultList();
		RoleUser roleUser = null;
		if (listRoleUser.size() > 0) {
			roleUser = listRoleUser.get(0);
		}
		return roleUser;
	}
	
	@SuppressWarnings("unchecked")
	public RoleUser getByToken(Token token) {
		Query q = em.createNamedQuery("RoleUser.findByUserIdAndRoleAppCode");
		q.setParameter("userId", token.getUser().getId());
		q.setParameter("roleAppCode", "PM");
		List<RoleUser> roleUserList = q.getResultList();
		try {
			return roleUserList.get(0);	
		}catch(Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserList() {
		Query q = em.createNamedQuery("RoleUser.findByRoleId");
		return q.getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByRoleIdAndnameList(int roleId, String name) {
		Query q = em.createNamedQuery("RoleUser.findByRoleIdAndName")
				.setParameter("roleId", roleId)
				.setParameter("name", "%" + name + "%");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserListByToken(String token) {
		Query q = em.createNamedQuery("RoleUser.findByToken");
		q.setParameter("token", token);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<RoleUser> findByNamaNotVendor(String name){
		Query q = em.createNamedQuery("RoleUser.findByNameNotVendor");
		q.setParameter("name", "%"+name+"%");
		q.setMaxResults(10);
		List<RoleUser> roleUserList = q.getResultList();
		for(int i=0; i < roleUserList.size();i++){
			User newUser = new User();
			newUser.setNamaPengguna(roleUserList.get(i).getUser().getNamaPengguna());
			newUser.setId(roleUserList.get(i).getUser().getId());
			newUser.setEmail(roleUserList.get(i).getUser().getEmail());
			
			RoleUser roleUserNew = new RoleUser();
			roleUserNew = roleUserList.get(i);
			roleUserNew.setUser(newUser);
			roleUserList.set(i, roleUserNew);
		}
		return q.getResultList();
	}
	
	public RoleUser insertRoleUser(RoleUser roleUser) {
		roleUser.setCreated(new Date());
		roleUser.setIsDelete(0);
		super.create(roleUser);
		return roleUser;
	}

	public RoleUser UpdateRoleUser(RoleUser roleUser) {
		roleUser.setUpdated(new Date());
		super.edit(roleUser);
		return roleUser;
	}
	
	public RoleUser deleteRoleUser(int id) {
		RoleUser roleUser = super.find(id);
		roleUser.setDeleted(new Date());
		roleUser.setIsDelete(1);
		super.edit(roleUser);
		return roleUser;
	}

	public RoleUser deleteRow(int id) {
		RoleUser roleUser = super.find(id);
		super.remove(roleUser);
		return roleUser;
	}

	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByNamaPenggunaPartially(String namaPengguna) {
		Query q = em.createNamedQuery("RoleUser.findByUserNamePartial");
		q.setParameter("username", "%" + namaPengguna + "%");
		return q.getResultList();

	}

	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserListByTokenAnAppCode(String token,
			String appCode) {
		Query q = em.createNamedQuery("RoleUser.findByTokenAndAppCode");
		q.setParameter("token", token);
		q.setParameter("appCode", appCode);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByUserIdAndAppCode(Integer userId,
			String roleAppCode) {
		Query q = em.createNamedQuery("RoleUser.findByUserIdAndRoleAppCode");
		q.setParameter("userId", userId);
		q.setParameter("roleAppCode", roleAppCode);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByOrganisasiAndAppCode(Integer orgId,
			String roleAppCode) {
		Query q = em.createNamedQuery("RoleUser.findByOrganisasiAndRoleAppCode");
		q.setParameter("orgId", orgId);
		q.setParameter("roleAppCode", roleAppCode);
		return q.getResultList();
	}
	public void deletebyUserId(int userId) {
		Query q = em.createNamedQuery("RoleUser.deletebyUserId");
		q.setParameter("userId", userId);
		q.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByAfcoOrganisasi(String name, Token token) {
		
		Integer organisasiId =  organisasiSession.getAfcoOrganisasiByUserId(token.getUser().getId()).getId();
		
		Query q = em.createQuery("SELECT roleUser FROM RoleUser roleUser WHERE roleUser.isDelete = 0 "
				+ "AND roleUser.organisasi.id = :organisasiId AND roleUser.user.namaPengguna LIKE :name AND roleUser.user.isDelete = 0");
		q.setParameter("organisasiId", organisasiId);
		q.setParameter("name", "%"+name+"%");
		q.setMaxResults(10);
		
		List<RoleUser> roleUserList = q.getResultList();
		for(int i=0; i < roleUserList.size();i++){
			User newUser = new User();
			newUser.setNamaPengguna(roleUserList.get(i).getUser().getNamaPengguna());
			newUser.setId(roleUserList.get(i).getUser().getId());
			newUser.setEmail(roleUserList.get(i).getUser().getEmail());
			
			RoleUser roleUserNew = new RoleUser();
			roleUserNew = roleUserList.get(i);
			roleUserNew.setUser(newUser);
			roleUserList.set(i, roleUserNew);
		}
		return q.getResultList();
	}
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<Role> getRoleListByOrganisasi(Organisasi organisasi) {
		Query q = em.createNamedQuery("RoleUser.findRoleByOrganisasi");
		q.setParameter("organisasi", organisasi);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByRoleIdAndOrganisasiId(Integer roleId, Integer organisasiId) {
		Query q = em.createNamedQuery("RoleUser.findRoleUserByRoleAndOrganisasi");
		q.setParameter("roleId", roleId);
		q.setParameter("organisasiId", organisasiId);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getRoleUserByParentUserIdAndRoleId(Integer parentUserId, List<Integer> roleIdList) {
		Query q = em.createNamedQuery("RoleUser.findByParentUserIdAndRoleId");
		q.setParameter("parentUserId", parentUserId);
		q.setParameter("roleIdList", roleIdList);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getUserListPagination (Integer pageNo, Integer pageSize, String nama, Integer organisasiId) {
		String query = "SELECT roleUser.user FROM RoleUser roleUser "
				+ "WHERE roleUser.organisasi.id IN(:organisasiList) AND roleUser.isDelete = 0 AND roleUser.user.isDelete = 0 "
				+ "AND roleUser.role.appCode =:appCode ";
		
		nama = nama == null ? "" : nama.trim();
		String appCode = Constant.ROLE_APPCODE_PM;
		String namaStr = nama.toUpperCase();
		if (!nama.equalsIgnoreCase("")) {
			query = query + "AND (roleUser.user.namaPengguna like :nama) ";
		}
		else {
			query = query + "order by roleUser.user.id ASC ";
		}
			
		Query q  = em.createQuery(query);
		
		if (!nama.equalsIgnoreCase("")) {
			q.setParameter("nama", "%" + namaStr + "%");
		}
		if(organisasiId != null) {
			List<Integer> organisasiIdList = new ArrayList<Integer>();
			List<Organisasi> organisasiList = organisasiSession.getAllChildListByOrganisasi(organisasiId);
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}
			organisasiIdList.add(organisasiId);
			q.setParameter("organisasiList", organisasiIdList);
		}
		q.setParameter("appCode", appCode);
		q.setFirstResult((pageNo - 1) * pageSize);
		q.setMaxResults(pageSize);
		 
		List<RoleUser> userList = q.getResultList();
	    
		return userList;
	}

	public Long getuserTotalList (String nama, Integer organisasiId) {
		String query = "SELECT count(roleUser.user) FROM RoleUser roleUser "
				+ "WHERE roleUser.organisasi.id IN(:organisasiList) AND roleUser.isDelete = 0 AND roleUser.user.isDelete = 0 ";
		
		nama = nama == null ? "" : nama.trim();
		String namaStr = nama.toUpperCase();
		if (!nama.equalsIgnoreCase("")) {
			query = query + "AND (roleUser.user.namaPengguna like :nama) ";
		}
		else {
			query = query + "order by roleUser.user.namaPengguna ASC ";
		}
			
		Query q  = em.createQuery(query);
		
		if (!nama.equalsIgnoreCase("")) {
			q.setParameter("nama", "%" + namaStr + "%");
		}
		if(organisasiId != null) {
			List<Integer> organisasiIdList = new ArrayList<Integer>();
			List<Organisasi> organisasiList = organisasiSession.getAllChildListByOrganisasi(organisasiId);
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}
			organisasiIdList.add(organisasiId);
			q.setParameter("organisasiList", organisasiIdList);
		}
		 
		return (Long) q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getSvpByOrganisasi (Integer svpId, Integer organisasiId) {
		String query = "SELECT roleUser FROM RoleUser roleUser "
				+ "WHERE roleUser.organisasi.id IN(:organisasiList) AND roleUser.isDelete = 0 AND roleUser.user.isDelete = 0 "
				+ "AND roleUser.role.id =:svpId AND roleUser.role.appCode =:appCode ";
		
		String appCode = Constant.ROLE_APPCODE_PM;
			
		Query q  = em.createQuery(query);
		
		if(organisasiId != null) {
			List<Integer> organisasiIdList = new ArrayList<Integer>();
			List<Organisasi> organisasiList = organisasiSession.getAllChildListByOrganisasi(organisasiId);
			for(Organisasi organisasi : organisasiList) {
				organisasiIdList.add(organisasi.getId());
			}
			q.setParameter("organisasiList", organisasiIdList);
		}
		q.setParameter("appCode", appCode);
		q.setParameter("svpId", svpId);
		 
		List<RoleUser> userList = q.getResultList();
	    
		return userList;
	}
	
	@SuppressWarnings("unchecked")
	public RoleUser getRoleUserByOrganisasiId(Integer organisasiId) {
		Query q = em.createNamedQuery("RoleUser.findRoleUserByOrganisasi");
		q.setParameter("organisasiId", organisasiId);
		List<RoleUser> roleUserList= (List<RoleUser>) q.getResultList();
		if(roleUserList.size() > 0) {
			return roleUserList.get(0);
		}else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RoleUser> getListRoleUserSvpByDvp (RoleUser roleUserDVP) {
		List<RoleUser> listRoleUserSVP = new ArrayList<RoleUser>();
		List<String> codeList = new ArrayList<String>();
		codeList.add("TPK");
		codeList.add("PTK");
		codeList.add("PJG");
		codeList.add("PLG");
		codeList.add("TBS");
		codeList.add("BKL");
		codeList.add("CBN");
		codeList.add("BTN");
		codeList.add("JBI");
		codeList.add("PBM");
		codeList.add("TGN");
		codeList.add("SKA");
		Boolean cabang = false;
		String organisasiCode = roleUserDVP.getUser().getJabatan().getOrganisasi().getCode();
		for(String orgCodeCabang : codeList) {
			if (orgCodeCabang.equalsIgnoreCase(organisasiCode)) {
				cabang = true;
			}
		}
		if (cabang) {
			listRoleUserSVP = this.getSvpByOrganisasi(Constant.ROLE_ID_PENGGUNA_SPV, roleUserDVP.getUser().getJabatan().getOrganisasi().getId());
		}
		else {
			listRoleUserSVP = this.getRoleUserByRoleIdAndOrganisasiId(Constant.ROLE_ID_PENGGUNA_SPV, roleUserDVP.getOrganisasi().getId());
		}
		return listRoleUserSVP;
	}
}
