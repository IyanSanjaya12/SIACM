package id.co.promise.procurement.purchaserequest;

import id.co.promise.procurement.entity.Organisasi;
import id.co.promise.procurement.entity.PurchaseRequest;

import java.util.Date;

public class PurchaseRequestDTO {
	private Integer id;
	private String termandcondition;
	private Organisasi organisasi;
	private String department;
	private String prnumber;
	private String costcenter;
	private String totalcost;
	private Date daterequired;
	private Date postdate;
	private String nextapproval;
	private String procurementprocess;
	private String description;
	private Integer status;
	private Integer isJoin;
	private Integer pengadaanId;

	public PurchaseRequestDTO(PurchaseRequest pr) {
		this.id = pr.getId();
		this.termandcondition = pr.getTermandcondition();
		this.organisasi = pr.getOrganisasi();
		this.department = pr.getDepartment();
		this.prnumber = pr.getPrnumber();
		this.costcenter = pr.getCostcenter();
		//this.totalcost = pr.getTotalcost();
		this.daterequired = pr.getDaterequired();
		this.postdate = pr.getPostdate();
		this.nextapproval = pr.getNextapproval();
		this.procurementprocess = pr.getProcurementprocess();
		this.description = pr.getDescription();
		this.status = pr.getStatus();
		this.isJoin = pr.getIsJoin();
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setTermandcondition(String termandcondition) {
		this.termandcondition = termandcondition;
	}

	public void setOrganisasi(Organisasi organisasi) {
		this.organisasi = organisasi;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setPrnumber(String prnumber) {
		this.prnumber = prnumber;
	}

	public void setCostcenter(String costcenter) {
		this.costcenter = costcenter;
	}

	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}

	public void setDaterequired(Date daterequired) {
		this.daterequired = daterequired;
	}

	public void setPostdate(Date postdate) {
		this.postdate = postdate;
	}

	public void setNextapproval(String nextapproval) {
		this.nextapproval = nextapproval;
	}

	public void setProcurementprocess(String procurementprocess) {
		this.procurementprocess = procurementprocess;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPengadaanId() {
		return pengadaanId;
	}

	public void setPengadaanId(Integer pengadaanId) {
		this.pengadaanId = pengadaanId;
	}

	public Integer getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
	}

	public Integer getId() {
		return id;
	}

	public String getTermandcondition() {
		return termandcondition;
	}

	public Organisasi getOrganisasi() {
		return organisasi;
	}

	public String getDepartment() {
		return department;
	}

	public String getPrnumber() {
		return prnumber;
	}

	public String getCostcenter() {
		return costcenter;
	}

	public String getTotalcost() {
		return totalcost;
	}

	public Date getDaterequired() {
		return daterequired;
	}

	public Date getPostdate() {
		return postdate;
	}

	public String getNextapproval() {
		return nextapproval;
	}

	public String getProcurementprocess() {
		return procurementprocess;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
