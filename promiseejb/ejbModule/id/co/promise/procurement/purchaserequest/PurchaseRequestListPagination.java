package id.co.promise.procurement.purchaserequest;

import java.util.List;

public class PurchaseRequestListPagination {
	private Integer numAllstatus;

	private Integer numNeedVerification;
	private Integer numProcurementProcess;
	private Integer numOnProcess;
	private Integer numReceived;

	private Integer totalItems;
	private Integer startIndex;
	private Integer endIndex;
	private List<PurchaseRequestDTO> listPurchaseRequestDTOs;

	public Integer getNumProcurementProcess() {
		return numProcurementProcess;
	}

	public void setNumProcurementProcess(Integer numProcurementProcess) {
		this.numProcurementProcess = numProcurementProcess;
	}

	public Integer getNumOnProcess() {
		return numOnProcess;
	}

	public void setNumOnProcess(Integer numOnProcess) {
		this.numOnProcess = numOnProcess;
	}

	public Integer getNumReceived() {
		return numReceived;
	}

	public void setNumReceived(Integer numReceived) {
		this.numReceived = numReceived;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public Integer getNumNeedVerification() {
		return numNeedVerification;
	}

	public void setNumNeedVerification(Integer numNeedVerification) {
		this.numNeedVerification = numNeedVerification;
	}

	public PurchaseRequestListPagination(int numAllstatus, int numNeedVerification, int numProcurementProcess, int numOnProcess, int numReceived, Integer totalItems, Integer startIndex,
			Integer endIndex, List<PurchaseRequestDTO> listPurchaseRequestDTOs) {
		this.numAllstatus = numAllstatus;

		this.numNeedVerification = numNeedVerification;
		this.numProcurementProcess = numProcurementProcess;
		this.numOnProcess = numOnProcess;
		this.numReceived = numReceived;

		this.totalItems = totalItems;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.listPurchaseRequestDTOs = listPurchaseRequestDTOs;
	}

	public Integer getNumAllStatus() {
		return numAllstatus;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public List<PurchaseRequestDTO> getListPurchaseRequestDTOs() {
		return listPurchaseRequestDTOs;
	}
}
