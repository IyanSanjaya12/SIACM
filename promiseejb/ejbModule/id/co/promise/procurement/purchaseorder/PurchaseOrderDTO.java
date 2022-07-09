/**
 * fdf
 */
package id.co.promise.procurement.purchaseorder;

import id.co.promise.procurement.entity.PurchaseOrder;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author User
 *
 */
public class PurchaseOrderDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String poNumber;
	private String department;
	private Double totalCost;
	private String purchaseRequestNumber;
	private Date purchaseOrderDate;
	private String status;
	private Integer statusProses;
	private Integer terminFk;
	private String kodeMataUang;
	private Double kurs;

	public PurchaseOrderDTO(PurchaseOrder po,
			List<PurchaseOrderItemDTO> purchaseOrderItemDTOList) {
		this.id = po.getId();
		this.poNumber = po.getPoNumber();
		this.department = po.getDepartment();
		this.totalCost = po.getTotalCost();
		this.purchaseOrderDate = po.getPurchaseOrderDate();
		if(po.getPurchaseRequest() != null){
			this.purchaseRequestNumber = po.getPurchaseRequest().getPrnumber();
		}
		this.status = po.getStatus();
		this.statusProses = po.getStatusProses();
		this.terminFk = po.getTerminFk();
		if(po.getMataUang()!= null){
			this.kodeMataUang = po.getMataUang().getKode();
			this.kurs=po.getMataUang().getKurs();
		}
		if(po.getMataUang()== null){
			this.kodeMataUang = "IDR";
			this.kurs = 1.0;
		}
				
	}

	public Integer getTerminFk() {
		return terminFk;
	}

	public Integer getId() {
		return id;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public String getDepartment() {
		return department;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public Date getPurchaseOrderDate() {
		return purchaseOrderDate;
	}
	
	public String getKodeMataUang() {
		return kodeMataUang;
	}
	
	public Double getKurs() {
		return kurs;
	}


//	public List<PurchaseOrderItemDTO> getPurchaseOrderItemDTOList() {
//		return purchaseOrderItemDTOList;
//	}

	public String getPurchaseRequestNumber() {
		return purchaseRequestNumber;
	}

	public String getStatus() {
		return status;
	}

	public Integer getStatusProses() {
		return statusProses;
	}

	public void setStatusProses(Integer statusProses) {
		this.statusProses = statusProses;
	}

}
