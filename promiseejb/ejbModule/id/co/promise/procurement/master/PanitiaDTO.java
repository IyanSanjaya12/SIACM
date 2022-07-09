package id.co.promise.procurement.master;

import java.util.List;

import id.co.promise.procurement.entity.Panitia;
import id.co.promise.procurement.entity.TimPanitia;
import id.co.promise.procurement.entity.AnggotaPanitia;
import id.co.promise.procurement.entity.PejabatPelaksanaPengadaan;


public class PanitiaDTO {
	
	private TimPanitia timPanitia;
	private PejabatPelaksanaPengadaan pejabatPelaksanaPengadaan;
	private Panitia panitia;
	private String nama;
	private String type;
	private List <AnggotaPanitia> anggotaPanitiaList;
	
	public TimPanitia getTimPanitia() {
		return timPanitia;
	}
	public void setTimPanitia(TimPanitia timPanitia) {
		this.timPanitia = timPanitia;
	}
	public PejabatPelaksanaPengadaan getPejabatPelaksanaPengadaan() {
		return pejabatPelaksanaPengadaan;
	}
	public void setPejabatPelaksanaPengadaan(
			PejabatPelaksanaPengadaan pejabatPelaksanaPengadaan) {
		this.pejabatPelaksanaPengadaan = pejabatPelaksanaPengadaan;
	}
	public Panitia getPanitia() {
		return panitia;
	}
	public void setPanitia(Panitia panitia) {
		this.panitia = panitia;
	}
	public String getNama() {
		return nama;
	}
	public void setNama(String nama) {
		this.nama = nama;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<AnggotaPanitia> getAnggotaPanitiaList() {
		return anggotaPanitiaList;
	}
	public void setAnggotaPanitiaList(List<AnggotaPanitia> anggotaPanitiaList) {
		this.anggotaPanitiaList = anggotaPanitiaList;
	}
	
	
	
}
