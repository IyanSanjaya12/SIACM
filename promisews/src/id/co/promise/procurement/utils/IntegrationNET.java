package id.co.promise.procurement.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.promise.procurement.DTO.FormBillingRequest;

public class IntegrationNET {

	private final static String USER_AGENT = "Mozilla/5.0";

	public static Boolean createBillingMemoRequest(FormBillingRequest formBillingRequest) {
		try {

			/** set url integration **/
			String urlCM 		  = ParamContext.getParameterStringByName("CM_WS_URL");
			String url   		  = "/billingMemoRequest/createBillingMemoRequest";
			String newUrl 		  = urlCM + url;
			URL obj 			  = new URL(newUrl);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			/** optional default is GET/POST */
			con.setRequestMethod(Constant.NET_POST_URL);
			con.setDoOutput(true);

			/** set request header **/
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty(Constant.SERVICES_USER, KeyGenHelper.encrypt(Constant.ENCRYPTION_KEY_FOR_PASS, "ProMIS3CM"));
			con.setRequestProperty(Constant.SERVICES_PASSWORD, KeyGenHelper.encrypt(Constant.ENCRYPTION_KEY_FOR_PASS, "Astr0b3ans"));

			/** set Object to Json with custom format date **/
			Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
			String json = gson.toJson(formBillingRequest);
			

			/** set json to byte[] **/
			OutputStream os = con.getOutputStream();
			os.write(json.getBytes());
			os.flush();

			/** get result from CM **/
			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

			FormBillingRequest formBillingRequestResponse = gson.fromJson(response.toString(), FormBillingRequest.class);
			
			if (formBillingRequestResponse.getBillingMemoRequest().getBillMemoReqPk() == null) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
