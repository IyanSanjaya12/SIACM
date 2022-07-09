package id.co.promise.procurement.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.RoleUser;
import id.co.promise.procurement.entity.SalesOrder;
import id.co.promise.procurement.entity.SalesOrderItem;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.security.RoleUserSession;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class SalesOrderItemSession extends AbstractFacadeWithAudit<SalesOrderItem> {
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;
	
	@EJB
	private  SalesOrderSession salesOrderSession;
	
	@EJB
	private RoleUserSession roleUserSession;

	public SalesOrderItemSession() {
		super(SalesOrderItem.class);
	}

	public SalesOrderItem getSalesOrderItem(int id){
		return super.find(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<SalesOrderItem> getSalesOrderItemBySalesOrderIdAndVendorId(int id, int vId){
		
		Query q = em.createQuery("SELECT soi FROM SalesOrderItem soi WHERE soi.isDelete = 0 AND soi.salesOrder.id = :id AND soi.catalog.vendor.id = :vId");
		q.setParameter("id", id);
		q.setParameter("vId", vId);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SalesOrderItem> getSalesOrderItemByVendorId(int id){
		
		Query q = em.createQuery("SELECT soi FROM SalesOrderItem soi WHERE soi.isDelete = 0 and soi.catalog.vendor.id = :id)");
		q.setParameter("id", id);
		
		List<SalesOrderItem> soiList = q.getResultList();
		List<SalesOrderItem> soiListFiltering = new ArrayList<SalesOrderItem>();
		
		for(SalesOrderItem soi : soiList){
			boolean isAdd = true;
			for(SalesOrderItem soifilter : soiListFiltering){
				/*
				 * if(soi.getSoNumber().equalsIgnoreCase(soifilter.getSoNumber())){ isAdd =
				 * false; }
				 */
			}
			if(isAdd){
				
				List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(soi.getSalesOrder().getUserCreate());
				
				if(roleUser.size() != 0){
					soi.setBuyer(roleUser.get(0).getOrganisasi().getNama());
				}
				
				soiListFiltering.add(soi);
			}
		}
		

		return soiListFiltering;
	}
	
	@SuppressWarnings("unchecked")
	public List<SalesOrderItem> getSalesOrderItemBySalesOrderIdAndByVendorId(int salesOrderId,int vendorId){
		
		Query q = em.createQuery("SELECT soi FROM SalesOrderItem soi WHERE soi.isDelete = 0 "
				+ "	and soi.isDelete =0 and soi.catalog.vendor.id = :vendorId and soi.salesOrder.id = :salesOrderId");
		q.setParameter("vendorId", vendorId);
		q.setParameter("salesOrderId", salesOrderId);
		
		List<SalesOrderItem> soiList = q.getResultList();
		List<SalesOrderItem> soiListFiltering = new ArrayList<SalesOrderItem>();
		
		/*for(SalesOrderItem soi : soiList){
			boolean isAdd = true;
			for(SalesOrderItem soifilter : soiListFiltering){
				if(soi.getSoNumber().equalsIgnoreCase(soifilter.getSoNumber())){
					isAdd = false;
				}
			}
			if(isAdd){
				soiListFiltering.add(soi);
			}
		}*/

		return soiList;
	}
	
	@SuppressWarnings("unchecked")
	public List<SalesOrderItem> getSalesOrderItemListByUserId(int id){
		
		Query q = em.createQuery("SELECT soi FROM SalesOrderItem soi WHERE soi.isDelete = 0 AND soi.salesOrder.userCreate = :id");
		q.setParameter("id", id);
		
		List<SalesOrderItem> salesOrderItemList = q.getResultList();
		List<SalesOrderItem> salesOrderItemFilterList = new ArrayList<SalesOrderItem>();
		for(SalesOrderItem soi : salesOrderItemList){
			boolean isAdd = true;
			for(SalesOrderItem soifilter : salesOrderItemFilterList){
				/*
				 * if(soi.getSoNumber().equalsIgnoreCase(soifilter.getSoNumber())){ isAdd =
				 * false; }
				 */
			}
			if(isAdd){
				
				List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(soi.getSalesOrder().getUserCreate());
				
				if(roleUser.size() != 0){
					soi.setBuyer(roleUser.get(0).getOrganisasi().getNama());
				}
				
				salesOrderItemFilterList.add(soi);
			}
		}
	
		return salesOrderItemFilterList;
	}
	
	@SuppressWarnings("unchecked")
	public List<SalesOrderItem> getSalesOrderItemList(){
		
		Query q = em.createQuery("SELECT soi FROM SalesOrderItem soi WHERE soi.isDelete = 0");

		List<SalesOrderItem> salesOrderItemList = q.getResultList();
		List<SalesOrderItem> salesOrderItemFilterList = new ArrayList<SalesOrderItem>();
		for(SalesOrderItem soi : salesOrderItemList){
			boolean isAdd = true;
			for(SalesOrderItem soifilter : salesOrderItemFilterList){
				/*
				 * if(soi.getSoNumber().equalsIgnoreCase(soifilter.getSoNumber())){ isAdd =
				 * false; }
				 */
			}
			if(isAdd){
				
				List<RoleUser> roleUser = roleUserSession.getRoleUserByUserId(soi.getSalesOrder().getUserCreate());
				
				if(roleUser.size() != 0){
					soi.setBuyer(roleUser.get(0).getOrganisasi().getNama());
				}
				
				salesOrderItemFilterList.add(soi);
			}
		}
	
		return salesOrderItemFilterList;
	}
	
	public SalesOrderItem insertSalesOrderItem(SalesOrderItem SalesOrderItem, Token token){
		SalesOrderItem.setCreated(new Date());
		SalesOrderItem.setIsDelete(0);
		super.create(SalesOrderItem, AuditHelper.OPERATION_CREATE, token);
		return SalesOrderItem;
	}
	
	public SalesOrderItem updateSalesOrderItem(SalesOrderItem SalesOrderItem, Token token){
		SalesOrderItem.setIsDelete(0);
		super.edit(SalesOrderItem, AuditHelper.OPERATION_UPDATE, token);
		return SalesOrderItem;				
	}
	
	public SalesOrderItem deleteSalesOrderItem(int id, Token token){
		SalesOrderItem salesOrderItem = super.find(id);
		salesOrderItem.setIsDelete(1);
		salesOrderItem.setDeleted(new Date());
		super.edit(salesOrderItem, AuditHelper.OPERATION_DELETE, token);
		return salesOrderItem;
	}
	
	public SalesOrderItem deleteRowSalesOrderItem(int id, Token token){
		SalesOrderItem salesOrderItem = super.find(id);
		super.remove(salesOrderItem, AuditHelper.OPERATION_ROW_DELETE, token);
		return salesOrderItem;
	}
	
	public SalesOrderItem deleteSalesOrderItemBySalesOrderItemSoNumber(String soNumber, Token token){
		
		//List<SalesOrderItem> soiList = getSalesOrderItemBySalesOrderId(id);
		
		Query q = em.createQuery("SELECT soi FROM SalesOrderItem soi WHERE soi.isDelete = 0 AND soi.soNumber = :soNumber");
		q.setParameter("soNumber", soNumber);
		
		List<SalesOrderItem> soiList = q.getResultList();
		
		for(SalesOrderItem soi : soiList){
			soi.setIsDelete(1);
			soi.setDeleted(new Date());
			super.edit(soi, AuditHelper.OPERATION_DELETE, token);
		}

		salesOrderSession.deleteRole(soiList.get(0).getSalesOrder().getId(), token);
		return null;
	}
	
	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return ema;
	}
	
	
}
