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
@NamedQueries({
		@NamedQuery(name = "PejabatPelaksanaPengadaan.find", query = "SELECT x FROM PejabatPelaksanaPengadaan x WHERE x.isDelete = 0"),
		@NamedQuery(name = "PejabatPelaksanaPengadaan.findByPanitia", query = "SELECT x FROM PejabatPelaksanaPengadaan x WHERE x.isDelete = 0 AND x.panitia.id =:panitiaId"),
		@NamedQuery(name = "PejabatPelaksanaPengadaan.findByNama", query = "SELECT pejabatPelaksanaPengadaan FROM PejabatPelaksanaPengadaan pejabatPelaksanaPengadaan WHERE pejabatPelaksanaPengadaan.isDelete = 0 AND pejabatPelaksanaPengadaan.nama =:nama"),
		@NamedQuery(name = "PejabatPelaksanaPengadaan.findByRoleUser", query = "SELECT x FROM PejabatPelaksanaPengadaan x WHERE x.isDelete = 0 AND x.pic.user.id =:userId") })
@Table(name = "T2_PJBT_PLKSN_PGDN")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_PJBT_PLKSN_PGDN", initialValue = 1, allocationSize = 1)
public class PejabatPelaksanaPengadaan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PEJABAT_PELAKSANA_PENGADAAN_ID")
	private Integer id;

	@Column(name = "NAMA", length = 200)
	private String nama;

	@OneToOne
	@JoinColumn(name = "PANITIA_ID", referencedColumnName = "PANITIA_ID")
	private Panitia panitia;

	@OneToOne
	@JoinColumn(name = "PIC_ID", referencedColumnName = "ROLE_USER_ID")
	private RoleUser pic;

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

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Panitia getPanitia() {
		return panitia;
	}

	public void setPanitia(Panitia panitia) {
		this.panitia = panitia;
	}

	public RoleUser getPic() {
		return pic;
	}

	public void setPic(RoleUser pic) {
		this.pic = pic;
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
