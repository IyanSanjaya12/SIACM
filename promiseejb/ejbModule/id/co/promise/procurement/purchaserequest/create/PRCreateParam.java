package id.co.promise.procurement.purchaserequest.create;

public class PRCreateParam {
	private HeaderCreateParam header;
	private DetilCreateParam[] detil;

	public HeaderCreateParam getHeader() {
		return header;
	}

	public void setHeader(HeaderCreateParam header) {
		this.header = header;
	}

	public DetilCreateParam[] getDetil() {
		return detil;
	}

	public void setDetil(DetilCreateParam[] detil) {
		this.detil = detil;
	}

}
