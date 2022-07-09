package id.co.promise.procurement.entity.siacm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TB_BONUS")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T2_BONUS", initialValue = 1, allocationSize = 1)
public class Bonus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BONUS_ID")
	private Integer id;
	
	@Column(name = "PERSENTASE")
	private Double persentase;
	
	@Column(name = "NILAI_BONUS")
	private Double nilaiBonus;
	
	@ManyToOne
	@JoinColumn(name = "PENJUALAN_ID", referencedColumnName = "PENJUALAN_ID")
	private Penjualan penjualan;
	
	@ManyToOne
	@JoinColumn(name = "KARYAWAN_ID", referencedColumnName = "KARYAWAN_ID")
	private Karyawan karyawan;

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

	public Karyawan getKaryawan() {
		return karyawan;
	}

	public void setKaryawan(Karyawan karyawan) {
		this.karyawan = karyawan;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPersentase() {
		return persentase;
	}

	public void setPersentase(Double persentase) {
		this.persentase = persentase;
	}

	public Penjualan getPenjualan() {
		return penjualan;
	}

	public void setPenjualan(Penjualan penjualan) {
		this.penjualan = penjualan;
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

	public Double getNilaiBonus() {
		return nilaiBonus;
	}

	public void setNilaiBonus(Double nilaiBonus) {
		this.nilaiBonus = nilaiBonus;
	}
	
	
}
