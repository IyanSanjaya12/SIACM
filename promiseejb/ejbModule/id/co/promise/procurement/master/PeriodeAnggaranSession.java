package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.PeriodeAnggaran;
import id.co.promise.procurement.utils.AbstractFacade;

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class PeriodeAnggaranSession extends AbstractFacade<PeriodeAnggaran> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	/**
	 * 
	 */
	public PeriodeAnggaranSession() {
		super(PeriodeAnggaran.class);
	}

	public List<PeriodeAnggaran> getList() {
		Query q = em.createNamedQuery("PeriodeAnggaran.find");
		return q.getResultList();
	}

	public PeriodeAnggaran getPeriodeAnggaran(int id) {
		return super.find(id);
	}

	public PeriodeAnggaran createPeriodeAnggaran(PeriodeAnggaran object) {
		object.setCreated(new Date());
		object.setIsDelete(0);
		super.create(object);
		return object;
	}

	public PeriodeAnggaran editPeriodeAnggaran(PeriodeAnggaran object) {
		object.setUpdated(new Date());
		super.edit(object);
		return object;
	}

	public PeriodeAnggaran deletePeriodeAnggaran(int id) {
		PeriodeAnggaran x = super.find(id);
		x.setDeleted(new Date());
		x.setIsDelete(1);
		super.edit(x);
		return x;
	}

	public PeriodeAnggaran deleteRowPeriodeAnggaran(int id) {
		PeriodeAnggaran x = super.find(id);
		super.remove(x);
		return x;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
