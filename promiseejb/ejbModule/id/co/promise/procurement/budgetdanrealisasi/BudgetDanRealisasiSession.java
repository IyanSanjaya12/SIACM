package id.co.promise.procurement.budgetdanrealisasi;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.CatalogKontrak;
import id.co.promise.procurement.entity.DeliveryReceived;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;

@Stateless
@LocalBean
public class BudgetDanRealisasiSession extends AbstractFacadeWithAudit<CatalogKontrak> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BudgetDanRealisasiSession() {
		super(CatalogKontrak.class);
	}

	@SuppressWarnings("unchecked")
	public List<DeliveryReceived> getReportBudgetDanRealisasi(String search, Integer type, Integer pageNo,
			Integer pageSize, String sortingType, Date startDate, Date endDate) {
		String query = "SELECT deliveryReceived FROM DeliveryReceived deliveryReceived where deliveryReceived.isDelete = 0 " ;
		String Obj = ""; 
		String Obj2 = ""; 
		String sortQuery = ""; 
		String searchQuery="";
		search = search == null ? "" : search.trim();
		  
		  if(type == 1 ){ 
			  Obj = "deliveryReceived.purchaseOrder.purchaseRequest.approvedDate " ; 
			  sortQuery = " Order by deliveryReceived.purchaseOrder.purchaseRequest.prnumber "+ sortingType; 
				  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search)";
			  } 
		  else if(type == 2){ 
			  Obj = "deliveryReceived.purchaseOrder.approvedDate " ;
			  sortQuery = " Order by deliveryReceived.purchaseOrder.poNumber "+ sortingType; 
			  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.poNumber like :search)";
			  }
		  else { 
			  Obj = "deliveryReceived.purchaseOrder.purchaseRequest.approvedDate " ; 
			  Obj2 = "deliveryReceived.purchaseOrder.approvedDate " ; 
			  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search " + "or deliveryReceived.purchaseOrder.poNumber like :search) "; 
			  }
		  
		  if(startDate != null && endDate != null){ 
			  
			  String filterDate1 = Obj  +" >= :startDate "; 
			  String filterDate2 = Obj +" <= :endDate "; 
			  if(!Obj2.equals("")){ 
				  query = query + " AND (" + filterDate1 + " or " + Obj2 + " >= :startDate) " + " AND (" + filterDate2 + " or " + Obj2 + " <= :endDate) "; 
				  }
			  else{ 
				  query = query+ " AND " + filterDate1 + " AND " + filterDate2 ; 
				  }
		  }
		  
		  query = query + searchQuery + sortQuery;
		 

		Query q = getEntityManager().createQuery(query);
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		q.setParameter("search", "%" + search +"%");
		q.setFirstResult((pageNo - 1) * pageSize); 
		q.setMaxResults(pageSize);
		 
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryReceived> getExcelReportBudgetDanRealisasi(String search, Integer type, String sortingType, Date startDate, Date endDate) {
		String query = "SELECT deliveryReceived FROM DeliveryReceived deliveryReceived where deliveryReceived.isDelete = 0 " ;
		String Obj = ""; 
		String Obj2 = ""; 
		String sortQuery = ""; 
		String searchQuery="";
		search = search == null ? "" : search.trim();
		  
		  if(type == 1 ){ 
			  Obj = "deliveryReceived.purchaseOrder.purchaseRequest.approvedDate " ; 
			  sortQuery = " Order by deliveryReceived.purchaseOrder.purchaseRequest.prnumber "+ sortingType; 
				  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search)";
			  } 
		  else if(type == 2){ 
			  Obj = "deliveryReceived.purchaseOrder.approvedDate " ;
			  sortQuery = " Order by deliveryReceived.purchaseOrder.poNumber "+ sortingType; 
			  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.poNumber like :search)";
			  }
		  else { 
			  Obj = "deliveryReceived.purchaseOrder.purchaseRequest.approvedDate " ; 
			  Obj2 = "deliveryReceived.purchaseOrder.approvedDate " ; 
			  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search " + "or deliveryReceived.purchaseOrder.poNumber like :search) "; 
			  }
		  
		  if(startDate != null && endDate != null){ 
			  
			  String filterDate1 = Obj  +" >= :startDate "; 
			  String filterDate2 = Obj +" <= :endDate "; 
			  if(!Obj2.equals("")){ 
				  query = query + " AND (" + filterDate1 + " or " + Obj2 + " >= :startDate) " + " AND (" + filterDate2 + " or " + Obj2 + " <= :endDate) "; 
				  }
			  else{ 
				  query = query+ " AND " + filterDate1 + " AND " + filterDate2 ; 
				  }
		  }
		  
		  query = query + searchQuery + sortQuery;
		 

		Query q = getEntityManager().createQuery(query);
		if (startDate != null && endDate != null) {
			q.setParameter("startDate", startDate);
			q.setParameter("endDate", endDate);
		}
		q.setParameter("search", "%" + search +"%");
		 
		return q.getResultList();
	}

	public Long getTotalList(Integer type, String search, String sortingType, Date startDate, Date endDate) {
		String query = "SELECT count (deliveryReceived) FROM DeliveryReceived deliveryReceived where deliveryReceived.isDelete = 0 " ;
		
		  String Obj = ""; 
		  String Obj2 = ""; 
		  String sortQuery = ""; 
		  String searchQuery="";
		  search = search == null ? "" : search.trim();
		  
		  if(type == 1 ){ 
			  Obj = "deliveryReceived.purchaseOrder.purchaseRequest.approvedDate " ; 
			  sortQuery = " Order by deliveryReceived.purchaseOrder.purchaseRequest.prnumber "+ sortingType; 
				  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search)";
			  } 
		  else if(type == 2){ 
			  Obj = "deliveryReceived.purchaseOrder.approvedDate " ;
			  sortQuery = " Order by deliveryReceived.purchaseOrder.poNumber "+ sortingType; 
			  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.poNumber like :search)";
			  }
		  else { 
			  Obj = "deliveryReceived.purchaseOrder.purchaseRequest.approvedDate " ; 
			  Obj2 = "deliveryReceived.purchaseOrder.approvedDate " ; 
			  searchQuery = searchQuery + "AND (deliveryReceived.purchaseOrder.purchaseRequest.prnumber like :search " + "or deliveryReceived.purchaseOrder.poNumber like :search) "; 
			  }
		  
		  if(startDate != null && endDate != null){ 
			  
			  String filterDate1 = Obj  +" >= :startDate "; 
			  String filterDate2 = Obj +" <= :endDate "; 
			  if(!Obj2.equals("")){ 
				  query = query + " AND (" + filterDate1 + " or " + Obj2 + " >= :startDate) " + " AND (" + filterDate2 + " or " + Obj2 + " <= :endDate) "; 
				  }
			  else{ 
				  query = query+ " AND " + filterDate1 + " AND " + filterDate2 ; 
				  }
		  }
			
			query = query + searchQuery + sortQuery;


			Query q = getEntityManager().createQuery(query);
			if (startDate != null && endDate != null) {
				q.setParameter("startDate", startDate);
				q.setParameter("endDate", endDate);
			}
			q.setParameter("search", "%" + search + "%");
		return (Long) q.getSingleResult();
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
