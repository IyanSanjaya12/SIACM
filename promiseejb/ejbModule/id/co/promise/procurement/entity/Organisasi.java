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
		@NamedQuery(name = "Organisasi.find", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0"),
		@NamedQuery(name = "Organisasi.findByParentId", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND o.parentId=:parentId"),
		@NamedQuery(name = "Organisasi.findSelfAndChildByParentId", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND (o.parentId=:parentId OR o.id =:parentId)"),
		@NamedQuery(name = "Organisasi.getByNama", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND o.nama=:nama"),
		@NamedQuery(name = "Organisasi.findById", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND o.id=:id"),
		@NamedQuery(name = "Organisasi.findByNamePartial", query = "SELECT a FROM Organisasi a WHERE a.isDelete = 0 AND a.nama LIKE :name"),
		@NamedQuery(name="Organisasi.findListByOrganisasiId", query="SELECT organisasi FROM Organisasi organisasi WHERE  organisasi.isDelete=0 AND organisasi.id IN :organisasiIdList"),
		@NamedQuery(name="Organisasi.getListNotSelect", query="SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND o.parentId IN (SELECT org.id FROM Organisasi org WHERE org.parentId IS NULL) AND o.id NOT IN :selectList"),
		@NamedQuery(name = "Organisasi.findParentAfco", query = "SELECT a FROM Organisasi a WHERE a.isDelete = 0 AND a.parentId = NULL"),
		@NamedQuery(name = "Organisasi.findCodeNotNull", query = "SELECT a FROM Organisasi a WHERE a.isDelete = 0 AND a.code IS NOT NULL"),
		@NamedQuery(name = "Organisasi.findOrganisasiLevel2List", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND o.parentId IN (SELECT org.id FROM Organisasi org WHERE org.parentId IS NULL)"),
		@NamedQuery(name = "Organisasi.getByUnitIdEoffice", query = "SELECT o FROM Organisasi o WHERE o.isDelete = 0 AND o.unitIdEoffice=:unitIdEoffice"),
		@NamedQuery(name = "Organisasi.getOrganisasiListParentIdIsNull", query = "SELECT organisasi FROM Organisasi organisasi WHERE organisasi.isDelete = 0 AND organisasi.parentId = NULL ")
		
	})
@Table(name = "T1_ORGANISASI")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_ORGANISASI", initialValue = 1, allocationSize = 1)
public class Organisasi {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ORGANISASI_ID")
	private Integer id;
	
	@Column(name = "PARENT_ID")
	private Integer parentId;
	@Column(name = "NAMA")
	private String nama;
	@Column(name = "CODE")
	private String code;
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
	
	@Column(name = "UNIT_ID_EOFFICE")
	private Integer unitIdEoffice;
	
	@Column(name = "TIPE")
	private String tipe;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
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

	public Integer getUnitIdEoffice() {
		return unitIdEoffice;
	}

	public void setUnitIdEoffice(Integer unitIdEoffice) {
		this.unitIdEoffice = unitIdEoffice;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}
	
}
