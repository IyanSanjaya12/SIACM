package id.co.promise.procurement.master;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

@Stateless
@LocalBean
public class HariLiburSession extends AbstractFacadeWithAudit<HariLibur> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public HariLiburSession() {
		super(HariLibur.class);
	}

	public List<HariLibur> getHariLiburList() {
		Query q = em.createNamedQuery("HariLibur.getHariLiburList");
		return q.getResultList();
	}
	
	public List<HariLibur> getHariLiburListById(Integer id) {
		Query q = em.createNamedQuery("HariLibur.getHariLiburListById");
		q.setParameter("id",id);
		return q.getResultList();
	}
	
	public List<HariLibur> getHariLiburBetWeenDates(Date date1, Date date2) {
		Query q = em.createQuery("SELECT hl FROM HariLibur hl WHERE hl.isDelete = 0 AND tanggal >= :date1 AND tanggal <= :date2");
		q.setParameter("date1", date1, TemporalType.DATE);
		q.setParameter("date2", date2, TemporalType.DATE);

		return q.getResultList();
	}

	public HariLibur getHariLiburById(int hariLiburId) {
		return super.find(hariLiburId);
	}

	public HariLibur createHariLibur(HariLibur ap, Token token) {
		ap.setCreated(new Date());
		ap.setIsDelete(0);
		super.create(ap, AuditHelper.OPERATION_CREATE, token);
		return ap;
	}

	public HariLibur editHariLibur(HariLibur ap, Token token) {
		ap.setUpdated(new Date());
		super.edit(ap, AuditHelper.OPERATION_UPDATE, token);
		return ap;
	}

	public HariLibur deleteHariLibur(int id, Token token) {
		HariLibur bu = super.find(id);
		bu.setIsDelete(1);
		bu.setDeleted(new Date());
		super.edit(bu, AuditHelper.OPERATION_DELETE, token);
		return bu;
	}
	
	@SuppressWarnings("unchecked")
	public Boolean checkNamaHariLibur(String nama, String toDo, Integer hariLiburId) {
		Query q = em.createNamedQuery("HariLibur.findNama");
		q.setParameter("nama", nama);
		List<HariLibur> hariLiburList = q.getResultList();

		Boolean isSave = false;
		if (toDo.equalsIgnoreCase("insert")) {
			if (hariLiburList != null && hariLiburList.size() > 0) {
				isSave = false;
			} else {
				isSave = true;
			}

		} else if (toDo.equalsIgnoreCase("update")) {
			if (hariLiburList != null && hariLiburList.size() > 0) {
				if (hariLiburId.equals(hariLiburList.get(0).getId())) {
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
	
	public HariLibur insertHariLibur(HariLibur hariLibur, Token token){
		hariLibur.setCreated(new Date());
		hariLibur.setIsDelete(0);
		super.create(hariLibur, AuditHelper.OPERATION_CREATE, token);
		return hariLibur;
	}
	
	public HariLibur updateHariLibur(HariLibur hariLibur, Token token) {
		super.edit(hariLibur, AuditHelper.OPERATION_UPDATE, token);
		return hariLibur;
	}


	public List<HariLibur> getHariLiburByDate(Date tanggal) {
		Query q = em.createNamedQuery("HariLibur.findByDate");
		q.setParameter("tanggal", tanggal);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public HariLibur getHariLiburByNama(String nama) {
		Query q = em.createQuery("SELECT hariLibur FROM HariLibur hariLibur WHERE hariLibur.isDelete = 0 AND hariLibur.nama = :nama");
		q.setParameter("nama", nama);
		
		List<HariLibur> hariLibur = q.getResultList();
		if (hariLibur != null && hariLibur.size() > 0) {
			return hariLibur.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<HariLibur> getHariLiburListWithPagination(Integer start, Integer length, String keyword, Integer columnOrder, String tipeOrder) {

		String queryUser = "SELECT hariLibur FROM HariLibur hariLibur WHERE hariLibur.isDelete =:isDelete AND "
				+ "(hariLibur.nama like :keyword OR hariLibur.tanggal like :keyword OR hariLibur.deskripsi like :keyword) ";

		String[] columnToView = { "id", "nama", "tanggal", "deskripsi"};
		if (columnOrder > 0) {
			queryUser+="ORDER BY hariLibur. "+columnToView[columnOrder] + " " + tipeOrder;
		} else {
			queryUser+="ORDER BY hariLibur.id asc ";
		}

		Query query = em.createQuery(queryUser);
		query.setParameter("isDelete", 0);
		query.setParameter("keyword", keyword);
		query.setFirstResult(start);
		query.setMaxResults(length);
		List<HariLibur> hariLiburList = query.getResultList();
		return hariLiburList;
	}

	public long getHariLiburListCount(String keyword) {
		String queryCountUser = "SELECT COUNT(hariLibur) FROM HariLibur hariLibur WHERE hariLibur.isDelete = :isDelete "
				+ "AND (hariLibur.nama like :keyword OR hariLibur.tanggal like :keyword OR hariLibur.deskripsi like :keyword)";

		Query query = em.createQuery(queryCountUser);
		query.setParameter("keyword", keyword);
		query.setParameter("isDelete", 0);
		Object resultQuery = query.getSingleResult();
		if (resultQuery != null) {
			return (Long) resultQuery;
		}
		return 0;
	}
	
	public Date getTotalWorkingDays(Integer countDate) throws ParseException {
		String pattern = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String endDateStr = getWorkingDaysDate(new Date(),countDate);
		Date endDate = simpleDateFormat.parse(endDateStr);
		Date startDate = new Date();
		List<HariLibur>hariLiburList = getHariLiburBetWeenDates(startDate,endDate);
		while(hariLiburList.size() != 0) {
			startDate = endDate;
			endDate = simpleDateFormat.parse(getWorkingDaysDate(startDate,hariLiburList.size()));
			hariLiburList = getHariLiburBetWeenDates(startDate,endDate);
		}
		return endDate;
	  }
	
	public Date getWorkingDays(Date startDate, Integer countDate) throws ParseException {
		String pattern = "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String endDateStr = getWorkingDaysDate(startDate,countDate);
		Date endDate = simpleDateFormat.parse(endDateStr);
		if (startDate == null) {
		 startDate = new Date();
		}
		List<HariLibur>hariLiburList = getHariLiburBetWeenDates(startDate,endDate);
		while(hariLiburList.size() != 0) {
			startDate = endDate;
			endDate = simpleDateFormat.parse(getWorkingDaysDate(startDate,hariLiburList.size()));
			hariLiburList = getHariLiburBetWeenDates(startDate,endDate);
		}
		return endDate;
	  }
	
	public String getWorkingDaysDate (Date startDate,Integer workingDays) {
		 	Date date=startDate;
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    SimpleDateFormat s;
		    s=new SimpleDateFormat("MM/dd/yy");
		    Integer days = workingDays;
		    for(int i=0;i<days;)
		    {
		        calendar.add(Calendar.DAY_OF_MONTH, 1);
		        if(calendar.get(Calendar.DAY_OF_WEEK)<=5)
		        {
		            i++;
		        }

		    }
		    date=calendar.getTime(); 
		    s=new SimpleDateFormat("dd/MM/yyyy");
		    return s.format(date);
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


}
