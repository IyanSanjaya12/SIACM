package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import id.co.promise.procurement.entity.User;

@Entity
@Table(name = "T4_CATALOG_COMMENT")
/*
 * @TableGenerator(name = "tableSequence", table = "PROMISE_SEQUENCE",
 * pkColumnName = "TABLE_SEQ_NAME", valueColumnName = "TABLE_SEQ_VALUE",
 * pkColumnValue = "T4_CATALOG_COMMENT", initialValue = 1, allocationSize = 1)
 */
public class CatalogComment implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_COMMENT_ID")
	private Integer id;
	
	@Column(name = "RATING")
	private Integer rating;
	
	@Column(name = "RATING_COMMENT", columnDefinition = "varchar(1000)", length = 1000)
	private String ratingComment;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="USER_ID")
	private User user;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED")
	private Date created;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID")
	private Catalog catalog;

	@Transient
	private String roleUser;
	
	@Transient
	private String organisasi;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getRatingComment() {
		return ratingComment;
	}

	public void setRatingComment(String ratingComment) {
		this.ratingComment = ratingComment;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public String getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(String roleUser) {
		this.roleUser = roleUser;
	}

	public String getOrganisasi() {
		return organisasi;
	}

	public void setOrganisasi(String organisasi) {
		this.organisasi = organisasi;
	}
}
