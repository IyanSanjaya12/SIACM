package id.co.promise.procurement.DTO;


public class ContractRequestDetailMaterial {
	
private Integer reqDetItemPk;
	
	private Integer  reqDetItemReqFk;
	
	private String  reqDetItemNama;
	
	private String  reqDetItemJns;

	private Double  reqDetItemJumlah;

	private Double  reqDetItemHargaSatuan;

	private Double  reqDetItemHargaTotal;

	private Integer  reqDetItemIsDeleted;

	private Integer  reqDetItemProcGrpFK;

	private Integer  reqDetItemReqPTermFK;
	
	private Integer  reqDetItemPrItemFk;
	
	//private ProcurementGroup procurementGroup;
	
	//private ContractRequestPaymentTerms contractRequestPaymentTerms;
	
	private Integer position;
	
	private Double totalMaterial;
	
	private Double totalMaterialSebelumBayar;
	
	private Integer positionIndex;
	
	private Integer satuanFk;
	
	public Integer getSatuanFk() {
		return satuanFk;
	}

	public void setSatuanFk(Integer satuanFk) {
		this.satuanFk = satuanFk;
	}

	public Integer getReqDetItemPk() {
		return reqDetItemPk;
	}

	public void setReqDetItemPk(Integer reqDetItemPk) {
		this.reqDetItemPk = reqDetItemPk;
	}

	public Integer getReqDetItemReqFk() {
		return reqDetItemReqFk;
	}

	public void setReqDetItemReqFk(Integer reqDetItemReqFk) {
		this.reqDetItemReqFk = reqDetItemReqFk;
	}

	public String getReqDetItemNama() {
		return reqDetItemNama;
	}

	public void setReqDetItemNama(String reqDetItemNama) {
		this.reqDetItemNama = reqDetItemNama;
	}

	public String getReqDetItemJns() {
		return reqDetItemJns;
	}

	public void setReqDetItemJns(String reqDetItemJns) {
		this.reqDetItemJns = reqDetItemJns;
	}

	public Double getReqDetItemJumlah() {
		return reqDetItemJumlah;
	}

	public void setReqDetItemJumlah(Double reqDetItemJumlah) {
		this.reqDetItemJumlah = reqDetItemJumlah;
	}

	public Double getReqDetItemHargaSatuan() {
		return reqDetItemHargaSatuan;
	}

	public void setReqDetItemHargaSatuan(Double reqDetItemHargaSatuan) {
		this.reqDetItemHargaSatuan = reqDetItemHargaSatuan;
	}

	public Double getReqDetItemHargaTotal() {
		return reqDetItemHargaTotal;
	}

	public void setReqDetItemHargaTotal(Double reqDetItemHargaTotal) {
		this.reqDetItemHargaTotal = reqDetItemHargaTotal;
	}

	public Integer getReqDetItemIsDeleted() {
		return reqDetItemIsDeleted;
	}

	public void setReqDetItemIsDeleted(Integer reqDetItemIsDeleted) {
		this.reqDetItemIsDeleted = reqDetItemIsDeleted;
	}

	public Integer getReqDetItemProcGrpFK() {
		return reqDetItemProcGrpFK;
	}

	public void setReqDetItemProcGrpFK(Integer reqDetItemProcGrpFK) {
		this.reqDetItemProcGrpFK = reqDetItemProcGrpFK;
	}

	public Integer getReqDetItemReqPTermFK() {
		return reqDetItemReqPTermFK;
	}

	public void setReqDetItemReqPTermFK(Integer reqDetItemReqPTermFK) {
		this.reqDetItemReqPTermFK = reqDetItemReqPTermFK;
	}

	public Integer getReqDetItemPrItemFk() {
		return reqDetItemPrItemFk;
	}

	public void setReqDetItemPrItemFk(Integer reqDetItemPrItemFk) {
		this.reqDetItemPrItemFk = reqDetItemPrItemFk;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Double getTotalMaterial() {
		return totalMaterial;
	}

	public void setTotalMaterial(Double totalMaterial) {
		this.totalMaterial = totalMaterial;
	}

	public Double getTotalMaterialSebelumBayar() {
		return totalMaterialSebelumBayar;
	}

	public void setTotalMaterialSebelumBayar(Double totalMaterialSebelumBayar) {
		this.totalMaterialSebelumBayar = totalMaterialSebelumBayar;
	}

	public Integer getPositionIndex() {
		return positionIndex;
	}

	public void setPositionIndex(Integer positionIndex) {
		this.positionIndex = positionIndex;
	}
}
