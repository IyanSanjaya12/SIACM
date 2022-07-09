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

@Entity
@NamedQueries({
    @NamedQuery(name="MataUang.find", query="SELECT mataUang FROM MataUang mataUang WHERE mataUang.isDelete = 0"),
    @NamedQuery(name="mataUang.findKode", query="select mataUang from MataUang mataUang where mataUang.isDelete=0 and mataUang.kode = :kode"),
    @NamedQuery(name="mataUang.findNama", query="select mataUang from MataUang mataUang where mataUang.isDelete=0 and mataUang.nama = :nama")
})
@Table(name="T1_MATA_UANG")
@TableGenerator( name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE", 
	pkColumnValue = "T1_MATA_UANG", initialValue = 1, allocationSize = 1 )
public class MataUang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="MATA_UANG_ID")
	private Integer id;
	
	@Column(name="NAMA", length=100)
	private String nama;
	
	@Column(name="KODE", length=16)
	private String kode;
	
	@Column(name="KURS")
	private Double kurs;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED")
	private Date deleted;
	
	@Column(name="USERID")
	private Integer userId;
	
	@Column(name="ISDELETE", columnDefinition="tinyint(1) default 0")
	private Integer isDelete;
	
	@Column(name="KODE_EBS")
	private String kodeEbs;

	public String getKodeEbs() {
		return kodeEbs;
	}

	public void setKodeEbs(String kodeEbs) {
		this.kodeEbs = kodeEbs;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public Double getKurs() {
		return kurs;
	}

	public void setKurs(Double kurs) {
		this.kurs = kurs;
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

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}
