package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "STORE_JMS")
public class StoreJMS {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STORE_JMS_PK")
	private Integer storeJmsPk;
	
	@Lob
	@Column(name = "STORE_JMS_DATA")
	private String storeJmsData;
	
	@Column(name = "STORE_JMS_CREATED")
	private Date storeJmsCreated;
	
	@Column(name = "STORE_JMS_UPDATED")
	private Date storeJmsUpdated;
	
	@Column(name = "STORE_JMS_MODULE")
	private String storeJmsModule;
	
	/** 
	 * 0 = NEW
	 * 1 = SUCCESS
	 * 2 = FAILED
	 * **/
	@Column(name = "STORE_JMS_STAT")
	private Integer storeJmsStat;
	
	@Lob
	@Column(name = "STORE_JMS_REASON")
	private String storeJmsReason;

	public Integer getStoreJmsPk() {
		return storeJmsPk;
	}

	public void setStoreJmsPk(Integer storeJmsPk) {
		this.storeJmsPk = storeJmsPk;
	}

	public String getStoreJmsData() {
		return storeJmsData;
	}

	public void setStoreJmsData(String storeJmsData) {
		this.storeJmsData = storeJmsData;
	}

	public Date getStoreJmsCreated() {
		return storeJmsCreated;
	}

	public void setStoreJmsCreated(Date storeJmsCreated) {
		this.storeJmsCreated = storeJmsCreated;
	}

	public Date getStoreJmsUpdated() {
		return storeJmsUpdated;
	}

	public void setStoreJmsUpdated(Date storeJmsUpdated) {
		this.storeJmsUpdated = storeJmsUpdated;
	}

	public String getStoreJmsModule() {
		return storeJmsModule;
	}

	public void setStoreJmsModule(String storeJmsModule) {
		this.storeJmsModule = storeJmsModule;
	}

	public Integer getStoreJmsStat() {
		return storeJmsStat;
	}

	public void setStoreJmsStat(Integer storeJmsStat) {
		this.storeJmsStat = storeJmsStat;
	}

	public String getStoreJmsReason() {
		return storeJmsReason;
	}

	public void setStoreJmsReason(String storeJmsReason) {
		this.storeJmsReason = storeJmsReason;
	}

}
