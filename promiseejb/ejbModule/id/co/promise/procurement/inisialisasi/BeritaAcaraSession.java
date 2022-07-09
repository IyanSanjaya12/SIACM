package id.co.promise.procurement.inisialisasi;

import java.util.Date;
import java.util.List;

import id.co.promise.procurement.entity.BeritaAcara;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 
 * Project Name : promiseejb Package Name :
 * id.co.promise.procurement.inisialisasi File Name : BeritaAcaraSession.java
 * Description :
 * 
 * @author : Reinhard MT, reinhardmt@mmi-pt.com, rhtanamal@gmail.com Since : Sep
 *         12, 2015
 *
 */

@Stateless
@LocalBean
public class BeritaAcaraSession extends AbstractFacadeWithAudit<BeritaAcara> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public BeritaAcaraSession() {

		super(BeritaAcara.class);
	}

	public BeritaAcara get(int id) {

		return super.find(id);
	}

	public List<BeritaAcara> getListByPengadaanAndTahapan(int pengadaanId,
			int tahapanId) {

		Query q = em
				.createNamedQuery("BeritaAcara.getListByPengadaanAndTahapan");
		q.setParameter("pengadaanId", pengadaanId);
		q.setParameter("tahapanId", tahapanId);
		return q.getResultList();
	}

	public List<BeritaAcara> getList() {

		Query q = em.createNamedQuery("BeritaAcara.getList");
		return q.getResultList();
	}

	public BeritaAcara insert(BeritaAcara beritaAcara, Token token) {

		beritaAcara.setCreated(new Date());
		beritaAcara.setIsDelete(0);
		super.create(beritaAcara, AuditHelper.OPERATION_CREATE, token);
		return beritaAcara;
	}

	public BeritaAcara update(BeritaAcara beritaAcara, Token token) {

		beritaAcara.setUpdated(new Date());
		super.edit(beritaAcara, AuditHelper.OPERATION_UPDATE, token);
		return beritaAcara;
	}

	public BeritaAcara delete(int id, Token token) {

		BeritaAcara beritaAcara = super.find(id);
		beritaAcara.setIsDelete(1);
		beritaAcara.setDeleted(new Date());
		super.create(beritaAcara, AuditHelper.OPERATION_DELETE, token);
		return beritaAcara;
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
