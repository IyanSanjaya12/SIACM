 package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author : Reinhard MT
 */

@Entity
@NamedQueries({ @NamedQuery(name = "TableLog.getList", query = "SELECT t FROM TableLog t ") })
@Table(name = "TABLE_LOG")
public class TableLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TABLE_LOG_ID")
	private Integer id;

	@Column(name = "NAMA_KOLOM")
	private String namaKolom;

	@Column(name = "NAMA_TABLE")
	private String namaTable;

	@Column(name = "NILAI_AWAL",columnDefinition="varchar(2000)")
	@Lob
	private String nilaiAwal;

	@Column(name = "NILAI_BARU",columnDefinition="varchar(2000)")
	@Lob
	private String nilaiBaru;

	@OneToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WAKTU")
	private Date watu;

	@Column(name = "ACTION_TYPE")
	private int actionType;

	public int getActionType() {
		return actionType;
	}

	public void setActionType(int actionType) {
		this.actionType = actionType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNamaKolom() {
		return namaKolom;
	}

	public void setNamaKolom(String namaKolom) {
		this.namaKolom = namaKolom;
	}

	public String getNamaTable() {
		return namaTable;
	}

	public void setNamaTable(String namaTable) {
		this.namaTable = namaTable;
	}

	public String getNilaiAwal() {
		return nilaiAwal;
	}

	public void setNilaiAwal(String nilaiAwal) {
		this.nilaiAwal = nilaiAwal;
	}

	public String getNilaiBaru() {
		return nilaiBaru;
	}

	public void setNilaiBaru(String nilaiBaru) {
		this.nilaiBaru = nilaiBaru;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getWatu() {
		return watu;
	}

	public void setWatu(Date watu) {
		this.watu = watu;
	}

}
