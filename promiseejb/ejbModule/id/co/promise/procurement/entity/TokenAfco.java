package id.co.promise.procurement.entity;

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

/**
 * @author User
 *
 */
@Entity
@Table(name = "T1_TOKEN_AFCO")
@NamedQueries({
	@NamedQuery(name="TokenAfco.find", query="SELECT tokenAfco FROM TokenAfco tokenAfco WHERE tokenAfco = tokenAfco and tokenAfco.isDelete=0"),
    @NamedQuery(name="TokenAfco.findByCode", query="SELECT tokenAfco FROM TokenAfco tokenAfco WHERE tokenAfco.kodeToken LIKE :kodeToken and tokenAfco.isDelete=0")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_TOKEN_AFCO", initialValue = 1, allocationSize = 1)
public class TokenAfco {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TOKEN_AFCO_ID")
	private Integer id;
	
	@Column(name = "KODE_AFCO")
	private String kodeAfco;
	
	@Column(name = "KODE_TOKEN")
	private String kodeToken;
	
	@Column(name = "IS_DELETED")
	private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getKodeAfco() {
		return kodeAfco;
	}

	public void setKodeAfco(String kodeAfco) {
		this.kodeAfco = kodeAfco;
	}

	public String getKodeToken() {
		return kodeToken;
	}

	public void setKodeToken(String kodeToken) {
		this.kodeToken = kodeToken;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
