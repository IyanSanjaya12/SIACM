package id.co.promise.procurement.purchaseorder.create;

public class TermAndConditionParam {
	private Integer dptype;
	private Integer termType;
	private Integer discounttype;
	private Integer taxtype;
	private Double dp;
	private Double discount;
	private Double tax;
	private String note;

	public Integer getDptype() {
		return dptype;
	}

	public void setDptype(Integer dptype) {
		this.dptype = dptype;
	}

	public Integer getTermType() {
		return termType;
	}

	public void setTermType(Integer termType) {
		this.termType = termType;
	}

	public Integer getDiscounttype() {
		return discounttype;
	}

	public void setDiscounttype(Integer discounttype) {
		this.discounttype = discounttype;
	}

	public Integer getTaxtype() {
		return taxtype;
	}

	public void setTaxtype(Integer taxtype) {
		this.taxtype = taxtype;
	}

	public Double getDp() {
		return dp;
	}

	public void setDp(Double dp) {
		this.dp = dp;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
