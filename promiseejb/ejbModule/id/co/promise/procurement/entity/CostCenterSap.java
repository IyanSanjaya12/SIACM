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

@Entity
@NamedQueries({
    @NamedQuery(name="CostCenterSap.find", query="SELECT costCenter FROM CostCenterSap costCenter Where costCenter.isDelete = 0")
})
@Table(name = "T1_COSTCENTER_SAP")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_COSTCENTER_SAP", initialValue = 1, allocationSize = 1)
public class CostCenterSap {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COSTCENTER_SAP_ID")
	private Integer id;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="DESCRIPTION")
	private String description;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED")
	private Date updated;
	
	@Column(name="ISDELETE")
	private Integer isDelete;
	
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
