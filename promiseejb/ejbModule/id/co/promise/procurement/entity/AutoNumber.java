package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T1_AUTONUMBER_SEQ")
@NamedQueries({
@NamedQuery
	(name="AutoNumber.getAutoNumberByType", query="SELECT autonumber FROM AutoNumber autonumber WHERE autonumber.isDelete = 0 AND autonumber.type=:type"),
	@NamedQuery
	(name="AutoNumber.getAutoNumberByList", query="SELECT autonumber FROM AutoNumber autonumber WHERE autonumber.isDelete = 0"),
	@NamedQuery
	(name="AutoNumber.getAutoNumberByTypeAndOrganisasiId", query="SELECT autonumber FROM AutoNumber autonumber WHERE autonumber.isDelete = 0 AND autonumber.type=:type AND autonumber.organisasi.id=:organisasiId")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_AUTONUMBER_SEQ", initialValue = 1, allocationSize = 1)
public class AutoNumber {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AUTONUMBER_SEQ_ID")
	private Integer id;

	@Column(name = "TYPE", length = 16)
	private String type;

	@Column(name = "FORMAT")
	private String format;
	
	@Column(name = "SEQ_VAL")
	private Integer seqVal;
	
	@Column(name = "TAHUN")
	private Integer tahun;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RESET_ON")
	private Date resetOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Integer getSeqVal() {
		return seqVal;
	}

	public void setSeqVal(Integer seqVal) {
		this.seqVal = seqVal;
	}

	public Date getResetOn() {
		return resetOn;
	}

	public void setResetOn(Date resetOn) {
		this.resetOn = resetOn;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getTahun() {
		return tahun;
	}

	public void setTahun(Integer tahun) {
		this.tahun = tahun;
	}
	
	
	
}
