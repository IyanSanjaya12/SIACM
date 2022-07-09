package id.co.promise.procurement.rating;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author F.H.K
 *
 */

import id.co.promise.procurement.catalog.entity.AttributeGroup;
import id.co.promise.procurement.catalog.entity.AttributePanelGroup;
import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogAttribute;
import id.co.promise.procurement.catalog.entity.CatalogCategory;
import id.co.promise.procurement.catalog.entity.CatalogImage;
import id.co.promise.procurement.catalog.entity.CatalogLocation;
import id.co.promise.procurement.catalog.entity.Category;
import id.co.promise.procurement.catalog.entity.CategoryAttribute;
import id.co.promise.procurement.catalog.entity.OrderTypeEnum;
import id.co.promise.procurement.catalog.session.CatalogCategorySession;
import id.co.promise.procurement.catalog.session.CatalogImageSession;
import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.MataUang;
import id.co.promise.procurement.entity.Satuan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.Vendor;
import id.co.promise.procurement.master.MataUangSession;
import id.co.promise.procurement.master.SatuanSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.StaticProperties;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;

@Stateless
@LocalBean
public class RatingDeliveryReceiptSession extends AbstractFacadeWithAudit<Catalog> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private VendorSession vendorSession;
	
	@EJB
	private SatuanSession satuanSession;
	
	@EJB
	private MataUangSession mataUangSession;
	
	@EJB
	private CatalogCategorySession catalogCategorySession;
	
	@EJB
	private CatalogImageSession catalogImageSession;
	

	@EJB
	private EmailNotificationSession emailNotificationSession;

	public RatingDeliveryReceiptSession() {
		super(Catalog.class);
	}
	

	
	@SuppressWarnings({ "unchecked" })
	public List<Catalog> getListPagination(String search, String vendorName, String orderKeyword, Integer pageNo, Integer pageSize, String sort ) {
		String query = "SELECT catalogCategory, catalog FROM Catalog catalog JOIN CatalogCategory catalogCategory on catalog.id = catalogCategory.catalog "
				+ "and catalog.isDelete = 0 ";
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("")? null
				: vendorName;
		if (!search.isEmpty()) {
			query = query + "and (catalog.namaIND like :search) ";
		}
		if (vendorName != null) {
			query = query + "and catalog.vendor.nama =:vendorName ";
		}

		if (!orderKeyword.isEmpty() ) {
			query = query + "order by catalog." + orderKeyword + " " + sort;
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		  q.setFirstResult((pageNo - 1) * pageSize);
		  q.setMaxResults(pageSize);
		 
		return q.getResultList();
	}
	
	public Long getTotalList(String search, String vendorName,
			Integer pageNo, Integer pageSize) {
		String query = "SELECT count (catalog) FROM Catalog catalog JOIN CatalogCategory catalogCategory on catalog.id = catalogCategory.catalog "
				+ "and catalog.isDelete = 0 ";
		search = search == null ? "" : search.trim();
		vendorName = vendorName == null || vendorName.equals("undefined") || vendorName.equals("")? null
				: vendorName;
		if (!search.isEmpty()) {
			query = query + "and (catalog.namaIND like :search) ";
		}
		if (vendorName != null) {
			query = query + "and catalog.vendor.nama =:vendorName ";
		}

		Query q = getEntityManager().createQuery(query);
		if (!search.isEmpty()) {
			q.setParameter("search", "%" + search + "%");
		}
		if (vendorName != null) {
			q.setParameter("vendorName", vendorName);
		}
		return (Long) q.getSingleResult();
	}



	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
