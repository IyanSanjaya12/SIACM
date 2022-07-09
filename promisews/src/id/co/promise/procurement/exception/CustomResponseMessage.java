package id.co.promise.procurement.exception;

public class CustomResponseMessage {
	private String type;
	private String message;
	private Object detail;

	public CustomResponseMessage() {
	}

	public CustomResponseMessage(String message, Object object) {
		this.message = message;
		this.detail = object;
	}

	public CustomResponseMessage(String message) {
		this.message = message;
	}

	public CustomResponseMessage(Object object) {
		this.detail = object;
	}

	private void clear() {
		this.message = null;
		this.type = null;
		this.detail = null;
	}

	public CustomResponseMessage info(String message, Object object) {
		clear();
		this.type = "INFO";
		this.message = message;
		this.detail = object;
		return this;
	}

	public CustomResponseMessage info(String message) {
		clear();
		this.type = "INFO";
		this.message = message;
		return this;
	}

	public CustomResponseMessage warn(String message, Object object) {
		clear();
		this.type = "WARN";
		this.message = message;
		this.detail = object;
		return this;
	}

	public CustomResponseMessage warn(String message) {
		clear();
		this.type = "WARN";
		this.message = message;
		return this;
	}

	public CustomResponseMessage error(String message, Object object) {
		clear();
		this.type = "ERROR";
		this.message = message;
		this.detail = object;
		return this;
	}

	public CustomResponseMessage error(String message) {
		clear();
		this.type = "ERROR";
		this.message = message;
		return this;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getDetail() {
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}

}
