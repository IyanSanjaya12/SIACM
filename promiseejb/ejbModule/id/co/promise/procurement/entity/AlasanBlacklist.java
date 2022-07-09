package id.co.promise.procurement.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;

/**
 * An entity class for table T1_M_BLACKLIST, contains field definition including
 * setters and getters, make sure for table name and sequence name must have the same name, and the sequence name
 * has _SEQ in the end, for example the table name is T1_M_BLACKLIST, then the sequence name must T1_M_BLACKLIST_SEQ<br><br>
 *
 *
 * @author       : Spring MVC Code Generator 
 * @date         : 15-March-2015
 * @generator    : hardnrei
 */
@Entity
@Table(name = "T1_ALASAN_BLACKLIST")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_ALASAN_BLACKLIST", initialValue = 1, allocationSize = 1)
public class AlasanBlacklist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ALASAN_BLACKLIST_ID")
	private Integer alasanBlacklistId;
	
	@Lob 
	@Column(name = "ALASAN_BLACKLIST_KETERANGAN")
	private String alasanBlacklistKeterangan;	
	
	@Column(name = "ALASAN_BLACKLIST_JML_WAKTU")
	private Integer alasanBlacklistJmlWaktu;
	
	/**
	 * D = Days
	 * M = Months
	 * Y = Years
	 */
	
	@Column(name = "ALASAN_BLACKLIST_JK_WAKTU")
	private String alasanBlacklistJkWaktu;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ALASAN_BLACKLIST_CREATED")
	private Date alasanBlacklistCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ALASAN_BLACKLIST_UPDATED")
	private Date alasanBlacklistUpdated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ALASAN_BLACKLIST_DELETED")
	private Date alasanBlacklistDeleted;
	
	@Column(name="ALASAN_BLACKLIST_USERID")
	private Integer alasanBlacklistUserId;
	
	@ColumnDefault( value = "0" )
	@Column(name="ALASAN_BLACKLIST_ISDELETE")
	private Integer alasanBlacklistIsDelete;
	
	@Transient
	private Integer selected;
	
	@Transient
	private Integer jumlahHari;
	
	public Integer getAlasanBlacklistId() {
		return alasanBlacklistId;
	}

	public void setAlasanBlacklistId(Integer alasanBlacklistId) {
		this.alasanBlacklistId = alasanBlacklistId;
	}

	public String getAlasanBlacklistKeterangan() {
		return alasanBlacklistKeterangan;
	}

	public void setAlasanBlacklistKeterangan(String alasanBlacklistKeterangan) {
		this.alasanBlacklistKeterangan = alasanBlacklistKeterangan;
	}

	public Integer getAlasanBlacklistJmlWaktu() {
		return alasanBlacklistJmlWaktu;
	}

	public void setAlasanBlacklistJmlWaktu(Integer alasanBlacklistJmlWaktu) {
		this.alasanBlacklistJmlWaktu = alasanBlacklistJmlWaktu;
	}

	public String getAlasanBlacklistJkWaktu() {
		return alasanBlacklistJkWaktu;
	}

	public void setAlasanBlacklistJkWaktu(String alasanBlacklistJkWaktu) {
		this.alasanBlacklistJkWaktu = alasanBlacklistJkWaktu;
	}

	public Date getAlasanBlacklistCreated() {
		return alasanBlacklistCreated;
	}

	public void setAlasanBlacklistCreated(Date alasanBlacklistCreated) {
		this.alasanBlacklistCreated = alasanBlacklistCreated;
	}

	public Date getAlasanBlacklistUpdated() {
		return alasanBlacklistUpdated;
	}

	public void setAlasanBlacklistUpdated(Date alasanBlacklistUpdated) {
		this.alasanBlacklistUpdated = alasanBlacklistUpdated;
	}

	public Date getAlasanBlacklistDeleted() {
		return alasanBlacklistDeleted;
	}

	public void setAlasanBlacklistDeleted(Date alasanBlacklistDeleted) {
		this.alasanBlacklistDeleted = alasanBlacklistDeleted;
	}

	public Integer getAlasanBlacklistUserId() {
		return alasanBlacklistUserId;
	}

	public void setAlasanBlacklistUserId(Integer alasanBlacklistUserId) {
		this.alasanBlacklistUserId = alasanBlacklistUserId;
	}

	public Integer getAlasanBlacklistIsDelete() {
		return alasanBlacklistIsDelete;
	}

	public void setAlasanBlacklistIsDelete(Integer alasanBlacklistIsDelete) {
		this.alasanBlacklistIsDelete = alasanBlacklistIsDelete;
	}

	public Integer getSelected() {
		return selected;
	}

	public void setSelected(Integer selected) {
		this.selected = selected;
	}

	public Integer getJumlahHari() {
		return jumlahHari;
	}

	public void setJumlahHari(Integer jumlahHari) {
		this.jumlahHari = jumlahHari;
	}

}


