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
@Table(name = "T1_TERM_AND_CONDITION")
@NamedQueries({
	@NamedQuery(name="TermAndCondition.find", query="SELECT termAndCondition FROM TermAndCondition termAndCondition WHERE termAndCondition.isDelete = 0"),
	@NamedQuery(name="TermAndConditionList.byId", query= "SELECT termAndCondition FROM TermAndCondition termAndCondition WHERE termAndCondition.isDelete = 0 And termAndCondition.id =:id")
})
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_TERM_AND_CONDITION", initialValue = 1, allocationSize = 1)
public class TermAndCondition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TERM_AND_CONDITION_ID")
	private Integer id;
	
	@Column(name = "TERM_AND_CONDITION_TYPE", length=50)
	private String termAndConditionType;
	
	@Column(name = "TERM_COUNT")
	private Integer termCount;

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

	public String getTermAndConditionType() {
		return termAndConditionType;
	}

	public void setTermAndConditionType(String termAndConditionType) {
		this.termAndConditionType = termAndConditionType;
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

	public Integer getTermCount() {
		return termCount;
	}

	public void setTermCount(Integer termCount) {
		this.termCount = termCount;
	}	
}
