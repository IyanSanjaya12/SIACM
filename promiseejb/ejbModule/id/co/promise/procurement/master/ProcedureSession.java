package id.co.promise.procurement.master;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;


@Stateless
@LocalBean
public class ProcedureSession{

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;


	public void execute(String procedurName, Integer pkTable) {
		String q = "BEGIN "+procedurName+"("+pkTable+"); END;";
		Query query = em.createNativeQuery(q);
		query.executeUpdate();
	}
	
	public void execute(String procedurName, String valueTable) {
		String q = "BEGIN "+procedurName+"('"+valueTable+"'); END;";
		Query query = em.createNativeQuery(q);
		query.executeUpdate();
	}
	
	public void execute(String procedurName, String valueTable1, String valueTable2) {
		String q = "BEGIN "+procedurName+"('"+valueTable1+"','"+valueTable2+"'); END;";
		Query query = em.createNativeQuery(q);
		query.executeUpdate();
	}
	
	public void execute(String procedurName, Integer pkTable, Integer orgParentId) {
		String q = "BEGIN "+procedurName+"("+pkTable+","+orgParentId+"); END;";
		Query query = em.createNativeQuery(q);
		query.executeUpdate();
	}
	
	public void execute(String procedurName, Integer pkTable, String paramString2) {
		String q = "BEGIN "+procedurName+"("+pkTable+","+paramString2+"); END;";
		Query query = em.createNativeQuery(q);
		query.executeUpdate();
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	protected EntityManager getEntityManagerAudit() {
		return ema;
	}
}
