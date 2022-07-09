package id.co.promise.procurement.modules.tablelog;

public class TableLogDTO {

	private Integer id;

	private String namaKolom;
	
	private String namaTable;
	
	private String nilaiAwal;
	
	private String nilaiBaru;
	
	private String user;
	
	private String waktu;
	
	private String actionType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNamaKolom() {
		return namaKolom;
	}

	public void setNamaKolom(String namaKolom) {
		this.namaKolom = namaKolom;
	}

	public String getNamaTable() {
		return namaTable;
	}

	public void setNamaTable(String namaTable) {
		this.namaTable = namaTable;
	}

	public String getNilaiAwal() {
		return nilaiAwal;
	}

	public void setNilaiAwal(String nilaiAwal) {
		this.nilaiAwal = nilaiAwal;
	}

	public String getNilaiBaru() {
		return nilaiBaru;
	}

	public void setNilaiBaru(String nilaiBaru) {
		this.nilaiBaru = nilaiBaru;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getWaktu() {
		return waktu;
	}

	public void setWaktu(String waktu) {
		this.waktu = waktu;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	
}
