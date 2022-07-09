/**
 * fdf
 */
package id.co.promise.procurement.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import id.co.promise.procurement.entity.AddressBook;
import id.co.promise.procurement.entity.Jabatan;
import id.co.promise.procurement.entity.PurchaseRequestItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.purchaseorder.PurchaseOrderItemDTO;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.audit.AuditHelper;

/**
 * @author User
 *
 */
@Stateless
@LocalBean
public class AddressBookSession extends AbstractFacadeWithAudit<AddressBook> {

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

	public AddressBookSession() {
		super(AddressBook.class);
	}

	public AddressBook getAddressBook(int id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<AddressBook> getAddressBookList() {
		Query query = em.createNamedQuery("AddressBook.find");
		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public List<AddressBook> getAddressBookListActive() {
		Query query = em.createNamedQuery("AddressBook.findActive");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public AddressBook getByAddressCodeEbs(String addressCodeEbs) {
		Query q = em.createNamedQuery("AddressBook.findByAddressCodeEbs");
		q.setParameter("addressCodeEbs", addressCodeEbs);
		List<AddressBook> addressBookList = q.getResultList();
		if (addressBookList != null && addressBookList.size() > 0) {
			return addressBookList.get(0);
		}
		return null;
	}

	
	@SuppressWarnings("unchecked")
	public List<AddressBook> getAddressBookPaggingListByOrganisasi(String keyword, Integer firstResult, Integer maxResult, Integer fieldOrder, String fieldOrderType) {
		String vQuery = "select ab from AddressBook ab where ab.isDelete = 0 ";
//		if (organisasiId != null) {
//			vQuery += " and ab.organisasi.id = :organisasiId ";
//		}
		if (keyword != null) {
			vQuery += " and (ab.addressLabel like :keyword or ab.city like :keyword ) ";
		}
		
		String[] columnToView = { "id", "addressLabel", "city", "defaultBillingAddress", "defaultShippingAddress",
				"status", ""};
		if (fieldOrder > 0) {
			vQuery +=" ORDER BY ab. "+columnToView[fieldOrder] + " " + fieldOrderType;
		} else {
			vQuery +=" ORDER BY ab.id desc ";
		}
		
		
		Query query = em.createQuery(vQuery);
//		if (organisasiId != null) {
//			query.setParameter("organisasiId", organisasiId);
//		}
		if (keyword != null) {
			query.setParameter("keyword", "%"+keyword+"%");
		}
		query.setFirstResult(firstResult);
		if (maxResult != null) {
			query.setMaxResults(maxResult);
		}
		return query.getResultList();
	}

	public Long countAddressBookListByOrganisasi(String keyword) {
		String vQuery = "select count(ab) from AddressBook ab where ab.isDelete = 0 ";
//		if (organisasiId != null) {
//			vQuery += " and ab.organisasi.id = :organisasiId ";
//		}
		if (keyword != null) {
			vQuery += " and (ab.addressLabel like :keyword or ab.city like :keyword ) ";
		}
		Query query = em.createQuery(vQuery);
//		if (organisasiId != null) {
//			query.setParameter("organisasiId", organisasiId);
//		}
		if (keyword != null) {
			query.setParameter("keyword", "%"+keyword+"%");
		}
		return (Long) query.getSingleResult();
	}

	public AddressBook insertAddressBook(AddressBook addressBook, Token token) {
		addressBook.setCreated(new Date());
		addressBook.setIsDelete(0);
		super.create(addressBook, AuditHelper.OPERATION_CREATE, token);
		return addressBook;
	}

	public AddressBook updateAddressBook(AddressBook addressBook, Token token) {
		addressBook.setUpdated(new Date());
		super.edit(addressBook, AuditHelper.OPERATION_UPDATE, token);
		return addressBook;
	}

	public AddressBook deleteAddressBook(int id, Token token) {
		AddressBook addressBook = super.find(id);
		addressBook.setIsDelete(1);
		addressBook.setDeleted(new Date());
		super.edit(addressBook, AuditHelper.OPERATION_DELETE, token);
		return addressBook;
	}

	public AddressBook deleteRowAddressBook(int id, Token token) {
		AddressBook addressBook = super.find(id);
		super.remove(addressBook, AuditHelper.OPERATION_ROW_DELETE, token);
		return addressBook;
	}
	
	@SuppressWarnings("unchecked")
	public List<AddressBookDTO> getAddressBookListByOrganisasi(Integer organisasiId) {
		Query query = em.createNamedQuery("AddressBook.findListByOrganisasi");
		query.setParameter("organisasiId", organisasiId);
		List<AddressBook> la = query.getResultList();
		List<AddressBookDTO> adDTOList = new ArrayList<AddressBookDTO>();
		for (AddressBook a : la) {
			adDTOList.add(new AddressBookDTO(a));
		}
		return adDTOList;
	}

	public AddressBook getById(Integer addressBookId) {
		
		Query query = em.createNamedQuery("AddressBook.getById");
		query.setParameter("id", addressBookId);
		List<AddressBook> addressBookList = query.getResultList();
		if (addressBookList != null && addressBookList.size() > 0) {
			return addressBookList.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<AddressBookDTOForCreatePO> getAddressBookListByPurchaseRequestId(Integer prId) {
		Query query = em.createNamedQuery("AddressBook.findByPrId");
		query.setParameter("isDelete", Constant.ZERO_VALUE)
		.setParameter("prId", prId);
		List<Object[]> result = (List<Object[]>) query.getResultList();
		List<AddressBookDTOForCreatePO> adBookDTOList = new ArrayList<>();
		for(Object[] obj : result) {
			AddressBookDTOForCreatePO adBookDTO = new AddressBookDTOForCreatePO();
			adBookDTO.setAddressBook((AddressBook) obj[0]);
			adBookDTO.setPurchaseRequestItem((PurchaseRequestItem)obj[1]);
			adBookDTOList.add(adBookDTO);
		}
		return adBookDTOList;
	}
	@SuppressWarnings("unchecked")
	public List<AddressBookDTO> getAddressBookListByOrganisasiList(List<Integer> organisasiListId) {
		Query query = em.createNamedQuery("AddressBook.getAddressBookListByOrganisasiList");
		query.setParameter("organisasiListId", organisasiListId);
		List<AddressBook> la = query.getResultList();
		List<AddressBookDTO> adDTOList = new ArrayList<AddressBookDTO>();
		for (AddressBook a : la) {
			adDTOList.add(new AddressBookDTO(a));
		}
		return adDTOList;
	}
}
