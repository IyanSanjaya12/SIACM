package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "T3_BANK_VENDOR")
@NamedQueries({
	@NamedQuery(name="BankVendor.findByVendor",
        	query="SELECT bankVendor FROM BankVendor bankVendor WHERE bankVendor.vendor.id =:vendorId AND bankVendor.isDelete = 0"),
	@NamedQuery(name="BankVendor.findByBankNumber", query="SELECT bankVendor FROM BankVendor bankVendor "
			+ "WHERE bankVendor.nomorRekening =:bankNumber AND bankVendor.vendor.id=:vendorId AND bankVendor.isDelete = 0"),
	@NamedQuery(name = "BankVendor.findByBnktAndSequence", query = "SELECT MAX(bankVendor.bnktSequence) FROM BankVendor bankVendor WHERE "
			+ "bankVendor.mataUang=:mataUang AND bankVendor.vendor =:vendor AND bankVendor.isDelete = 0"),
	@NamedQuery(name ="BankVendor.findSequence", query ="SELECT bankVendor from BankVendor bankVendor WHERE bankVendor.isDelete = 0 AND "
			+ "bankVendor.vendor = :vendor AND bankVendor.mataUang=:mataUang ORDER BY bankVendor.bnktSequence DESC "),
	@NamedQuery(name ="BankVendor.deleteBankVendor", query ="UPDATE BankVendor bankVendor set bankVendor.isDelete = 1 WHERE bankVendor.vendor.id =:vendorId ")
		
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_BANK_VENDOR", initialValue = 1, allocationSize = 1)
public class BankVendor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BANK_VENDOR_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "VENDOR_ID", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@Column(name = "NAMA_BANK")
	private String namaBank;
	
	@Column(name = "CABANG_BANK")
	private String cabangBank;
	
	@Column(name = "ALAMAT_BANK")
	private String alamatBank;
	
	@Column(name = "KOTA")
	private String kota;
	
	@Column(name = "NEGARA")
	private String negara;
	
	@Column(name = "NOMOR_REKENING")
	private String nomorRekening;
	
	@Column(name = "NAMA_NASABAH")
	private String namaNasabah;

	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Column(name="BNKT")
	private String bnkt;
	
	@Column(name="BNKT_SEQUENCE")
	private Integer bnktSequence;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getNamaBank() {
		return namaBank;
	}

	public void setNamaBank(String namaBank) {
		this.namaBank = namaBank;
	}

	public String getCabangBank() {
		return cabangBank;
	}

	public void setCabangBank(String cabangBank) {
		this.cabangBank = cabangBank;
	}

	public String getAlamatBank() {
		return alamatBank;
	}

	public void setAlamatBank(String alamatBank) {
		this.alamatBank = alamatBank;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public String getNegara() {
		return negara;
	}

	public void setNegara(String negara) {
		this.negara = negara;
	}

	public String getNomorRekening() {
		return nomorRekening;
	}

	public void setNomorRekening(String nomorRekening) {
		this.nomorRekening = nomorRekening;
	}

	public String getNamaNasabah() {
		return namaNasabah;
	}

	public void setNamaNasabah(String namaNasabah) {
		this.namaNasabah = namaNasabah;
	}

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
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

	public String getBnkt() {
		return bnkt;
	}

	public void setBnkt(String bnkt) {
		this.bnkt = bnkt;
	}

	public Integer getBnktSequence() {
		return bnktSequence;
	}

	public void setBnktSequence(Integer bnktSequence) {
		this.bnktSequence = bnktSequence;
	}
	
}
