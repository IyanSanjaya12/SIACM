package id.co.promise.procurement.purchaseorder.param;

import java.util.Date;
import java.util.List;

public class PurchaseOrderParam {
	private String poNumber;
	private Date poDate;
	private String prNumber;
	private Integer prId;
	private String companyName;
	private String fullName;
	private String address;
	private String telpNumber;
	private short isSaveToAddressBook;
	private short isShipToThisAddress;

	// ================================
	// Shipping to
	private String companyNameShipping;
	private String addressShipping;
	private String fullNameShipping;
	private String telpNumberShipping;
	private short isSaveToAddressBookShipping;
	private Date deliveryTime;
	private List<PurchaseOrderItemParam> purchaseOrderItemParamList;
//	private AddressBookParam addressBookParam;

	// ================================
	// term and conditions
	private Double downPayment;
//	private TermAndConditionParam termAndConditionParam;
	private Double discount;
	private Double tax;
	private String notes;

}
