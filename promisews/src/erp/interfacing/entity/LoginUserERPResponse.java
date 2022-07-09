package erp.interfacing.entity;

import java.util.List;

public class LoginUserERPResponse {
	private String status;

	private List<LoginUserDataERP> data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<LoginUserDataERP> getData() {
		return data;
	}

	public void setData(List<LoginUserDataERP> data) {
		this.data = data;
	}
}
