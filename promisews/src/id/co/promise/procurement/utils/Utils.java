package id.co.promise.procurement.utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.security.Base64Encoder;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import erp.interfacing.entity.ESBHeader;
import erp.interfacing.entity.Request;

/*RMT*/

import id.co.promise.procurement.entity.HariLibur;
import id.co.promise.procurement.entity.api.ResponseObject;
import id.co.promise.procurement.master.HariLiburSession;

public class Utils {
	
	private static Log logger = LogFactory.getLog(Utils.class);

	public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate, HariLiburSession hariLiburSession) {
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		int workDays = 0;

		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}

		List<HariLibur> hariLiburList = hariLiburSession.getHariLiburBetWeenDates(startDate, endDate);
		Integer holidays = hariLiburList.size();

		for (HariLibur hariLibur : hariLiburList) {
			Calendar libur = Calendar.getInstance();
			libur.setTime(hariLibur.getTanggal());

			if (libur.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || libur.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				holidays--;
			}
		}
		do {
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		} while (startCal.getTimeInMillis() <= endCal.getTimeInMillis());

		workDays = workDays - holidays;

		return workDays;
	}
	
	public static ResponseObject doPostJSONString (Object object , String url) throws ClientProtocolException, IOException {
		ResponseObject responseObj = new ResponseObject();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		Gson gson = new Gson();
		String req = gson.toJson(object);
		HttpPost request = new HttpPost(url);
		StringEntity params = new StringEntity(req);
		request.addHeader("content-type", "application/json");
		request.setEntity(params);
		HttpResponse response = httpClient.execute(request);
		Integer status = response.getStatusLine().getStatusCode();
		// Getting the response body.
		String responseString = EntityUtils.toString(response.getEntity());
		responseObj.setResponseString(responseString);
		responseObj.setStatusCode(status.toString());
		return responseObj;
	}
	
	public static ResponseObject doGet ( String url) throws ClientProtocolException, IOException {
		ResponseObject responseObj = new ResponseObject();
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = httpClient.execute(request);
		Integer status = response.getStatusLine().getStatusCode();
		// Getting the response body.
		String responseString = EntityUtils.toString(response.getEntity());
		responseObj.setResponseString(responseString);
		responseObj.setStatusCode(status.toString());
		return responseObj;
	}
	
	public static ResponseObject doPost(Object object, String url){
		String encoding;
		ResponseObject responseObj = new ResponseObject();
		Integer status = 0;
		String externalId = "0";
		try {
			String user = Constant.AUTH_INTERFACING_SAG_USERNAME;
			String pass = Constant.AUTH_INTERFACING_SAG_PASSWORD;
			encoding = Base64Encoder.encode(user + ":" + pass);
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			String[] urlList = url.split("/");
			String methodName = urlList[urlList.length-1];
			
			//Build Request
			Request<Object> request = new Request<Object>();
			ESBHeader esbHeader = new ESBHeader();
			externalId = KeyGenHelper.finalEncrypt(timestamp.toString());
			esbHeader.setExternalId(externalId);
			esbHeader.setTimestamp(formatter.format(timestamp));
			request.setEsbHeader(esbHeader);
			if(object != null) {
				request.setEsbBody(object);
			}
			Gson gson = new Gson();
			String requestString = gson.toJson(request);
			JSONObject jsonObjData = new JSONObject(requestString);
			JSONObject jsonObjFull = new JSONObject();
			jsonObjFull.put(methodName+"Request", jsonObjData);
			
			//Send request and getting response
			HttpPost requestPost = new HttpPost(url);
			//Authorization API
			requestPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
			StringEntity params = new StringEntity(jsonObjFull.toString());
			requestPost.addHeader("content-type", "application/json");
			requestPost.setEntity(params);
			HttpResponse response = httpClient.execute(requestPost);
			status = response.getStatusLine().getStatusCode();
			
			// Getting the response body.
			String responseString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = new JSONObject(responseString);
			Object jsonBody = jsonObj.get(methodName+"Response");
			JSONObject jsonObjEsbBody = new JSONObject(jsonBody.toString());
			Object jsonBodyEsbBody = jsonObjEsbBody.get("esbBody");
			JSONObject jsonObjResult = new JSONObject(jsonBodyEsbBody.toString());
			responseObj.setResponseString(jsonObjResult.toString());
			responseObj.setStatusCode(status.toString());
			responseObj.setExternalId(externalId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseObj.setStatusCode(status.toString());
			responseObj.setExternalId(externalId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		return responseObj;
	}
	
	public static ResponseObject doPost(String url){
		String encoding;
		ResponseObject responseObj = new ResponseObject();
		Integer status = 0;
		String externalId = "0";
		try {
			String user = Constant.AUTH_INTERFACING_SAG_USERNAME;
			String pass = Constant.AUTH_INTERFACING_SAG_PASSWORD;
			encoding = Base64Encoder.encode(user + ":" + pass);
			
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			String[] urlList = url.split("/");
			String methodName = urlList[urlList.length-1];
			
			//Build Request
			Request<Object> request = new Request<Object>();
			ESBHeader esbHeader = new ESBHeader();
			externalId = KeyGenHelper.finalEncrypt(timestamp.toString());
			esbHeader.setExternalId(externalId);
			esbHeader.setTimestamp(formatter.format(timestamp));
			request.setEsbHeader(esbHeader);
			Gson gson = new Gson();
			String requestString = gson.toJson(request);
			JSONObject jsonObjData = new JSONObject(requestString);
			JSONObject jsonObjFull = new JSONObject();
			jsonObjFull.put(methodName+"Request", jsonObjData);
			
			//Send request and getting response
			HttpPost requestPost = new HttpPost(url);
			//Authorization API
			requestPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
			StringEntity params = new StringEntity(jsonObjFull.toString());
			requestPost.addHeader("content-type", "application/json");
			requestPost.setEntity(params);
			HttpResponse response = httpClient.execute(requestPost);
			status = response.getStatusLine().getStatusCode();
			
			// Getting the response body.
			String responseString = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = new JSONObject(responseString);
			Object jsonBody = jsonObj.get(methodName+"Response");
			JSONObject jsonObjEsbBody = new JSONObject(jsonBody.toString());
			Object jsonBodyEsbBody = jsonObjEsbBody.get("esbBody");
			JSONObject jsonObjResult = new JSONObject(jsonBodyEsbBody.toString());
			responseObj.setResponseString(jsonObjResult.toString());
			responseObj.setStatusCode(status.toString());
			responseObj.setExternalId(externalId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseObj.setStatusCode(status.toString());
			responseObj.setExternalId(externalId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		return responseObj;
	}
	static class MyAuthenticator extends Authenticator {
		String SAP_USERNAME = "";
		String SAP_PASSWORD = "";
		
		public PasswordAuthentication getPasswordAuthentication() {
			if(Constant.IS_DEVELOPMENT_MODE) {
				SAP_USERNAME = Constant.AUTH_INTERFACING_SAP_DEV_USERNAME;
				SAP_PASSWORD = Constant.AUTH_INTERFACING_SAP_DEV_PASSWORD;
			}else {
				SAP_USERNAME = Constant.AUTH_INTERFACING_SAP_PROD_USERNAME;
				SAP_PASSWORD = Constant.AUTH_INTERFACING_SAP_PROD_PASSWORD;
			}
			return (new PasswordAuthentication( SAP_USERNAME, SAP_PASSWORD.toCharArray()));
		}
	}
	
	public static ResponseObject doGetWSDL(Class objEntity, Class obj, Class objResponse, Object dataCreation, Class datacreationbankoutClass) throws IOException{
		ResponseObject responseObj = new ResponseObject();
		Integer status = 0;
		String externalId = "0";
		try{
			
			
			
			 Object getObjResponse = objEntity.newInstance();
			 Object getObjEntity = objEntity.newInstance();
			 Object getObj= obj.newInstance();

	//		 getObjResponse	= ((Class) dataCreation).dataCreation(getObjEntity);
		
		
		/** message return ? Error! **/
		//if (!gvtxtreturn.equalsIgnoreCase("Success get data BANK!")){
		//	return messagesError("Error Data Bank, Please Contact Our Administrator");
		//}
		
		logger.info("########################################################");
		logger.info("####################### GET BANK #########################");
		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		return responseObj;
	}
	
	
	public static void connectionSAP(Object dataCreation) throws IOException{
		String SAP_USERNAME = "";
		String SAP_PASSWORD = "";
		if(Constant.IS_DEVELOPMENT_MODE) {
			SAP_USERNAME = Constant.AUTH_INTERFACING_SAP_DEV_USERNAME;
			SAP_PASSWORD = Constant.AUTH_INTERFACING_SAP_DEV_PASSWORD;
		}else {
			SAP_USERNAME = Constant.AUTH_INTERFACING_SAP_PROD_USERNAME;
			SAP_PASSWORD = Constant.AUTH_INTERFACING_SAP_PROD_PASSWORD;
		}
		Authenticator.setDefault(new MyAuthenticator());
		
		BindingProvider prov = (BindingProvider)dataCreation;
		prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, SAP_USERNAME /*Constant.AUTH_INTERFACING_SAP_USERNAME*/);//hardcode dlu karena masih suka berubah
		prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, SAP_PASSWORD /* Constant.AUTH_INTERFACING_SAP_PASSWORD*/);//hardcode dlu karena masih suka berubah
		((BindingProvider) prov).getRequestContext().put("com.sun.xml.ws.connect.timeout", 1 * 60 * 1000);
		((BindingProvider) prov).getRequestContext().put("com.sun.xml.ws.request.timeout", 3 * 60 * 1000); 
		return ;
	}
	
	

	
	
	
	
	
	
	

	public static Date getNextDate(Date startDate, Integer jmlHari){ 
		Calendar cals = Calendar.getInstance();
		cals.set(Calendar.HOUR_OF_DAY, 0);
		cals.set(Calendar.MINUTE, 0);
		cals.set(Calendar.SECOND, 0);
		cals.add(Calendar.DATE, jmlHari);
		return cals.getTime();
	}
	
	public static void main (String [] args) throws Exception {
		logger.info("==== START ===="); 
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		try { 
//			String url = Constant.EPROC_BACKEND_ADDRESS + "/postLoginVendor";
//			String url = Constant.INTERFACING_BACKEND_ADDRESS_EOFFICE + "/postLoginUser";
//			PostLoginERPRequest loginUserERPRequest = new PostLoginERPRequest();
//			loginUserERPRequest.setUsername("284108569");
//			loginUserERPRequest.setPassword("123456");
//			ResponseObject responseObject1 = doPost(loginUserERPRequest, Constant.INTERFACING_BACKEND_ADDRESS_EOFFICE + "/postLoginUser");
//			Gson gson1 = new Gson();
//			JSONObject jsonObj1 = new JSONObject(responseObject1.getResponseString());
//			Object  jsonBody1 = jsonObj1.getJSONArray("body");
//			List<LoginUserERPResponse> loginUserERPResponses = gson1.fromJson(jsonBody1.toString(),new TypeToken<List<LoginUserERPResponse>>() {}.getType());
//			
//			String url2 = Constant.EBS_BACKEND_ADDRESS + "/getMasterItem";
//			ResponseObject responseObject = doPost(url2);
//			Gson gson = new Gson();
//			JSONObject jsonObj = new JSONObject(responseObject.getResponseString());
//			Object  jsonBody = jsonObj.getJSONArray("body");
//			List<ItemERP> itemERPs = gson.fromJson(jsonBody.toString(),new TypeToken<List<ItemERP>>() {}.getType());
//			System.out.println("END");
		} catch (Exception e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}
	}
}
