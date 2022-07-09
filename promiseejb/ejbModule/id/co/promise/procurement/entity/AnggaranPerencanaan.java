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
@Table(name = "T2_ANGGARAN_P")
@NamedQueries({
		@NamedQuery(name = "AnggaranPerencanaan.find", query = "SELECT ap FROM AnggaranPerencanaan ap WHERE ap.isDelete = 0"),
		@NamedQuery(name = "AnggaranPerencanaan.findByAlokasiAnggaran", query = "SELECT ap FROM AnggaranPerencanaan ap WHERE ap.isDelete = 0 and ap.alokasiAnggaran.id=:alokasiAnggaranId")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_ANGGARAN_P", initialValue = 1, allocationSize = 1)
public class AnggaranPerencanaan {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ANGGARAN_P_ID")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "ALOKASI_ANGGARAN_ID", referencedColumnName = "ALOKASI_ANGGARAN_ID")
	private AlokasiAnggaran alokasiAnggaran;
	
	@OneToOne
	@JoinColumn(name = "PERENCANAAN_ID", referencedColumnName = "PERENCANAAN_ID")
	private Perencanaan perencanaan;
	
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AlokasiAnggaran getAlokasiAnggaran() {
		return alokasiAnggaran;
	}

	public void setAlokasiAnggaran(AlokasiAnggaran alokasiAnggaran) {
		this.alokasiAnggaran = alokasiAnggaran;
	}

	public Perencanaan getPerencanaan() {
		return perencanaan;
	}

	public void setPerencanaan(Perencanaan perencanaan) {
		this.perencanaan = perencanaan;
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
