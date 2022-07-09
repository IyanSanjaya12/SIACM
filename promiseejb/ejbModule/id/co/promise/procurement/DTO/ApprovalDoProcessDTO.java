package id.co.promise.procurement.DTO;

import id.co.promise.procurement.catalog.entity.Catalog;
import id.co.promise.procurement.catalog.entity.CatalogHistory;

public class ApprovalDoProcessDTO {
	
	private String todo;
	
	private Integer id;
	
	private String note;
	
	private Integer status;
	
	private Integer approvalProcessId;
	
	private Catalog catalog; 
	
	private CatalogHistory catalogHistory;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getApprovalProcessId() {
		return approvalProcessId;
	}

	public void setApprovalProcessId(Integer approvalProcessId) {
		this.approvalProcessId = approvalProcessId;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	public CatalogHistory getCatalogHistory() {
		return catalogHistory;
	}

	public void setCatalogHistory(CatalogHistory catalogHistory) {
		this.catalogHistory = catalogHistory;
	}

	public String getTodo() {
		return todo;
	}

	public void setTodo(String todo) {
		this.todo = todo;
	}
	
}
