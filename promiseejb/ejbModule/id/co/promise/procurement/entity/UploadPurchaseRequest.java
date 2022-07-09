package id.co.promise.procurement.entity;

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

import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "T4_U_PURCHASE_REQUEST")
@NamedQueries({ @NamedQuery(name = "UploadPurchaseRequest.getList", query = "SELECT ua FROM UploadPurchaseRequest ua WHERE ua.isDelete = 0  "),
		@NamedQuery(name = "UploadPurchaseRequest.getListByPurchaseRequest", query = "SELECT ua FROM UploadPurchaseRequest ua WHERE ua.isDelete = 0 and ua.purchaseRequest.id = :purchaseRequestId"),
		@NamedQuery(name = "UploadPurchaseRequest.deleteRowByByPurchaseRequest", query = "delete  FROM UploadPurchaseRequest ua  WHERE ua.purchaseRequest.id = :purchaseRequestId") })

@TableGenerator(name = "tableSequence", table = "SEQUENCE", pkColumnName = "TABLE_SEQ_NAME", 
valueColumnName = "TABLE_SEQ_VALUE", pkColumnValue = "T4_U_PURCHASE_REQUEST", initialValue = 1, allocationSize = 1)
public class UploadPurchaseRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UPLOAD_PR_ID")
	private Integer id;

	@Column(name = "UPLOAD_PR_REAL_NAME")
	private String uploadPrRealName; // nama asli file

	@Column(name = "UPLOAD_PR_FILE_NAME")
	private String uploadPrFileName; // nama file di server setelah encrypt

	@ManyToOne
	@JoinColumn(name = "PURCHASE_REQUEST_ID", referencedColumnName = "PURCHASE_REQUEST_ID")
	private PurchaseRequest purchaseRequest;

	@Column(name = "UPLOAD_PR_FILE_SIZE")
	private Long uploadPRFileSize;

	@ColumnDefault( value = "0" )
	@Column(name = "ISDELETE")
	private Integer isDelete;

	@Column(name = "USERID")
	private Integer userId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUploadPrRealName() {
		return uploadPrRealName;
	}

	public void setUploadPrRealName(String uploadPrRealName) {
		this.uploadPrRealName = uploadPrRealName;
	}

	public String getUploadPrFileName() {
		return uploadPrFileName;
	}

	public void setUploadPrFileName(String uploadPrFileName) {
		this.uploadPrFileName = uploadPrFileName;
	}

	public PurchaseRequest getPurchaseRequest() {
		return purchaseRequest;
	}

	public void setPurchaseRequest(PurchaseRequest purchaseRequest) {
		this.purchaseRequest = purchaseRequest;
	}

	public Long getUploadPRFileSize() {
		return uploadPRFileSize;
	}

	public void setUploadPRFileSize(Long uploadPRFileSize) {
		this.uploadPRFileSize = uploadPRFileSize;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

}
