package id.co.promise.procurement.purchaseorder;

import java.io.Serializable;
import java.util.List;

public class PurchaseOrderListPagination implements Serializable{
	private Integer jmlData;
	private Integer startIndex;
	private Integer endIndex;
	private List<PurchaseOrderDTO> purchaseOrderDTOList;

	public PurchaseOrderListPagination(int jmlData, Integer startIndex,
			Integer endIndex, List<PurchaseOrderDTO> purchaseOrderDTOList) {
		this.jmlData = jmlData;
		this.purchaseOrderDTOList = purchaseOrderDTOList;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		
	}

	public Integer getJmlData() {
		return jmlData;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public List<PurchaseOrderDTO> getPurchaseOrderDTOList() {
		return purchaseOrderDTOList;
	}
}
