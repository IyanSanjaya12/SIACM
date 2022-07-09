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
@NamedQueries({
	@NamedQuery(name="TerminKontrak.find",
            query="SELECT tk FROM TerminKontrak tk WHERE tk.isDelete = 0"),
    @NamedQuery(name="TerminKontrak.findByKontrak",
            query="SELECT tk FROM TerminKontrak tk WHERE tk.isDelete = 0 AND tk.kontrak.id = :kontrakId")
})
@Table(name="T7_TERMIN_KONTRAK")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T7_TERMIN_KONTRAK", initialValue = 1, allocationSize = 1)
public class TerminKontrak {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TERMIN_KONTRAK_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="KONTRAK_ID", referencedColumnName="KONTRAK_ID")
	private Kontrak kontrak;
	
	@Column(name="TANGGAL")
	private Date tanggal;
	
	@ManyToOne
	@JoinColumn(name="JENIS_TERMIN_ID", referencedColumnName="JENIS_TERMIN_ID")
	private JenisTermin jenisTermin;
	
	@Column(name="KETERANGAN")
	private String keterangan;
	
	@ManyToOne
	@JoinColumn(name="MATA_UANG_ID", referencedColumnName="MATA_UANG_ID")
	private MataUang mataUang;
	
	@ColumnDefault( value = "0" )
	@Column(name="NILAI")
	private Double nilai;
	
	@ColumnDefault( value = "0" )
	@Column(name="NILAI_TUKAR")
	private Double nilaiTukar;
	
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
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Kontrak getKontrak() {
		return kontrak;
	}
	public void setKontrak(Kontrak kontrak) {
		this.kontrak = kontrak;
	}
	public Date getTanggal() {
		return tanggal;
	}
	public void setTanggal(Date tanggal) {
		this.tanggal = tanggal;
	}
	public JenisTermin getJenisTermin() {
		return jenisTermin;
	}
	public void setJenisTermin(JenisTermin jenisTermin) {
		this.jenisTermin = jenisTermin;
	}
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public MataUang getMataUang() {
		return mataUang;
	}
	public void setMataUang(MataUang mataUang) {
		this.mataUang = mataUang;
	}
	public Double getNilai() {
		return nilai;
	}
	public void setNilai(Double nilai) {
		this.nilai = nilai;
	}
	public Double getNilaiTukar() {
		return nilaiTukar;
	}
	public void setNilaiTukar(Double nilaiTukar) {
		this.nilaiTukar = nilaiTukar;
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
