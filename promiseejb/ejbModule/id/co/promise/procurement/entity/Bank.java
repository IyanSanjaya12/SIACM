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
@Table(name = "T1_BANK")
@NamedQueries({
@NamedQuery(name="Bank.find",
	query="SELECT bank FROM Bank bank WHERE bank.isDelete = 0 ORDER BY bank.kodeBank"),
	@NamedQuery(name="bank.findKodeBank",
    query="select bank FROM Bank bank WHERE bank.isDelete = 0 and bank.kodeBank = :kodeBank"),
    @NamedQuery(name="bank.findId",
    query="select bank FROM Bank bank WHERE bank.isDelete = 0 and bank.id = :id")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_BANK", initialValue = 1, allocationSize = 1)
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BANK_ID")
	private Integer id;
	
	@Column(name = "KODE_BANK", length=100)
	private String kodeBank;
	
	@Column(name = "NAMA_BANK", length=250)
	private String namaBank;
	
	@Column(name = "STATUS_KANTOR", length=100)
	private String statusKantor;
	
	@Column(name = "NAMA_KANTOR", length=100)
	private String namaKantor;
	
	@Column(name = "ALAMAT", length=300)
	private String alamat;
	
	@Column(name = "PROPINSI", length=100)
	private String propinsi;
	
	@Column(name = "KOTA", length=100)
	private String kota;
	
	@Column(name = "KODE_POS", length=10)
	private String kodePos;
	
	@Column(name = "TELEPON", length=30)
	private String telepon;
	
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
	
	@Column(name="ISDELETE")
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKodeBank() {
		return kodeBank;
	}

	public void setKodeBank(String kodeBank) {
		this.kodeBank = kodeBank;
	}

	public String getNamaBank() {
		return namaBank;
	}

	public void setNamaBank(String namaBank) {
		this.namaBank = namaBank;
	}

	public String getStatusKantor() {
		return statusKantor;
	}

	public void setStatusKantor(String statusKantor) {
		this.statusKantor = statusKantor;
	}

	public String getNamaKantor() {
		return namaKantor;
	}

	public void setNamaKantor(String namaKantor) {
		this.namaKantor = namaKantor;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public String getPropinsi() {
		return propinsi;
	}

	public void setPropinsi(String propinsi) {
		this.propinsi = propinsi;
	}

	public String getKodePos() {
		return kodePos;
	}

	public void setKodePos(String kodePos) {
		this.kodePos = kodePos;
	}

	public String getTelepon() {
		return telepon;
	}

	public void setTelepon(String telepon) {
		this.telepon = telepon;
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
