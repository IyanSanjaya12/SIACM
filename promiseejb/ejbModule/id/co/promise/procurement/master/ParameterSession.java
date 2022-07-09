package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Parameter;
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
public class ParameterSession extends AbstractFacadeWithAudit<Parameter> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public ParameterSession() {
		super(Parameter.class);
	}

	public Parameter getParameter(String id) {
		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<Parameter> getParameterList() {
		Query q = em.createNamedQuery("Parameter.find");
		return q.getResultList();
	}

	public Parameter updateParameter(Parameter Parameter, Token token) {
		Parameter.setUpdated(new Date());
		super.edit(Parameter, AuditHelper.OPERATION_UPDATE, token);
		return Parameter;
	}
	
	public Parameter getParameterByName(String byName) {
		Query q = em.createNamedQuery("Parameter.findByName");
		q.setParameter("byName", byName);
		return (Parameter) q.getSingleResult();
	}
	
	public Boolean getParameterBooleanByName(String paramName) {
		Parameter parameter = getParameterByName(paramName);
		String captchaValidator = parameter.getNilai();
		return captchaValidator.equalsIgnoreCase("true") ? true : false;
		
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
