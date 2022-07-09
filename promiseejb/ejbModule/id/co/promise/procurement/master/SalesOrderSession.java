package id.co.promise.procurement.master;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.email.EmailNotificationSession;
import id.co.promise.procurement.entity.Aksi;
import id.co.promise.procurement.entity.EmailNotification;
import id.co.promise.procurement.entity.SalesOrder;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
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
public class SalesOrderSession extends AbstractFacadeWithAudit<SalesOrder> {
	
	@EJB
	EmailNotificationSession emailNotificationSession;

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public SalesOrderSession() {
		super(SalesOrder.class);
	}

	public SalesOrder getSalesOrder(int id){
		return super.find(id);
	}
	
	public List<SalesOrder> getSalesOrderList() {
		Query q = em.createNamedQuery("SalesOrder.find");
		return q.getResultList();
	}
	
	public SalesOrder updateStatusSalesOrderbySalesOrderId(int id, Token token) {
		SalesOrder so = super.find(id);
		so.setStatus(1);
		super.edit(so, AuditHelper.OPERATION_DELETE, token);
		
		return so;
	}
	
	
	
	public SalesOrder insertSalesOrder(SalesOrder salesOrder, Token token){
		salesOrder.setCreated(new Date());
		salesOrder.setIsDelete(0);
		super.create(salesOrder, AuditHelper.OPERATION_CREATE, token);
		return salesOrder;
	}
	
	public SalesOrder updateSalesOrder(SalesOrder salesOrder, Token token){
		salesOrder.setIsDelete(0);
		super.edit(salesOrder, AuditHelper.OPERATION_UPDATE, token);
		return salesOrder;				
	}
	
	public SalesOrder deleteRole(int id, Token token){
		SalesOrder salesOrder = super.find(id);
		salesOrder.setIsDelete(1);
		salesOrder.setDeleted(new Date());
		super.edit(salesOrder, AuditHelper.OPERATION_DELETE, token);
		return salesOrder;
	}
	
	public SalesOrder deleteRowSalesOrder(int id, Token token){
		SalesOrder salesOrder = super.find(id);
		super.remove(salesOrder, AuditHelper.OPERATION_ROW_DELETE, token);
		return salesOrder;
	}
	
	public SalesOrder insertNotificationCo(User user, int id, String soNumber){
		try {
			emailNotificationSession.getMailGeneratorCreateCOVendorAndUserCreated(user, id, soNumber);
		} catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return null;
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
