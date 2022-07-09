package id.co.promise.procurement.master;

import id.co.promise.procurement.entity.Item;
import id.co.promise.procurement.entity.ItemGroup;
import id.co.promise.procurement.entity.ItemType;
import id.co.promise.procurement.entity.Satuan;

public class ItemDTO {
	private Integer id;
	private String nama;
	private String kode;
	private String deskripsi;
	private Satuan satuan;
	private ItemType itemType;
	private ItemGroup itemGroup;

	public ItemDTO(Item i) {
		this.id = i.getId();
		this.nama = i.getNama();
		this.kode = i.getKode();
		this.deskripsi = i.getDeskripsi();
		this.satuan = i.getSatuanId();
		this.itemType = i.getItemType();
		this.itemGroup = i.getItemGroupId();
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

	public String getDeskripsi() {
		return deskripsi;
	}

	public Satuan getSatuan() {
		return satuan;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public ItemGroup getItemGroup() {
		return itemGroup;
	}

}
