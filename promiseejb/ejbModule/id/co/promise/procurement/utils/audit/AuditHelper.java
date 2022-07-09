package id.co.promise.procurement.utils.audit;

import id.co.promise.procurement.entity.TableLog;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.security.TokenSession;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Table;

import org.jboss.logging.Logger;


public class AuditHelper {
	
	@EJB
	private TokenSession tokenSession;

	public static final int OPERATION_CREATE = 1;
	public static final int OPERATION_UPDATE = 2;
	public static final int OPERATION_DELETE = 3;
	public static final int OPERATION_ROW_DELETE = 4;
	public static final int OPERATION_UNDELETE = 5;

	private final static Integer MAX_STRING_LENGTH_VALUE = 2900;

	static SimpleDateFormat sdfDateMMYYYYStrip = new SimpleDateFormat("dd-MM-yyyy");
	static SimpleDateFormat sdfDateMMYYYYHHmmSS = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	final static Logger logger = Logger.getLogger(AuditHelper.class);
	
	public static void logActionUpdate(EntityManager em, EntityManager emAudit, int actionType, Object obj, Token token) {
		Object id = em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(obj);
		Object oldEntity = null;
		
		if (actionType != OPERATION_CREATE) {
			oldEntity = emAudit.find(obj.getClass(), id);
		}
		
		/*Find user by token*/
		User user = token== null? null: token.getUser();
		//logger.info ("token = " + token + " user" + token);
		
		
		for (Field field : obj.getClass().getDeclaredFields()) {
		/*	System.out.println("field = " + field);*/
			Column column = field.getAnnotation(Column.class);
			if ( column != null ){
				Object oldValue = actionType == OPERATION_CREATE ? null : ReflectionUtil.getInstance(oldEntity).getFieldValue(field.getName());
				Object newValue = ReflectionUtil.getInstance(obj).getFieldValue(field.getName());
	
				Boolean isUpdated = false;
	
				isUpdated = (((oldValue == null) && (newValue != null)) || ((oldValue != null) && (!oldValue.equals(newValue))));
			
				String strNamatable = getTableNamebyClass(obj.getClass());
			
				/*insert log for columns only when update = true*/
				/*logger.info("Id = " + id + " Field Name = " + field.getName() + " oldVal=" + oldValue + " newVal="+ newValue);*/
				
				if (isUpdated) {
						insertLog(em, actionType, strNamatable, column.name(), strValue(oldValue), strValue(newValue), new Date(), user);
				}
			}
		}
	}

	private static void insertLog(EntityManager em, Integer actType, String strNamatable, String strNamaKolom, String nilaiAwal, String nilaiBaru, Date time, User user) {
	
		TableLog t = new TableLog();
		t.setActionType(actType);
		t.setNamaTable(strNamatable);
		t.setNamaKolom(strNamaKolom);
		t.setNilaiAwal(nilaiAwal);
		t.setNilaiBaru(nilaiBaru);
		t.setWatu(time);
		t.setUser(user);
		em.persist(t);
	}

	static String getTableNamebyClass(Class<?> c) {

		Table table = c.getAnnotation(Table.class);
		String tableName = table.name();
		return tableName;
	}

	private static String strValue(Object objVal) {

		String value = "";
		if (objVal != null) {
			if (objVal instanceof Date) {
				value = getDateddMMYYYYStrip((Date) objVal);
				
			} else if (objVal instanceof Timestamp) {
				value = getDateddMMYYYYHHmmSS((Timestamp) objVal);
			} else {
				value = objVal.toString();
				if (value.length() > MAX_STRING_LENGTH_VALUE) {
					value = value.substring(0, MAX_STRING_LENGTH_VALUE);
				}
			}
		}

		return value;
	}

	public static String getDateddMMYYYYStrip(Date date) {
		if (date != null) {
			return sdfDateMMYYYYStrip.format(date);
		} else {
			return "";
		}
	}

	public static String getDateddMMYYYYHHmmSS(java.sql.Timestamp date) {
		if (date != null) {
			return sdfDateMMYYYYHHmmSS.format(date);
		} else {
			return "";
		}
	}



}
