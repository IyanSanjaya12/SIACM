package id.co.promise.procurement.entity;

import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "SYNC")
@NamedQueries({ @NamedQuery(name = "Sync.find", query = "SELECT sync FROM Sync sync WHERE sync = sync"),

})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "SYNC", initialValue = 1, allocationSize = 1)
public class Sync {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SYNC_ID")
	private Integer id;

	@Column(name = "URL")
	private String url;
	
	@Column(name = "REQUEST")
	private Clob request;

	@Column(name = "RESPONSE")
	private Clob response;

	@Column(name = "METHOD")
	private String method;

	@Column(name = "STATUS")
	private Integer status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TGL_PROCESS")
	private Date tglProcess;

	@Column(name = "SERVICE_NAME")
	private String serviceName;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Column(name = "EXTERNALID")
	private String externalId;
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Clob getRequest() {
		return request;
	}

	public void setRequest(Clob request) {
		this.request = request;
	}



	public Clob getResponse() {
		return response;
	}

	public void setResponse(Clob response) {
		this.response = response;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}


	public Date getTglProcess() {
		return tglProcess;
	}

	public void setTglProcess(Date tglProcess) {
		this.tglProcess = tglProcess;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
