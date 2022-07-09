package id.co.promise.procurement.entity.api;

public class Response<T> {
	ResponseStatus status = ResponseStatus.OK;
	String statusText = null;
	Integer totalData;
	T body;

	public static <T> Response<T> ok(){
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.OK);
		return response;
	}

	public static <T> Response<T> ok(String statusText){
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.OK);
		response.setStatusText(statusText);
		return response;
	}
	
	public static <T> Response<T> ok(T T) {
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.OK);
		response.setT(T);
		return response;
	}

	public static <T> Response<T> ok(T T,Integer totalData) {
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.OK);
		response.setT(T);
		response.setTotalData(totalData);
		return response;
	}
	
	public static <T> Response<T> error(String statusText) {
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.ERROR);
		response.setStatusText(statusText);
		return response;
	}

	public static <T> Response<T> error(String statusText, T T) {
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.ERROR);
		response.setT(T);
		response.setStatusText(statusText);
		return response;
	}

	public static <T> Response<T> error(Exception e) {
		String errorText = e.getMessage() != null ? e.getMessage().toString() : "no message found";
		String causeText = e.getCause() != null ? e.getCause().toString() : "no cause found" ;
		
		String statusText = "Error : " + (errorText.length() > 200 ? errorText.substring(0, 200) : errorText)  + "; "
				+ "Cause : " + (causeText.length() > 200 ? causeText.substring(0, 200) : causeText);
		
		Response<T> response = new Response<T>();
		response.setStatus(ResponseStatus.ERROR);
		response.setStatusText(statusText);
		return response;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public T getBody() {
		return body;
	}

	public void setT(T body) {
		this.body = body;
	}

	public Integer getTotalData() {
		return totalData;
	}

	public void setTotalData(Integer totalData) {
		this.totalData = totalData;
	}

}
