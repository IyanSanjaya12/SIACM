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
@NamedQueries({
    @NamedQuery(name="DokumenPenawaranTeknis.find",
            query="SELECT dpt FROM DokumenPenawaranTeknis dpt WHERE dpt.isDelete = 0"),
    @NamedQuery(name="DokumenPenawaranTeknis.findByVendorIdAndPengadaanId",
        	query="SELECT dpa FROM DokumenPenawaranTeknis dpa WHERE dpa.isDelete=0 AND dpa.suratPenawaran.vendor.id=:vendorId AND dpa.suratPenawaran.pengadaan.id=:pengadaanId")
})
@Table(name = "T6_DOK_P_TEKNIS")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T6_DOK_P_TEKNIS", initialValue = 1, allocationSize = 1)
public class DokumenPenawaranTeknis {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DOKUMEN_PENAWARAN_TEKNIS_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "SURAT_PENAWARAN_ID", referencedColumnName = "SURAT_PENAWARAN_ID")
	private SuratPenawaran suratPenawaran;
	
	@Column(name = "REAL_FILE_NAME", length = 250)
	private String realFileName;	

	@Column(name = "FILE_NAME", length = 250)
	private String fileName;
	
	
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
	
	@ColumnDefault( value = "0" )
	@Column(name="ISDELETE")
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SuratPenawaran getSuratPenawaran() {
		return suratPenawaran;
	}

	public void setSuratPenawaran(SuratPenawaran suratPenawaran) {
		this.suratPenawaran = suratPenawaran;
	}

	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
