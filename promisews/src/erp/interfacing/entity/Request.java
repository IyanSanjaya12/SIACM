package erp.interfacing.entity;

public class Request<T> {

	private ESBHeader esbHeader;
	
	private T esbBody;

	public ESBHeader getEsbHeader() {
		return esbHeader;
	}

	public void setEsbHeader(ESBHeader esbHeader) {
		this.esbHeader = esbHeader;
	}

	public T getEsbBody() {
		return esbBody;
	}

	public void setEsbBody(T esbBody) {
		this.esbBody = esbBody;
	}
}
