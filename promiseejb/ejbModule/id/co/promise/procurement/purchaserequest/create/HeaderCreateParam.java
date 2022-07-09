package id.co.promise.procurement.purchaserequest.create;

import java.util.Date;

public class HeaderCreateParam {
	private String prnumber;
	private String department;
	private Double totalcost;
	private Date daterequired;
	private Integer nextapproval;
	private Integer afcoId;
	private String termandcondition;
	private Integer tahapanId = 26;
	private String description;
	private Integer approval;

	public String getPrnumber() {
		return prnumber;
	}

	public void setPrnumber(String prnumber) {
		this.prnumber = prnumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Double getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(Double totalcost) {
		this.totalcost = totalcost;
	}

	public Date getDaterequired() {
		return daterequired;
	}

	public void setDaterequired(Date daterequired) {
		this.daterequired = daterequired;
	}

	public Integer getNextapproval() {
		return nextapproval;
	}

	public void setNextapproval(Integer nextapproval) {
		this.nextapproval = nextapproval;
	}

	public Integer getAfcoId() {
		return afcoId;
	}

	public void setAfcoId(Integer afcoId) {
		this.afcoId = afcoId;
	}

	public String getTermandcondition() {
		return termandcondition;
	}

	public void setTermandcondition(String termandcondition) {
		this.termandcondition = termandcondition;
	}

	public Integer getTahapanId() {
		return tahapanId;
	}

	public void setTahapanId(Integer tahapanId) {
		this.tahapanId = tahapanId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getApproval() {
		return approval;
	}

	public void setApproval(Integer approval) {
		this.approval = approval;
	}

}
