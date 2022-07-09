package erp.interfacing.entity;

import java.util.List;

public class ESBBody<T> {
	
	private ESBStatus status;
	
	private Integer totalData;
	
	private List<T> body;

	public ESBStatus getStatus() {
		return status;
	}

	public void setStatus(ESBStatus status) {
		this.status = status;
	}

	public Integer getTotalData() {
		return totalData;
	}

	public void setTotalData(Integer totalData) {
		this.totalData = totalData;
	}

	public List<T> getBody() {
		return body;
	}

	public void setBody(List<T> body) {
		this.body = body;
	}
	
	
}
