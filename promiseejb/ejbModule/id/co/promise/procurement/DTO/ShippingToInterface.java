/**
 * fdf
 */
package id.co.promise.procurement.DTO;

import java.util.Date;

public class ShippingToInterface {

	private Integer id;   
	
	/** berdasarkan termin **/
	private Date deliveryTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	

}
