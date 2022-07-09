package id.co.promise.procurement.utils;

import java.io.Serializable;
import java.util.List;


public class JsonObject<T> implements Serializable {

	private static final long serialVersionUID = -3406769186706129994L;

	Integer recordsTotal; 
	
	Integer recordsFiltered;
	
	String draw;
	
	List<T> data;
	
	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public String getDraw() {
		return draw;
	}
	public void setDraw(String draw) {
		this.draw = draw;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	 
	
}
