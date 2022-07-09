package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.MataUang;

import javax.persistence.Column;

public class MataUangDTO {
	private Integer id;
	private String nama;
	private String kode;
	private Double kurs;

	public MataUangDTO(MataUang m) {
		this.id = m.getId();
		this.nama = m.getNama();
		this.kode = m.getKode();
		this.kurs = m.getKurs();
	}

	public Integer getId() {
		return id;
	}

	public String getNama() {
		return nama;
	}

	public String getKode() {
		return kode;
	}

	public Double getKurs() {
		return kurs;
	}

}
