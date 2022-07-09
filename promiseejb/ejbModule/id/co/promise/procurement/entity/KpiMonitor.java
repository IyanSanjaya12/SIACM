package id.co.promise.procurement.entity;

import java.util.List;
import java.util.Map;


public class KpiMonitor {

	private Integer departementId;
	
	private String departementName;
	
	private long onProgress;
	
	private long pending;
	
	private long done;
	
	private long total;
	
	private List<Map<String, Object>> lisMap;
	
	public Integer getDepartementId() {
		return departementId;
	}

	public void setDepartementId(Integer departementId) {
		this.departementId = departementId;
	}

	public String getDepartementName() {
		return departementName;
	}

	public void setDepartementName(String departementName) {
		this.departementName = departementName;
	}

	public long getOnProgress() {
		return onProgress;
	}

	public void setOnProgress(long onProgress) {
		this.onProgress = onProgress;
	}

	public long getPending() {
		return pending;
	}

	public void setPending(long pending) {
		this.pending = pending;
	}

	public long getDone() {
		return done;
	}

	public void setDone(long done) {
		this.done = done;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<Map<String, Object>> getLisMap() {
		return lisMap;
	}

	public void setLisMap(List<Map<String, Object>> lisMap) {
		this.lisMap = lisMap;
	}
	
}
