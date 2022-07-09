package id.co.promise.procurement.entity;

import java.math.BigDecimal;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

@Entity
@NamedQueries({ @NamedQuery(name = "AlokasiAnggaran.find", query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0"),
	@NamedQuery(name = "AlokasiAnggaran.findByUsed", query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0 and x.isUsed=0"),
	@NamedQuery(name = "AlokasiAnggaran.findByBiroPengadaan", query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0 and x.sisaAnggaran>0 AND x.biroPengadaan.id=:biroPengadaan"),
	@NamedQuery(name = "AlokasiAnggaran.findByUserId", query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0 AND x.userId=:userId"),
	@NamedQuery(name = "AlokasiAnggaran.findByAccountNameandByNomorDraff", query = "SELECT x FROM AlokasiAnggaran x WHERE x.isDelete = 0 AND x.accountName like '%:search%' OR x.nomorDraft like '%:search%'"),
	@NamedQuery(name="AlokasiAnggaran.findByNomorDraft", query="SELECT x FROM AlokasiAnggaran x WHERE x.isDelete=0 AND x.nomorDraft=:nomorDraft")
})
@Table(name = "T2_ALOKASI_ANGGARAN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ALOKASI_ANGGARAN", initialValue = 1, allocationSize = 1)
public class AlokasiAnggaran {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ALOKASI_ANGGARAN_ID")
	private Integer id;
	
	@Column(name = "NOMOR_DRAFT")
	private String nomorDraft; //bisa dijadikan account number
	
	@Column(name = "TANGGAL_DRAFT")
	private Date tanggalDraft;
	
	@Column(name="ACCOUNT_NAME")
	private String accountName;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi biroPengadaan;
	
	@Column(name = "TAHUN_ANGGARAN")
	private Integer tahunAnggaran;
	
	@OneToOne
	@JoinColumn(name = "PERIODE_ANGGARAN_ID", referencedColumnName = "PERIODE_ANGGARAN_ID")
	private PeriodeAnggaran periodeAnggaran;
	
	@OneToOne
	@JoinColumn(name = "JENIS_ANGGARAN_ID", referencedColumnName = "JENIS_ANGGARAN_ID")
	private JenisAnggaran jenisAnggaran;
	
	@ColumnDefault( value = "0" )
	@Column(name="JUMLAH")
	private Double jumlah;
	
	@ColumnDefault( value = "0" )
	@Column(name="SISA_ANGGARAN")
	private Double sisaAnggaran;
	
	@ColumnDefault( value = "0" )
	@Column(name="BOOKING_ANGGARAN")
	private Double bookingAnggaran;
	
	@OneToOne
	@JoinColumn(name = "MATA_UANG_ID", referencedColumnName = "MATA_UANG_ID")
	private MataUang mataUang;
	
	@Column(name="STATUS")
	private Integer status;
	
	@Column(name="PARENT")
	private Integer parentId;
	
	@Column(name="IS_USED")
	private Integer isUsed;
		
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
	
	@ColumnDefault( value = "0" )
	@Column(name="ISDELETE")
	private Integer isDelete;
	
	@Transient
	private BigDecimal plannedBudget;
	
	@Transient
	private BigDecimal availableBudget;
	
	@Transient
	private BigDecimal usedBudget;
	
	@Transient
	private BigDecimal bookedBudget;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomorDraft() {
		return nomorDraft;
	}

	public void setNomorDraft(String nomorDraft) {
		this.nomorDraft = nomorDraft;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Date getTanggalDraft() {
		return tanggalDraft;
	}

	public void setTanggalDraft(Date tanggalDraft) {
		this.tanggalDraft = tanggalDraft;
	}

	public Organisasi getBiroPengadaan() {
		return biroPengadaan;
	}

	public void setBiroPengadaan(Organisasi biroPengadaan) {
		this.biroPengadaan = biroPengadaan;
	}

	public Integer getTahunAnggaran() {
		return tahunAnggaran;
	}

	public void setTahunAnggaran(Integer tahunAnggaran) {
		this.tahunAnggaran = tahunAnggaran;
	}

	public PeriodeAnggaran getPeriodeAnggaran() {
		return periodeAnggaran;
	}

	public void setPeriodeAnggaran(PeriodeAnggaran periodeAnggaran) {
		this.periodeAnggaran = periodeAnggaran;
	}

	public JenisAnggaran getJenisAnggaran() {
		return jenisAnggaran;
	}

	public void setJenisAnggaran(JenisAnggaran jenisAnggaran) {
		this.jenisAnggaran = jenisAnggaran;
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
	

	public MataUang getMataUang() {
		return mataUang;
	}

	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Double getSisaAnggaran() {
		return sisaAnggaran;
	}

	public void setSisaAnggaran(Double sisaAnggaran) {
		this.sisaAnggaran = sisaAnggaran;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Double getBookingAnggaran() {
		return bookingAnggaran;
	}

	public void setBookingAnggaran(Double bookingAnggaran) {
		this.bookingAnggaran = bookingAnggaran;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public BigDecimal getPlannedBudget() {
		return plannedBudget;
	}

	public void setPlannedBudget(BigDecimal plannedBudget) {
		this.plannedBudget = plannedBudget;
	}

	public BigDecimal getAvailableBudget() {
		return availableBudget;
	}

	public void setAvailableBudget(BigDecimal availableBudget) {
		this.availableBudget = availableBudget;
	}

	public BigDecimal getUsedBudget() {
		return usedBudget;
	}

	public void setUsedBudget(BigDecimal usedBudget) {
		this.usedBudget = usedBudget;
	}

	public BigDecimal getBookedBudget() {
		return bookedBudget;
	}

	public void setBookedBudget(BigDecimal bookedBudget) {
		this.bookedBudget = bookedBudget;
	}
	
}
