package id.co.promise.procurement.exception;

public class CustomResponse {
	private static CustomResponseMessage crm;

	public static CustomResponseMessage getCustomResponseMessage() {
		if (crm == null) {
			crm = new CustomResponseMessage();
		}
		return crm;
	}
}
