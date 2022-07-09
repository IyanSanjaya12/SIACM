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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
    @NamedQuery(name="AnggotaPanitia.find",
                query="SELECT x FROM AnggotaPanitia x WHERE x.isDelete = 0"),
    @NamedQuery(name="AnggotaPanitia.findByTimPanitia",
                query="SELECT x FROM AnggotaPanitia x WHERE x.isDelete = 0 AND x.timPanitia.id =:timPanitiaId"),
    @NamedQuery(name="AnggotaPanitia.findByRoleUser",
                query="SELECT x FROM AnggotaPanitia x WHERE x.isDelete = 0 AND x.pic.user.id =:userId")
})
@Table(name = "T1_ANGGOTA_PANITIA")
@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T1_ANGGOTA_PANITIA", initialValue = 1, allocationSize = 1)
public class AnggotaPanitia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ANGGOTA_PANITIA_ID")
	private Integer id;
	
	@Column(name = "KD_POSISI")
	private Integer kdPosisi;
	
	@OneToOne
	@JoinColumn(name = "PIC_ID", referencedColumnName = "ROLE_USER_ID")
	private RoleUser pic;
	
	@ManyToOne
	@JoinColumn(name = "TIM_PANITIA_ID", referencedColumnName = "TIM_PANITIA_ID")
	private TimPanitia timPanitia;
	
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
	
	@Column(name="ISDELETE")
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getKdPosisi() {
		return kdPosisi;
	}

	public void setKdPosisi(Integer kdPosisi) {
		this.kdPosisi = kdPosisi;
	}

	public RoleUser getPic() {
		return pic;
	}

	public void setPic(RoleUser pic) {
		this.pic = pic;
	}

	public TimPanitia getTimPanitia() {
		return timPanitia;
	}

	public void setTimPanitia(TimPanitia timPanitia) {
		this.timPanitia = timPanitia;
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
