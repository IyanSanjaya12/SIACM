package id.co.promise.procurement.DTO;

import java.util.Date;

public class ContractRequestDetail {
	
	private Integer reqDetPk;
	
	private Integer  reqDetReqFk;
	
	private String  reqDetNo;
	
	private Integer  reqDetCreqTypeFk;

	private String  reqDetNotes;

	private String  reqDetNotaIntern;

	private String  reqDetNmPengadaan;

	private String  reqDetJnsPengadaan;

	private String  reqDetAnggaran;

	private String  reqDetUser;
	
	private String  reqDetNoSpp;
	
	private Date  reqDetTglSpp;
	
	private String  reqDetNoSpk;
	
	private Date reqDetTglSpk;
	
	private String  reqDetJudulSpk;
	
	private Integer  reqDetIsDeleted;
	
	//private CReqType cReqType;
	
	private Double reqDetTotalPengadaan;

	public Integer getReqDetPk() {
		return reqDetPk;
	}

	public void setReqDetPk(Integer reqDetPk) {
		this.reqDetPk = reqDetPk;
	}

	public Integer getReqDetReqFk() {
		return reqDetReqFk;
	}

	public void setReqDetReqFk(Integer reqDetReqFk) {
		this.reqDetReqFk = reqDetReqFk;
	}

	public String getReqDetNo() {
		return reqDetNo;
	}

	public void setReqDetNo(String reqDetNo) {
		this.reqDetNo = reqDetNo;
	}

	public Integer getReqDetCreqTypeFk() {
		return reqDetCreqTypeFk;
	}

	public void setReqDetCreqTypeFk(Integer reqDetCreqTypeFk) {
		this.reqDetCreqTypeFk = reqDetCreqTypeFk;
	}

	public String getReqDetNotes() {
		return reqDetNotes;
	}

	public void setReqDetNotes(String reqDetNotes) {
		this.reqDetNotes = reqDetNotes;
	}

	public String getReqDetNotaIntern() {
		return reqDetNotaIntern;
	}

	public void setReqDetNotaIntern(String reqDetNotaIntern) {
		this.reqDetNotaIntern = reqDetNotaIntern;
	}

	public String getReqDetNmPengadaan() {
		return reqDetNmPengadaan;
	}

	public void setReqDetNmPengadaan(String reqDetNmPengadaan) {
		this.reqDetNmPengadaan = reqDetNmPengadaan;
	}

	public String getReqDetJnsPengadaan() {
		return reqDetJnsPengadaan;
	}

	public void setReqDetJnsPengadaan(String reqDetJnsPengadaan) {
		this.reqDetJnsPengadaan = reqDetJnsPengadaan;
	}

	public String getReqDetAnggaran() {
		return reqDetAnggaran;
	}

	public void setReqDetAnggaran(String reqDetAnggaran) {
		this.reqDetAnggaran = reqDetAnggaran;
	}

	public String getReqDetUser() {
		return reqDetUser;
	}

	public void setReqDetUser(String reqDetUser) {
		this.reqDetUser = reqDetUser;
	}

	public String getReqDetNoSpp() {
		return reqDetNoSpp;
	}

	public void setReqDetNoSpp(String reqDetNoSpp) {
		this.reqDetNoSpp = reqDetNoSpp;
	}

	public Date getReqDetTglSpp() {
		return reqDetTglSpp;
	}

	public void setReqDetTglSpp(Date reqDetTglSpp) {
		this.reqDetTglSpp = reqDetTglSpp;
	}

	public String getReqDetNoSpk() {
		return reqDetNoSpk;
	}

	public void setReqDetNoSpk(String reqDetNoSpk) {
		this.reqDetNoSpk = reqDetNoSpk;
	}

	public Date getReqDetTglSpk() {
		return reqDetTglSpk;
	}

	public void setReqDetTglSpk(Date reqDetTglSpk) {
		this.reqDetTglSpk = reqDetTglSpk;
	}

	public String getReqDetJudulSpk() {
		return reqDetJudulSpk;
	}

	public void setReqDetJudulSpk(String reqDetJudulSpk) {
		this.reqDetJudulSpk = reqDetJudulSpk;
	}

	public Integer getReqDetIsDeleted() {
		return reqDetIsDeleted;
	}

	public void setReqDetIsDeleted(Integer reqDetIsDeleted) {
		this.reqDetIsDeleted = reqDetIsDeleted;
	}

	public Double getReqDetTotalPengadaan() {
		return reqDetTotalPengadaan;
	}

	public void setReqDetTotalPengadaan(Double reqDetTotalPengadaan) {
		this.reqDetTotalPengadaan = reqDetTotalPengadaan;
	}

}
