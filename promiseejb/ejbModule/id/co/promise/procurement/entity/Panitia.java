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
		@NamedQuery(name = "Panitia.find", query = "SELECT panitia FROM Panitia panitia WHERE panitia.isDelete = 0")
		})
@Table(name = "T1_PANITIA")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_PANITIA", initialValue = 1, allocationSize = 1)
public class Panitia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PANITIA_ID")
	private Integer id;

	@Column(name = "BERLAKU_MULAI")
	private Date berlakuMulai;

	@Column(name = "BERLAKU_SELESAI")
	private Date berlakuSelesai;

	@OneToOne
	@JoinColumn(name = "CABANG_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi cabang;

	@OneToOne
	@JoinColumn(name = "DIVISI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi divisi;

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

	public Date getBerlakuMulai() {
		return berlakuMulai;
	}

	public void setBerlakuMulai(Date berlakuMulai) {
		this.berlakuMulai = berlakuMulai;
	}

	public Date getBerlakuSelesai() {
		return berlakuSelesai;
	}

	public void setBerlakuSelesai(Date berlakuSelesai) {
		this.berlakuSelesai = berlakuSelesai;
	}

	public Organisasi getCabang() {
		return cabang;
	}

	public void setCabang(Organisasi cabang) {
		this.cabang = cabang;
	}

	public Organisasi getDivisi() {
		return divisi;
	}

	public void setDivisi(Organisasi divisi) {
		this.divisi = divisi;
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
