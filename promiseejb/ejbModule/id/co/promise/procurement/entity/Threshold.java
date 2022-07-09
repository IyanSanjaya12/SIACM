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
    @NamedQuery(name="Threshold.find", query="SELECT threshold FROM Threshold threshold WHERE threshold.isDelete = 0"),
    @NamedQuery(name="Threshold.findByType", query="SELECT threshold FROM Threshold threshold WHERE threshold.isDelete = 0 And threshold.tipe =:type ")
})
@Table(name = "T1_THRESHOLD")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_THRESHOLD", initialValue = 1, allocationSize = 1)
public class Threshold {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "THRESHOLD_ID")
	private Integer id;

	@Column(name = "NAME", length = 120)
	private String name;

	@Column(name = "START_RANGE")
	private Double startRange;
	
	@Column(name = "END_RANGE")
	private Double endRange;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED")
	private Date updated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED")
	private Date deleted;

	@Column(name = "USERID")
	private Integer userId;
	
	@Column(name = "TYPE")
	private Integer tipe;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	public Integer getTipe() {
		return tipe;
	}

	public void setTipe(Integer tipe) {
		this.tipe = tipe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getStartRange() {
		return startRange;
	}

	public void setStartRange(Double startRange) {
		this.startRange = startRange;
	}

	public Double getEndRange() {
		return endRange;
	}

	public void setEndRange(Double endRange) {
		this.endRange = endRange;
	}
	
}
