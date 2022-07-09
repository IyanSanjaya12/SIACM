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

import org.hibernate.annotations.ColumnDefault;

@Entity
@NamedQueries({
	@NamedQuery(name="Wilayah.findAll",
			query="SELECT w FROM Wilayah w WHERE w.isDelete = 0 ORDER BY w.lokasi_nama"),
	@NamedQuery(name="Wilayah.findPropinsi",
			query="SELECT w FROM Wilayah w WHERE w.lokasi_kabupatenkota=0 AND w.lokasi_kecamatan=0 AND w.lokasi_kelurahan=0 "
				+ "AND w.isDelete = 0 ORDER BY w.lokasi_nama"),
	@NamedQuery(name="Wilayah.findKota",
			query="SELECT w FROM Wilayah w WHERE w.lokasi_kabupatenkota!=0 AND w.lokasi_kecamatan=0 AND w.lokasi_kelurahan=0 "
				+ "AND w.lokasi_propinsi=:propinsiId AND w.isDelete = 0 ORDER BY w.lokasi_nama"),
	@NamedQuery(name="Wilayah.findPropinsiByNama",
						query="SELECT w FROM Wilayah w WHERE w.lokasi_kabupatenkota=0 AND w.lokasi_kecamatan=0 AND w.lokasi_kelurahan=0 "
							+ "AND w.lokasi_nama=:nama AND w.isDelete = 0 ORDER BY w.lokasi_nama"),
	@NamedQuery(name="Wilayah.findKotaByNama",
						query="SELECT w FROM Wilayah w WHERE w.lokasi_kabupatenkota!=0 AND w.lokasi_kecamatan=0 AND w.lokasi_kelurahan=0 "
							+ "AND w.lokasi_nama=:nama AND w.isDelete = 0 ORDER BY w.lokasi_nama")	
})
@Table(name="T1_WILAYAH")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_WILAYAH", initialValue = 1, allocationSize = 1)
public class Wilayah {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LOKASI_ID")
	private Integer id;
	
	@Column(name = "LOKASI_KODE", length=50)
	private String lokasi_kode;
	
	@Column(name = "LOKASI_NAMA", length=150)
	private String lokasi_nama;
	
	@ColumnDefault( value = "0" )
	@Column(name = "LOKASI_PROPINSI")
	private Integer lokasi_propinsi;
	
	@ColumnDefault( value = "0" )
	@Column(name = "LOKASI_KABUPATENKOTA")
	private Integer lokasi_kabupatenkota;
	
	@ColumnDefault( value = "0" )
	@Column(name = "LOKASI_KECAMATAN")
	private Integer lokasi_kecamatan;
	
	@ColumnDefault( value = "0" )
	@Column(name = "LOKASI_KELURAHAN")
	private Integer lokasi_kelurahan;
	
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

	public String getLokasi_kode() {
		return lokasi_kode;
	}

	public void setLokasi_kode(String lokasi_kode) {
		this.lokasi_kode = lokasi_kode;
	}

	public String getLokasi_nama() {
		return lokasi_nama;
	}

	public void setLokasi_nama(String lokasi_nama) {
		this.lokasi_nama = lokasi_nama;
	}

	public Integer getLokasi_propinsi() {
		return lokasi_propinsi;
	}

	public void setLokasi_propinsi(Integer lokasi_propinsi) {
		this.lokasi_propinsi = lokasi_propinsi;
	}

	public Integer getLokasi_kabupatenkota() {
		return lokasi_kabupatenkota;
	}

	public void setLokasi_kabupatenkota(Integer lokasi_kabupatenkota) {
		this.lokasi_kabupatenkota = lokasi_kabupatenkota;
	}

	public Integer getLokasi_kecamatan() {
		return lokasi_kecamatan;
	}

	public void setLokasi_kecamatan(Integer lokasi_kecamatan) {
		this.lokasi_kecamatan = lokasi_kecamatan;
	}

	public Integer getLokasi_kelurahan() {
		return lokasi_kelurahan;
	}

	public void setLokasi_kelurahan(Integer lokasi_kelurahan) {
		this.lokasi_kelurahan = lokasi_kelurahan;
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
