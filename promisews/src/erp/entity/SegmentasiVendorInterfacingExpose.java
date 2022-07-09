package erp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SegmentasiVendorInterfacingExpose {

	@JsonProperty("COMMODITY")
	private Integer COMMODITY;

	public Integer getCOMMODITY() {
		return COMMODITY;
	}

	public void setCOMMODITY(Integer cOMMODITY) {
		COMMODITY = cOMMODITY;
	}
	
}
