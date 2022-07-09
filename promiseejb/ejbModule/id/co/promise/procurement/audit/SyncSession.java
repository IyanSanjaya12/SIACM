package id.co.promise.procurement.audit;

import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.rowset.serial.SerialClob;
import javax.xml.bind.JAXB;

import org.jboss.logging.Logger;
import org.json.JSONObject;

import id.co.promise.procurement.entity.Sync;
import id.co.promise.procurement.entity.Token;
import id.co.promise.procurement.entity.api.Response;
import id.co.promise.procurement.utils.AbstractFacadeWithAudit;
import id.co.promise.procurement.utils.audit.AuditHelper;
import id.co.promise.procurement.vendor.VendorSession;
import oracle.jdbc.OracleConnection;

/**
 * @author : Reinhard MT
 */

@Stateless
@LocalBean
public class SyncSession extends AbstractFacadeWithAudit<Sync> {
	

	public SyncSession() {
		super(Sync.class);
	}

	final static Logger logger = Logger.getLogger(VendorSession.class);

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	@PersistenceContext(unitName = "promiseAuditPU")
	private EntityManager ema;

	public Sync insert(Sync sync, Token token) {
		sync.setTglProcess(new Date());
		sync.setCreated(new Date());
		sync.setIsDelete(0);
		super.create(sync, AuditHelper.OPERATION_CREATE, token);
		return sync;
	}
	
	public Sync update(Sync sync) {
		super.edit(sync);
		return sync;
	}

	public void create(String url, Object objRequest, Response response, String method, String serviceName ) throws SQLException {
		OracleConnection conn = null; 
		Integer status = 1;
		if (response.getStatusText() != null ) {
			status = 0;
		}
		
		Clob clobRequest = null;
		if ( objRequest !=null ) {
			JSONObject jsonObjRequest = new JSONObject(objRequest);
			String objRequestString = jsonObjRequest.toString();
			 clobRequest = new SerialClob(objRequestString.toCharArray());
		}

		JSONObject jsonObjResponse = new JSONObject(response);
		String objResponsString = jsonObjResponse.toString();

		
		Clob clobResponse = new SerialClob(objResponsString.toCharArray());

		Sync sync = new Sync();
		sync.setUrl(url);
		sync.setRequest(clobRequest);
		sync.setResponse(clobResponse);
		sync.setMethod(method);
		sync.setStatus(status);
		sync.setServiceName(serviceName);
		
		//Token tokenNew = tokenSession.findByToken(token);
		this.insert(sync, null);

	}
	
	public void create(String url, Object objRequest, Response response, String method, String serviceName, String externalId) throws SQLException {
		OracleConnection conn = null; 
		Integer status = 1;
		if (response.getStatusText() != null ) {
			status = 0;
		}
		
		Clob clobRequest = null;
		if ( objRequest !=null ) {
			JSONObject jsonObjRequest = new JSONObject(objRequest);
			String objRequestString = jsonObjRequest.toString();
			 clobRequest = new SerialClob(objRequestString.toCharArray());
		}

		JSONObject jsonObjResponse = new JSONObject(response);
		String objResponsString = jsonObjResponse.toString();

		
		Clob clobResponse = new SerialClob(objResponsString.toCharArray());

		Sync sync = new Sync();
		sync.setUrl(url);
		sync.setRequest(clobRequest);
		sync.setResponse(clobResponse);
		sync.setMethod(method);
		sync.setStatus(status);
		sync.setServiceName(serviceName);
		sync.setExternalId(externalId);
		
		//Token tokenNew = tokenSession.findByToken(token);
		this.insert(sync, null);

	}
	
	public void create(String url, Object objRequest, String response, String method, String serviceName ) throws SQLException {
		OracleConnection conn = null; 
		Integer status = 1;

		
		Clob clobRequest = null;
		if ( objRequest !=null ) {
			JSONObject jsonObjRequest = new JSONObject(objRequest);
			String objRequestString = jsonObjRequest.toString();
			 clobRequest = new SerialClob(objRequestString.toCharArray());
		}
	
		Clob clobResponse = new SerialClob(response.toCharArray());

		Sync sync = new Sync();
		sync.setUrl(url);
		sync.setRequest(clobRequest);
		sync.setResponse(clobResponse);
		sync.setMethod(method);
		sync.setStatus(status);
		sync.setServiceName(serviceName);
		
		//Token tokenNew = tokenSession.findByToken(token);
		this.insert(sync, null);

	}
	
	public void createXml(String url, Object objRequest, Object objResponse, String method, String serviceName ) throws SQLException {
		OracleConnection conn = null; 
		Integer status = 1;
		
		Clob clobRequest = null;	
		if ( objRequest !=null ) {
			String xmlString = objToStringXml(objRequest);
			 clobRequest = new SerialClob(xmlString.toCharArray());
		}
		
		Clob clobResponse = null;
		if ( objResponse !=null ) {
			String xmlString = objToStringXml(objResponse);
			clobResponse = new SerialClob(xmlString.toCharArray());
		}
		
		Sync sync = new Sync();
		sync.setUrl(url);
		sync.setRequest(clobRequest);
		sync.setResponse(clobResponse);
		sync.setMethod(method);
		sync.setStatus(status);
		sync.setServiceName(serviceName);
		
		//Token tokenNew = tokenSession.findByToken(token);
		this.insert(sync, null);

	}
	
	
	public String objToStringXml(Object obj) {
		StringWriter sw = new StringWriter();
		JAXB.marshal(obj, sw);
		String xmlString = sw.toString();
		
		return xmlString;
	}

	@Override
	protected EntityManager getEntityManager() {

		// TODO Auto-generated method stub
		return em;
	}

	@Override
	protected EntityManager getEntityManagerAudit() {
		// TODO Auto-generated method stub
		return null;
	}
}
