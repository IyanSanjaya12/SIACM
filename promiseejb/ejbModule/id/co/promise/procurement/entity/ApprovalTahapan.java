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
@Table(name = "T4_APPROVAL_TAHAPAN")
@NamedQueries({
		@NamedQuery(name = "ApprovalTahapan.getList", query = "SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.isDelete = 0"),
		@NamedQuery(name = "ApprovalTahapan.getListApprovalByTahapan", query = "SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.tahapan = :tahapan AND approvalTahapan.isDelete = 0"),
		@NamedQuery(name = "ApprovalTahapan.getApprovalTahapanByTahapan", query = "SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.isDelete = 0 and approvalTahapan.tahapan = :tahapan"),
		@NamedQuery(name = "ApprovalTahapan.getByOrganisasiAndTahapanName", query = "SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.isDelete = 0 AND approvalTahapan.tahapan.nama = :tahapanName AND approvalTahapan.organisasi.id = :organisasiId"),
		@NamedQuery(name = "ApprovalTahapan.getApprovalTahapanByOrganisasiAndTahapan", query = "SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.isDelete = 0 AND approvalTahapan.tahapan = :tahapan AND approvalTahapan.organisasi = :organisasi"),
		@NamedQuery(name = "ApprovalTahapan.getListByOrganisasi", query = "SELECT approvalTahapan FROM ApprovalTahapan approvalTahapan WHERE approvalTahapan.isDelete = 0 AND approvalTahapan.organisasi =:organisasi")
		
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T4_APPROVAL_TAHAPAN", initialValue = 1, allocationSize = 1)
public class ApprovalTahapan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "APPROVAL_TAHAPAN_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="TAHAPAN_ID", referencedColumnName="TAHAPAN_ID")
	private Tahapan tahapan;
	
	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@ManyToOne
	@JoinColumn(name = "ORGANISASI_ID", referencedColumnName = "ORGANISASI_ID")
	private Organisasi organisasi;

	
	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Tahapan getTahapan() {
		return tahapan;
	}

	public void setTahapan(Tahapan tahapan) {
		this.tahapan = tahapan;
	}
		
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}


