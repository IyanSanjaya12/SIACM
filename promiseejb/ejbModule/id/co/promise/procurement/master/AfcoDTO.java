package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Afco;

public class AfcoDTO {
	
	private Afco afco;
	
	private Integer organisasiId;

	public Afco getAfco() {
		return afco;
	}

	public void setAfco(Afco afco) {
		this.afco = afco;
	}

	public Integer getOrganisasiId() {
		return organisasiId;
	}

	public void setOrganisasiId(Integer organisasiId) {
		this.organisasiId = organisasiId;
	}
	
}
