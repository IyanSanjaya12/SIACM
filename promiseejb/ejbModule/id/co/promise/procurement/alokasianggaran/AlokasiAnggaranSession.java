package id.co.promise.procurement.alokasianggaran;

import id.co.promise.procurement.entity.AlokasiAnggaran;
import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.master.OrganisasiSession;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Stateless
@LocalBean
public class AlokasiAnggaranSession extends AbstractFacadeWithAudit<AlokasiAnggaran> {

	@EJB
	private RoleUserSession roleUserSession;
	@EJB
	private OrganisasiSession organisasiSession;

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	/**
	 * 
	 */
	public AlokasiAnggaranSession() {
		super(AlokasiAnggaran.class);
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getList() {
		Query q = em.createNamedQuery("AlokasiAnggaran.find");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getListByUsed() {
		Query q = em.createNamedQuery("AlokasiAnggaran.findByUsed");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> findByBiroPengadaan(int biroPengadaan) {
		Query q = em.createNamedQuery("AlokasiAnggaran.findByBiroPengadaan").setParameter("biroPengadaan",
				biroPengadaan);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getByJenisNomorTahun(Integer jenis, String nomor, Integer tahun) {
		String query = "select x from AlokasiAnggaran x where x.isDelete=0 and x.isUsed=0";
		if (jenis != null) {
			query += " and x.jenisAnggaran.id=:jenis";
		}
		if (nomor != null) {
			query += " and x.nomorDraft=:nomor";
		}
		if (tahun != null) {
			query += " and x.tahunAnggaran=:tahun";
		}
		Query q = em.createQuery(query);
		if (jenis != null) {
			q.setParameter("jenis", jenis);
		}
		if (nomor != null) {
			q.setParameter("nomor", nomor);
		}
		if (tahun != null) {
			q.setParameter("tahun", tahun);
		}
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public AlokasiAnggaranPaginationDTO getAlokasiAnggaranByPaging(String search, Integer startIndex, Integer endIndex,
			Token token) {
		User user = token.getUser();
		JsonElement jelement = new JsonParser().parse(search);
		JsonObject jobject = jelement.getAsJsonObject();
		// where
		JsonArray jarray = jobject.getAsJsonArray("filter");
		String where = "";
		for (int i = 0; i < jarray.size(); i++) {
			JsonObject joWhere = jarray.get(i).getAsJsonObject();
			String fixValue = joWhere.get("value").toString().replaceAll("\"", "");
			if (fixValue.equalsIgnoreCase("undefined"))
				fixValue = "";
			if (i > 0) {
				where += " OR o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
			} else {
				where += "o." + joWhere.get("key").toString().replaceAll("\"", "") + " like '%" + fixValue + "%'";
			}

		}
		// sorting
		String sort = " ";
		try {
			sort = " ORDER BY o." + jobject.get("sort").toString().replaceAll("\"", "");
		} catch (Exception e) {
			// No sorting
			e.printStackTrace();
		}

		// get total
		Query q = em.createQuery("SELECT COUNT(o) FROM AlokasiAnggaran o WHERE o.isDelete = 0 AND o.userId="
				+ user.getId() + " AND (" + where + ") " + sort);
		int totalData = Integer.parseInt(q.getSingleResult().toString());

		// get data
		q = em.createQuery("SELECT o FROM AlokasiAnggaran o WHERE o.isDelete = 0 AND o.userId=" + user.getId()
				+ " AND (" + where + ") " + sort);
		int[] range = { startIndex - 1, endIndex - 1 };
		q.setMaxResults(range[1] - range[0] + 1);
		q.setFirstResult(range[0]);
		List<AlokasiAnggaran> alokasiAnggaranList = q.getResultList();

		// reformat
		AlokasiAnggaranPaginationDTO alokasiAnggaranListDTO = new AlokasiAnggaranPaginationDTO(totalData, startIndex,
				endIndex, alokasiAnggaranList);
		return alokasiAnggaranListDTO;
	}

	public AlokasiAnggaran getAlokasiAnggaran(int id) {
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public AlokasiAnggaran getAlokasiAnggaranByNomorDraft(String nomorDraft) {
		Query q = em.createQuery("SELECT alokasiAnggaran FROM AlokasiAnggaran alokasiAnggaran WHERE alokasiAnggaran.isDelete = 0 AND alokasiAnggaran.nomorDraft = :nomorDraft");
		q.setParameter("nomorDraft", nomorDraft);
		
		List<AlokasiAnggaran> alokasiAnggaran = q.getResultList();
		if (alokasiAnggaran != null && alokasiAnggaran.size() > 0) {
			return alokasiAnggaran.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public AlokasiAnggaran getAlokasiAnggaranByAccountName(String accountName) {
		Query q = em.createQuery("SELECT alokasiAnggaran FROM AlokasiAnggaran alokasiAnggaran WHERE alokasiAnggaran.isDelete = 0 AND alokasiAnggaran.accountName = :accountName");
		q.setParameter("accountName", accountName);
		
		List<AlokasiAnggaran> alokasiAnggaran = q.getResultList();
		if (alokasiAnggaran != null && alokasiAnggaran.size() > 0) {
			return alokasiAnggaran.get(0);
		}
		
		return null;
	}

	public AlokasiAnggaran createAlokasiAnggaran(AlokasiAnggaran object, Token token) {
		object.setCreated(new Date());
		object.setUserId(token.getUser().getId());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public AlokasiAnggaran updateAlokasiAnggaran(AlokasiAnggaran object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public AlokasiAnggaran editAlokasiAnggaran(AlokasiAnggaran object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public AlokasiAnggaran deleteAlokasiAnggaran(int id, Token token) {
		AlokasiAnggaran x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public AlokasiAnggaran deleteRowAlokasiAnggaran(int id, Token token) {
		AlokasiAnggaran x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> findByAccountNameandByNomorDraff(String search, Token token) {

		String query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0";
		if (search.length() != 0) {
			query += "AND (x.accountName like '%" + search + "%' OR x.nomorDraft like '%" + search + "%')";
		}

		Query q = em.createQuery(query);

		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> findByAccountNameandByNomorDraffAndOrganisasi(String search, Token token) {
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
		String query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0 AND x.biroPengadaan.id IN :organisasiIDList ";

		if (search.length() != 0) {
			query += " AND (x.accountName like '%" + search + "%' OR x.nomorDraft like '%" + search + "%')";
		}

		Query q = em.createQuery(query);
		q.setParameter("organisasiIDList", organisasiIDNewList);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> findByAccountNameandByNomorDraffAndjenisAnggaran(String search, int jenisAnggaranId,
			int userId) {

		String query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0";
		if (search.length() != 0) {
			query += "AND (x.accountName like '%" + search + "%' OR x.nomorDraft like '%" + search
					+ "%') AND x.jenisAnggaran.id = :jenisAnggaranId ";
					//+"AND x.biroPengadaan.id IN (SELECT T3.organisasi.id FROM RoleUser T3 WHERE T3.user.id = :userId)";
		}

		Query q = em.createQuery(query);
		q.setParameter("jenisAnggaranId", jenisAnggaranId);
		//q.setParameter("userId", userId);

		return q.getResultList();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	/**
	 * Insert element at front of the sequence.
	 * 
	 * @param pElement
	 *            the element to add
	 * @return the element is inserted as the first element of the sequence
	 * @exception java.lang.NullPointerException
	 *                if element is detected null
	 */

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getList(int userId) {
		Query q = em.createNamedQuery("AlokasiAnggaran.findByUserId").setParameter("userId", userId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getListByUserOrganisasi(int userId) {
		Query q = em
				.createQuery(
						"SELECT T1 FROM AlokasiAnggaran T1 WHERE T1.biroPengadaan.id IN (SELECT T3.organisasi.id FROM RoleUser T3 WHERE T3.user.id = :userId) AND T1.isDelete = 0")
				.setParameter("userId", userId);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getAlokasiAnggaranListWithPagination(Integer start, Integer length, String keyword,
			Integer userId, Integer columnOrder, String tipeOrder) {

		String queryUser = "SELECT o FROM AlokasiAnggaran o WHERE o.userId=:userId AND "
				+ "(o.nomorDraft like :keyword OR o.accountName like :keyword) AND o.isDelete = :isDelete ";

		String[] columnToView = { "nomor", "accountName", "jenisAnggaran.nama", "biroPengadaan.nama", "tahunAnggaran",
				"jumlah", "bookingAnggaran", "", "sisaAnggaran" };
		if (columnOrder > 0) {
			queryUser+=" ORDER BY o. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+=" ORDER BY o.id desc ";
		}

		Query query = em.createQuery(queryUser);
		query.setParameter("userId", userId);
		query.setParameter("keyword", keyword);
		query.setParameter("isDelete", 0);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<AlokasiAnggaran> alokasiAnggaranlist = query.getResultList();
		return alokasiAnggaranlist;
	}

	public long getAlokasiAnggaranListCount(String keyword, Integer userId) {
		String queryCountUser = "SELECT COUNT(o) FROM AlokasiAnggaran o WHERE o.userId=:userId "
				+ "AND (o.nomorDraft like :keyword OR o.accountName like :keyword) AND o.isDelete = :isDelete  ";

		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		query.setParameter("userId", userId);
		query.setParameter("isDelete", 0);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> findByNomorDraft(String nomorDraft) {
		Query query = em.createNamedQuery("AlokasiAnggaran.findByNomorDraft");
		query.setParameter("nomorDraft", nomorDraft);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<AlokasiAnggaran> getListByUserOrganisasiAndJenisAnggaran(int userId, int jnsAnggaranId) {
		Query q = em
				.createQuery(
						"SELECT T1 FROM AlokasiAnggaran T1 WHERE T1.biroPengadaan.id IN (SELECT T3.organisasi.id FROM RoleUser T3 WHERE T3.user.id = :userId) AND T1.jenisAnggaran.id = :jnsAnggaranId AND T1.isDelete = 0 ")
				.setParameter("userId", userId).setParameter("jnsAnggaranId", jnsAnggaranId);
		return q.getResultList();
	}

	public List<DashboardAnggaranDTO> getAnggaranByYear(int userId) {
		Query q = em
				.createQuery(
						"SELECT T1.tahunAnggaran,SUM(T1.bookingAnggaran),SUM(T1.jumlah),SUM(T1.sisaAnggaran) FROM AlokasiAnggaran T1  WHERE T1.userId = :userId AND T1.isDelete = 0 GROUP by T1.tahunAnggaran ORDER by T1.tahunAnggaran ASC")
				.setParameter("userId", userId);
		List<DashboardAnggaranDTO> list = new ArrayList<DashboardAnggaranDTO>();
		Iterator iterator = q.getResultList().iterator();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			DashboardAnggaranDTO dashboardAnggaranDTO = new DashboardAnggaranDTO();
			dashboardAnggaranDTO.setYear((Integer) tuple[0]);
			BigDecimal bookedBudget = BigDecimal.valueOf((Double) tuple[1]);
			BigDecimal planBudget = BigDecimal.valueOf((Double) tuple[2]);
			dashboardAnggaranDTO.setBookedBudget(bookedBudget.setScale(2, RoundingMode.HALF_EVEN));
			dashboardAnggaranDTO.setPlannedBudget(planBudget.setScale(2, RoundingMode.HALF_EVEN));
			BigDecimal avlBudget = BigDecimal.valueOf((Double) tuple[2]);
			dashboardAnggaranDTO.setAvailableBudget(avlBudget.setScale(2, RoundingMode.HALF_EVEN));
			dashboardAnggaranDTO.setUsedBudget(dashboardAnggaranDTO.getPlannedBudget()
					.subtract( (dashboardAnggaranDTO.getBookedBudget()).setScale(2, RoundingMode.HALF_EVEN)
					.add((dashboardAnggaranDTO.getAvailableBudget().setScale(2, RoundingMode.HALF_EVEN)) ) ));
			list.add(dashboardAnggaranDTO);

		}
		return list;
	}
	
	public List<DashboardAnggaranDTO> getAnggaranByMonth(int userId) {
		Query q = em
				.createQuery(
						"SELECT YEAR(T1.created) as yearAnggaran, MONTH(T1.created) AS monthAnggaran, SUM(T1.bookingAnggaran),SUM(T1.jumlah),SUM(T1.sisaAnggaran) FROM AlokasiAnggaran T1 WHERE T1.userId = :userId AND T1.isDelete = 0 GROUP by YEAR(T1.created), MONTH(T1.created) ORDER BY 1 ASC,2 ASC ")
				.setParameter("userId", userId);
		
						
		List<DashboardAnggaranDTO> list = new ArrayList<DashboardAnggaranDTO>();
		Iterator iterator = q.getResultList().iterator();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			DashboardAnggaranDTO dashboardAnggaranDTO = new DashboardAnggaranDTO();
			dashboardAnggaranDTO.setYear((Integer) tuple[0]);
			dashboardAnggaranDTO.setMonth((Integer) tuple[1]);
			BigDecimal bookedBudget = BigDecimal.valueOf((Double) tuple[2]);
			BigDecimal planBudget = BigDecimal.valueOf((Double) tuple[3]);
			dashboardAnggaranDTO.setBookedBudget(bookedBudget.setScale(2));
			dashboardAnggaranDTO.setPlannedBudget(planBudget.setScale(2));
			BigDecimal avlBudget = BigDecimal.valueOf((Double) tuple[3]);
			dashboardAnggaranDTO.setAvailableBudget(avlBudget.setScale(2));
			dashboardAnggaranDTO.setUsedBudget(dashboardAnggaranDTO.getPlannedBudget()
					.subtract( (dashboardAnggaranDTO.getBookedBudget()).setScale(2)
					.add((dashboardAnggaranDTO.getAvailableBudget().setScale(2)) ) ));
			list.add(dashboardAnggaranDTO);

		}
		return list;
	}
	
	public List<DashboardAnggaranDTO> getAnggaranByWeek(int userId) {
		/*Query q = em
			.createQuery(
					"SELECT YEAR(T1.created) as yearAnggaran, MONTH(T1.created) AS monthAnggaran, SUM(T1.bookingAnggaran),SUM(T1.jumlah),SUM(T1.sisaAnggaran) FROM AlokasiAnggaran T1 WHERE T1.userId = :userId AND T1.isDelete = 0 GROUP by YEAR(T1.created), MONTH(T1.created),T1.status ORDER BY 1 ASC,2 ASC ")
			.setParameter("userId", userId);*/
					
		String query = "SELECT YEAR(alokasiAnggaran.created) AS Year, DATEPART(week, alokasiAnggaran.created ) AS Week, "
				+ "SUM (alokasiAnggaran.BOOKING_ANGGARAN) as BookingAnggaran, SUM (alokasiAnggaran.JUMLAH) as Jumlah, SUM (alokasiAnggaran.SISA_ANGGARAN) as SisaAnggaran FROM "
				+ "promise_t2_alokasi_anggaran alokasiAnggaran WHERE alokasiAnggaran.isDelete = 0 AND alokasiAnggaran.USERID = :userId GROUP BY YEAR (alokasiAnggaran.created), "
				+ "DATEPART(week, alokasiAnggaran.created) ORDER BY 1 ASC, 2 ASC";
			
		Query q  = em.createNativeQuery(query);
		q.setParameter("userId", userId);
		
		List<DashboardAnggaranDTO> list = new ArrayList<DashboardAnggaranDTO>();
		Iterator iterator = q.getResultList().iterator();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			DashboardAnggaranDTO dashboardAnggaranDTO = new DashboardAnggaranDTO();
			dashboardAnggaranDTO.setYear((Integer) tuple[0]);
			dashboardAnggaranDTO.setWeek((Integer) tuple[1]);
			BigDecimal bookedBudget = BigDecimal.valueOf((Double) tuple[2]);
			BigDecimal planBudget = BigDecimal.valueOf((Double) tuple[3]);
			dashboardAnggaranDTO.setBookedBudget(bookedBudget.setScale(2));
			dashboardAnggaranDTO.setPlannedBudget(planBudget.setScale(2));
			BigDecimal avlBudget = BigDecimal.valueOf((Double) tuple[4]);
			dashboardAnggaranDTO.setAvailableBudget(avlBudget.setScale(2));
			dashboardAnggaranDTO.setUsedBudget(dashboardAnggaranDTO.getPlannedBudget()
					.subtract( (dashboardAnggaranDTO.getBookedBudget()).setScale(2)
					.add((dashboardAnggaranDTO.getAvailableBudget().setScale(2)) ) ));
			list.add(dashboardAnggaranDTO);

		}
		return list;
	}
	
	
}
