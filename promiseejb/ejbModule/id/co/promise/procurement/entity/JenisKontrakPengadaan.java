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
@Table(name = "T5_JENIS_K_PENGADAAN")
@NamedQueries({ @NamedQuery(name = "JenisKontrakPengadaan.findAll", query = "SELECT o FROM JenisKontrakPengadaan o WHERE o.isDelete = 0"),
				@NamedQuery(name = "JenisKontrakPengadaan.findByPengadaan", query = "SELECT o FROM JenisKontrakPengadaan o WHERE o.isDelete = 0 AND o.pengadaan.id=:pengadaanId") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T5_JENIS_K_PENGADAAN", initialValue = 1, allocationSize = 1)
public class JenisKontrakPengadaan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "JENIS_KONTRAK_PENGADAAN_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="PENGADAAN_ID", referencedColumnName="PENGADAAN_ID")
	private Pengadaan pengadaan;
	@ManyToOne
	@JoinColumn(name="JENIS_KONTRAK_ID", referencedColumnName="JENIS_KONTRAK_ID")
	private JenisKontrak jenisKontrak;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
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
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Pengadaan getPengadaan() {
		return pengadaan;
	}
	public void setPengadaan(Pengadaan pengadaan) {
		this.pengadaan = pengadaan;
	}
	public JenisKontrak getJenisKontrak() {
		return jenisKontrak;
	}
	public void setJenisKontrak(JenisKontrak jenisKontrak) {
		this.jenisKontrak = jenisKontrak;
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
