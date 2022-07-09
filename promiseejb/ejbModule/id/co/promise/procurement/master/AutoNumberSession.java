package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Afco;
import id.co.promise.procurement.entity.AutoNumber;
import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.entity.Token;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@LocalBean
public class AutoNumberSession extends AbstractFacadeWithAudit<AutoNumber> {
	private Class<AutoNumber> entityClass;
	
	@EJB
	AutoNumberSession autoNumberSession;
	
	@EJB
	AfcoSession afcoSession;
	
	@EJB
	OrganisasiSession organisasiSession;
	
	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;
	
	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public AutoNumberSession() {
		super(AutoNumber.class);
	}
	
	public AutoNumber Autonumber(int id){
	    return super.find(id);
	}

	public List<AutoNumber> getAutoNumberByType(String type){
	    Query q = em.createNamedQuery("AutoNumber.getAutoNumberByType");
	    q.setParameter("type",type);
	    return q.getResultList();
	}
	
	public List<AutoNumber> getAutoNumberByList(){
	    Query q = em.createNamedQuery("AutoNumber.getAutoNumberByList");
	    return q.getResultList();
	}
	
	public String generateNumberByOrganisasiId(String type, Token token){
		Organisasi organisasi = organisasiSession.getOrganisasiByToken(token);
		Organisasi organisasiParent = organisasiSession.getRootParentByOrganisasi(organisasi);
	    Query q = em.createNamedQuery("AutoNumber.getAutoNumberByTypeAndOrganisasiId");
	    q.setParameter("type",type);
	    q.setParameter("organisasiId",organisasiParent.getId() == null ? 0 : organisasiParent.getId());
	    @SuppressWarnings("unchecked")
		List<AutoNumber> autonumber = q.getResultList();
	    
	    AutoNumber autoNumberType = autonumber.get(0);
		Date dateNow = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
			
		//Untuk set nilai seq_val
		Integer autoIncrement = 1;
		
		autoIncrement = autoNumberType.getSeqVal() + 1;
		
		if(cal.get(Calendar.YEAR) == autoNumberType.getTahun()){
			autoNumberType.setSeqVal(autoIncrement);
		}else{
			autoNumberType.setSeqVal(1);
			autoNumberType.setTahun(cal.get(Calendar.YEAR));;
		}
		
		String formatted = String.format("%05d", autoNumberType.getSeqVal());
	  
	    int longYear = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH) + 1;

		String format = autoNumberType.getFormat();
		
		String[] formatArray =  {"ORGANISASI_CODE","LONG_YEAR","MONTH","INC"};
		
		for (int i = 0; i < formatArray.length; i++){
			if(formatArray[i].equals("ORGANISASI_CODE")) {
				format = format.replace("[ORGANISASI_CODE]", organisasiParent.getCode());
            }
			if(formatArray[i].equals("LONG_YEAR")) {
				format = format.replace("[LONG_YEAR]", Integer.toString(longYear));
            }
			if(formatArray[i].equals("MONTH")) {
				format = format.replace("[MONTH]", String.format("%02d", month));
            }
			if(formatArray[i].equals("INC")) {
				format = format.replace("[INC]", formatted);
            }
		
		}
		return format;
	}
	
	public String generateNumber(String type, Token token) {
		
		List<AutoNumber> autonumber = autoNumberSession.getAutoNumberByType(type);
		//
		AutoNumber autoNumberType = autonumber.get(0);
		Date dateNow = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
			
		//Untuk set nilai seq_val
		Integer autoIncrement = 1;
		
		autoIncrement = autoNumberType.getSeqVal() + 1;
		
		if(cal.get(Calendar.YEAR) == autoNumberType.getTahun()){
			autoNumberType.setSeqVal(autoIncrement);
		}else{
			autoNumberType.setSeqVal(1);
			autoNumberType.setTahun(cal.get(Calendar.YEAR));;
		}
		
		//AutoNumber autoNumberUpdate = autoNumberSession.updateAutoNumber(autoNumberType, token);
		
		String formatted = String.format("%05d", autoNumberType.getSeqVal());
	  
	    int longYear = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH) + 1;
	    int day = cal.get(Calendar.DAY_OF_MONTH);

		String format = autoNumberType.getFormat();
		
		String[] formatArray =  {"DEPT","LONG_YEAR","YEAR","DATE","MONTH","INC"};
		
		//Get Afco
		List<Organisasi> organisasi = organisasiSession.getListOrganisasiByToken(token);
				///afcoSession.getAfcoByToken(token);
		
		for (int i = 0; i < formatArray.length; i++){
			if(formatArray[i].equals("DEPT")) {
				format = format.replace("[DEPT]", organisasi.get(0).getNama());
            }
			if(formatArray[i].equals("LONG_YEAR")) {
				format = format.replace("[LONG_YEAR]", Integer.toString(longYear));
            }
			if(formatArray[i].equals("YEAR")) {
				format = format.replace("[YEAR]", String.valueOf(cal.get(Calendar.YEAR)).substring(2));
            }
			if(formatArray[i].equals("DATE")) {
				format = format.replace("[DATE]", Integer.toString(day));
			}
			if(formatArray[i].equals("MONTH")) {
				format = format.replace("[MONTH]", String.format("%02d", month));
            }
			if(formatArray[i].equals("INC")) {
				format = format.replace("[INC]", formatted);
            }
		
		}
		return format;
	}
	
	public AutoNumber insertAutoNumber(AutoNumber autonumber, Token token){
		autonumber.setIsDelete(0);
		super.create(autonumber, AuditHelper.OPERATION_CREATE, token);
		return autonumber;
	}
	
	public AutoNumber updateAutoNumber(AutoNumber autonumber, Token token){
		super.edit(autonumber, AuditHelper.OPERATION_UPDATE, token);
		return autonumber;
	}
	
	public AutoNumber deleteAutoNumber(int id, Token token){
		AutoNumber autonumber = super.find(id);
		autonumber.setIsDelete(1);
		//siswa.setDeleted(new Date());
		super.edit(autonumber, AuditHelper.OPERATION_DELETE, token);
		return autonumber;
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
