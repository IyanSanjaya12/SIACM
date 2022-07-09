package id.co.promise.procurement.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "T3_TOKEN_REMEMBER")
@NamedQueries({
		@NamedQuery(name = "TokenRemember.findByToken", query = "SELECT t FROM TokenRemember t WHERE t.token = :token AND t.rememberMe = 1 AND t.isDelete = 0"),
		@NamedQuery(name = "TokenRemember.findByUser", query = "SELECT t FROM TokenRemember t WHERE t.user = :userId AND t.rememberMe = 1 AND t.isDelete = 0 order by t.id desc") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T3_TOKEN_REMEMBER", initialValue = 1, allocationSize = 1)
public class TokenRemember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TOKEN_REMEMBER_ID")
	private Integer id;

	@Column(name = "TOKEN")
	private String token;

	@Column(name = "CREATED")
	private Date created;

	@Column(name = "LOGOUT")
	private Date logout;

	@OneToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
	private User user;
	
	@Column(name="UPDATED")
	private Date updated;
	
	@Column(name = "LOGIN_TIME")
	private Date loginTime;

	@Column(name="ISDELETE")
	private Integer isDelete;
	
	/* Remember User 
	 * 
	 * 1. Yes
	 * 2. No
	 *  
	 * */
	@Column(name="REMEMBER_ME")
	private Integer rememberMe;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getLogout() {
		return logout;
	}

	public void setLogout(Date logout) {
		this.logout = logout;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Integer rememberMe) {
		this.rememberMe = rememberMe;
	}

}
