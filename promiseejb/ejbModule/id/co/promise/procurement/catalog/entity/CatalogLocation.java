package id.co.promise.procurement.catalog.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;


@Entity
@Table(name = "T4_CATALOG_LOCATION")
public class CatalogLocation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CATALOG_LOCATION_ID")
	private Integer id;
	
	@Column(name = "OFFICE_ADDRESS")
	private String officeAddress;
	
	@ColumnDefault( value = "0" )
	@Column(name = "STOCK")
	private Double stockProduct;
	
	@ColumnDefault( value = "0" )
	@Column(name = "MOQ")
	private Double minimalOrder;
	
	@ColumnDefault( value = "0" )
	@Column(name = "SUPPLY_ABILITY")
	private Double supplyAbility;
	
	@ColumnDefault( value = "0" )
	@Column(name = "OUT_STOCK")
	private Double outStock;
	
	@ColumnDefault( value = "0" )
	@Column(name = "QTY_NOTIF")
	private Double quantityNotify;
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="CATALOG_ID")
	private Catalog catalog;
	
	@Column(name = "ISDELETE", length=1)
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public Double getStockProduct() {
		return stockProduct;
	}

	public void setStockProduct(Double stockProduct) {
		this.stockProduct = stockProduct;
	}

	public Double getMinimalOrder() {
		return minimalOrder;
	}

	public void setMinimalOrder(Double minimalOrder) {
		this.minimalOrder = minimalOrder;
	}

	public Double getSupplyAbility() {
		return supplyAbility;
	}

	public void setSupplyAbility(Double supplyAbility) {
		this.supplyAbility = supplyAbility;
	}

	public Double getOutStock() {
		return outStock;
	}

	public void setOutStock(Double outStock) {
		this.outStock = outStock;
	}

	public Double getQuantityNotify() {
		return quantityNotify;
	}

	public void setQuantityNotify(Double quantityNotify) {
		this.quantityNotify = quantityNotify;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
}
