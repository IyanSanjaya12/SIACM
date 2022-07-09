package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.MenuAksi;
import id.co.promise.procurement.entity.Panitia;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class PanitiaSession extends AbstractFacadeWithAudit<Panitia> {
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	@EJB
	AnggotaPanitiaSession anggotaPanitiaSession;

	@EJB
	PejabatPelaksanaPengadaanSession pejabatPelaksanaSession;

	public PanitiaSession() {
		super(Panitia.class);
	}

	public Panitia getPanitia(int id) {
		return super.find(id);
	}

	public List<Panitia> getPanitiaList() {
		Query q = em.createNamedQuery("Panitia.find");
		return q.getResultList();
	}
	
	public List<Object> getAllPanitiaList() {
		Query q = em.createNamedQuery("Panitia.findAll");
		return q.getResultList();
	}

	public Panitia getPanitiaByUser(int userId) {
		List<AnggotaPanitia> anggotaPanitiaList = anggotaPanitiaSession
				.getAnggotaPanitiaByRoleUserList(userId);

		if (anggotaPanitiaList != null && anggotaPanitiaList.size() > 0) {
			AnggotaPanitia anggotaPanitia = anggotaPanitiaList.get(0);
			return anggotaPanitia.getTimPanitia().getPanitia();
		} else {
			List<PejabatPelaksanaPengadaan> listPejabat = pejabatPelaksanaSession
					.getPejabatPelaksanaByRoleUserList(userId);
			if (listPejabat.size() > 0) {
				PejabatPelaksanaPengadaan pelaksanaPengadaan = listPejabat
						.get(0);
				return pelaksanaPengadaan.getPanitia();
			} else {
				return null;
			}
		}
	}
	
	
	


	public Panitia insertPanitia(Panitia panitia, Token token) {
		panitia.setCreated(new Date());
		panitia.setIsDelete(0);
		super.create(panitia, AuditHelper.OPERATION_CREATE, token);
		return panitia;
	}

	public Panitia updatePanitia(Panitia panitia, Token token) {
		panitia.setUpdated(new Date());
		super.edit(panitia, AuditHelper.OPERATION_UPDATE, token);
		return panitia;
	}

	public Panitia deletePanitia(int id, Token token) {
		Panitia panitia = super.find(id);
		panitia.setIsDelete(1);
		panitia.setDeleted(new Date());
		super.edit(panitia, AuditHelper.OPERATION_DELETE, token);
		return panitia;
	}

	public Panitia deleteRowPanitia(int id, Token token) {
		Panitia panitia = super.find(id);
		super.remove(panitia, AuditHelper.OPERATION_ROW_DELETE, token);
		return panitia;
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
