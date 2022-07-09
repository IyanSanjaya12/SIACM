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
@Table(name = "T1_INDIKATOR_PENILAIAN")
@NamedQueries({
	@NamedQuery(name = "IndikatorPenilaian.findByKode",query="select indikatorPenilaian from IndikatorPenilaian indikatorPenilaian where indikatorPenilaian.iPIsDelete=0 and indikatorPenilaian.iPCode = :kode")
})

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_INDIKATOR_PENILAIAN", initialValue = 1, allocationSize = 1)
public class IndikatorPenilaian {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "INDIKATOR_P_ID")
 	private Integer iPId; 
	
	@Column(name = "INDIKATOR_P_CODE")
	private String iPCode; 
	
	@Column(name = "INDIKATOR_P_ASPEK_KERJA")
	private String iPAspekKerja;
	
	@Column(name = "INDIKATOR_P_INDIKATOR")
	private String iPIndikator;
	
	@ColumnDefault( value = "0" )
	@Column(name = "INDIKATOR_P_BOBOT")
	private Double iPBobot;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INDIKATOR_P_CREATED")
	private Date iPCreated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INDIKATOR_P_UPDATED")
	private Date iPUpdated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="INDIKATOR_P_DELETED")
	private Date iPDeleted;
	
	@Column(name="INDIKATOR_P_USERID")
	private Integer iPUserId;
	
	@ColumnDefault( value = "0" )
	@Column(name="INDIKATOR_P_ISDELETE")
	private Integer iPIsDelete;

	public Integer getiPId() {
		return iPId;
	}

	public void setiPId(Integer iPId) {
		this.iPId = iPId;
	}

	public String getiPCode() {
		return iPCode;
	}

	public void setiPCode(String iPCode) {
		this.iPCode = iPCode;
	}

	public String getiPAspekKerja() {
		return iPAspekKerja;
	}

	public void setiPAspekKerja(String iPAspekKerja) {
		this.iPAspekKerja = iPAspekKerja;
	}

	public String getiPIndikator() {
		return iPIndikator;
	}

	public void setiPIndikator(String iPIndikator) {
		this.iPIndikator = iPIndikator;
	}

	public Double getiPBobot() {
		return iPBobot;
	}

	public void setiPBobot(Double iPBobot) {
		this.iPBobot = iPBobot;
	}

	public Date getiPCreated() {
		return iPCreated;
	}

	public void setiPCreated(Date iPCreated) {
		this.iPCreated = iPCreated;
	}

	public Date getiPUpdated() {
		return iPUpdated;
	}

	public void setiPUpdated(Date iPUpdated) {
		this.iPUpdated = iPUpdated;
	}

	public Date getiPDeleted() {
		return iPDeleted;
	}

	public void setiPDeleted(Date iPDeleted) {
		this.iPDeleted = iPDeleted;
	}

	public Integer getiPUserId() {
		return iPUserId;
	}

	public void setiPUserId(Integer iPUserId) {
		this.iPUserId = iPUserId;
	}

	public Integer getiPIsDelete() {
		return iPIsDelete;
	}

	public void setiPIsDelete(Integer iPIsDelete) {
		this.iPIsDelete = iPIsDelete;
	}
	
}
