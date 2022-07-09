package id.co.promise.procurement.requestquotation;

import id.co.promise.procurement.entity.RequestQuotation;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class RequestQuotationSession extends AbstractFacadeWithAudit<RequestQuotation> {
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public RequestQuotationSession() {
		super(RequestQuotation.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		return ema;
	}

	public RequestQuotation insert(RequestQuotation object, Token token) {
		object.setCreated(new Date());
		object.setPostDate(object.getCreated());
		object.setIsDelete(0);
		super.create(object, AuditHelper.OPERATION_CREATE, token);
		return object;
	}

	public RequestQuotation update(RequestQuotation object, Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public RequestQuotation editDelegasiPengadaan(RequestQuotation object,
			Token token) {
		object.setUpdated(new Date());
		super.edit(object, AuditHelper.OPERATION_UPDATE, token);
		return object;
	}

	public RequestQuotation delete(int id, Token token) {
		RequestQuotation x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x, AuditHelper.OPERATION_DELETE, token);
		return x;
	}

	public RequestQuotation deleteRow(int id, Token token) {
		RequestQuotation x = super.find(id);
		super.remove(x, AuditHelper.OPERATION_ROW_DELETE, token);
		return x;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getCountAllByStatus() {
		return em.createQuery("select rq.requestQuotationStatus, count(rq) from RequestQuotation rq where rq.isDelete = 0 group by rq.requestQuotationStatus").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<RequestQuotation> getPagingList(String keywordSearch, int startRecord, int pageSize, String orderKeyword) {
		String vQuery = "select rq "
				+ "from RequestQuotation rq "
				+ "where rq.isDelete = 0 ";				
		if (keywordSearch != null) {
			vQuery = vQuery + "and (rq.purchaseRequest.prnumber like :keyword or rq.noRFQ like :keyword or rq.deliveryQuoteDate like :keyword "
					+ "or rq.postDate like :keyword or rq.requestQuotationStatus like :keyword) ";
		}
		if (orderKeyword != null) {
			vQuery = vQuery + "order by rq."+ orderKeyword + " asc";
		}
		Query query = em.createQuery(vQuery);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		query.setFirstResult((startRecord - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}
	
	public Long getTotalList(String keywordSearch) {
		String vQuery = "select COUNT (rq) "
				+ "from RequestQuotation rq "
				+ "where rq.isDelete = 0 ";
		if (keywordSearch != null) {
			vQuery =  vQuery + "and (rq.purchaseRequest.prnumber like :keyword or rq.noRFQ like :keyword or rq.deliveryQuoteDate like :keyword "
					+ "or rq.postDate like :keyword or rq.requestQuotationStatus like :keyword) ";
		}
		Query query = em.createQuery(vQuery);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		return (Long) query.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RequestQuotation> getListByVendor(int vendorId, String keywordSearch, int startRecord, int pageSize, String orderKeyword) {
		String vQuery = "select rq "
				+ "from RequestQuotation rq inner join rq.requestQuotationVendorList rqv "
				+ "where rq.isDelete = 0 and rqv.vendor.user = :vendorId ";				
		if (keywordSearch != null) {
			vQuery = vQuery + "and (rq.purchaseRequest.prnumber like :keyword or rq.noRFQ like :keyword or rq.deliveryQuoteDate like :keyword "
					+ "or rq.postDate like :keyword or rq.requestQuotationStatus like :keyword) ";
		}
		if (orderKeyword != null) {
			vQuery = vQuery + "order by rq."+ orderKeyword + " asc";
		}
		Query query = em.createQuery(vQuery);
		query.setParameter("vendorId", vendorId);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		query.setFirstResult((startRecord - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}
	
	public Long getTotalListByVendor(int vendorId, String keywordSearch) {
		String vQuery = "select COUNT (rq) "
				+ "from RequestQuotation rq left join rq.requestQuotationVendorList rqv "
				+ "where rq.isDelete = 0 and rqv.vendor.id = :vendorId ";
		if (keywordSearch != null) {
			vQuery =  vQuery + "and (rq.purchaseRequest.prnumber like :keyword or rq.noRFQ like :keyword or rq.deliveryQuoteDate like :keyword "
					+ "or rq.postDate like :keyword or rq.requestQuotationStatus like :keyword) ";
		}
		Query query = em.createQuery(vQuery);
		query.setParameter("vendorId", vendorId);
		if (keywordSearch != null) {
			query.setParameter("keyword", keywordSearch);
		}
		return (Long) query.getSingleResult();
	}

}
