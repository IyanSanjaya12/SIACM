package id.co.promise.procurement.utils;

import id.co.promise.procurement.entity.PRStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant {
	
	/* KAI 22042021 [23186] */
	public static final int ZERO_VALUES = 0;
	
	public static final int LOG_STATUS_SUCCESS = 1;
	public static final int LOG_STATUS_ERORR = 0;
	
	public static final String LOG_SUCCESS = "SUCCESS";
	public static final String LOG_ERORR = "ERORR";
	
	public static final String LOG_METHOD_POST= "POST";
	public static final String LOG_METHOD_GET = "GET";
	public static final String LOG_SERVICE_NAME_SEND_FROM_EBS = "SEND DATA FROM EBS";
	public static final String LOG_SERVICE_NAME_SEND_BO = "SEND DATA BO";
	public static final String LOG_SERVICE_NAME_SEND_PR = "SEND DATA PR";
	public static final String LOG_SERVICE_NAME_SEND_PO = "SEND DATA PO";
	public static final String LOG_SERVICE_NAME_GET_PO = "GET DATA PO";
	public static final String LOG_SERVICE_NAME_GET_PR = "GET DATA PR";
	public static final String LOG_SERVICE_NAME_GET_USER = "GET DATA USER";
	public static final String LOG_SERVICE_NAME_GET_BANK = "GET DATA BANK";
	public static final String LOG_SERVICE_NAME_CHECK_LOGIN = "SEND DATA USER & PASSSWORD INTERNAL";
	public static final String LOG_SERVICE_NAME_CHECK_LOGIN_VENDOR = "SEND DATA USER & PASSSWORD VENDOR";
	public static final String LOG_SERVICE_NAME_CHECK_USER = "CHECK DATA USER";
	public static final String LOG_SERVICE_NAME_GET_INVOICE_PAYMENT = "GET DATA INVOICE PAYMENT";
	public static final String LOG_SERVICE_NAME_GET_ITEM = "GET DATA MASTER ITEM";
	public static final String LOG_SERVICE_NAME_GET_DATA_RECEIPT = "GET DATA RECEIPT";
	public static final String LOG_SERVICE_NAME_GET_VENDOR = "GET DATA VENDOR";
	public static final String LOG_SERVICE_NAME_GET_PO_BY_PR = "GET PO BY PR";
	public static final String LOG_SERVICE_NAME_POST_DATA_RECEIPT = "POST DATA RECEIPT";
	public static final String LOG_SERVICE_NAME_GET_PUSPEL_CODE = "GET PUSPEL CODE";
	public static final String LOG_SERVICE_NAME_GET_EXPENSE_ACCOUNT = "GET EXPENSE ACCOUNT";

	public static final int ZERO_VALUE = 0;
	public static final int ONE_VALUE = 1;
	public static final int TWO_VALUE = 2;
	
	public static final String NOTIFICATION_TEMPLATE_GENERATE_PASSWORD = "NOTIFICATION GENERATE PASSWORD";

	public static final String JMS_FINAL_PO = "FINALPO";
	public static final String JMS_FINAL_CONTRACT_ITEM = "FINALCONTRACTITEM";

	/** template xls delivery receipt **/
	public static final int TEMPLATE_DR_KODE_PO = 0;
	public static final int TEMPLATE_DR_KODE_ITEM = 1;
	public static final int TEMPLATE_DR_PASS = 2;
	public static final int TEMPLATE_DR_FAILED = 3;
	public static final int TEMPLATE_DR_COMMENT = 4;

	/** template xls Attribute **/
	public static final int TEMPLATE_ATT_NAMA = 0;
	public static final int TEMPLATE_ATT_INPUT_TYPE = 1;
	public static final int TEMPLATE_ATT_OPTION = 2;

	public final static String SERVICES_USER = "SERVICES_USER";
	public final static String SERVICES_PASSWORD = "SERVICES_PASSWORD";
	public static final String ENCRYPTION_KEY_FOR_PASS = "promiseMMICombiphar";
	public static final String SESSION_PWD_KEY = "SESSION_PWD_KEY";
	public static final String COOKIES_KEY = "JPROMISESESSIONID";
	public static final String COOKIES_APPLIST_KEY = "JPROMISEAPPLIST";
	public static final String ENCRYPTION_KEY_FOR_FILE = "pr0m1sE2015";

	/** template xls Attribute Group **/
	public static final int TEMPLATE_ATT_GR_NAMA_GROUP = 0;
	public static final int TEMPLATE_ATT_GR_NAMA_PANEL = 1;
	public static final int TEMPLATE_ATT_GR_NAMA_ATT = 2;
	public static final int TEMPLATE_ATT_GR_ATT_INPUT_TYPE = 3;
	public static final int TEMPLATE_ATT_GR_OPTION = 4;

	public static final Integer LOGIN_STATUS_TERKUNCI = 0;
	public static final Integer LOGIN_STATUS_AKTIF = 1;

	public static final List<String> VENDOR_ROLE = Arrays.asList("VND", "VMCAL");

	/** template xls Category **/
	public static final int TEMPLATE_CATEGORY_DESCRIPTION = 0;
	public static final int TEMPLATE_CATEGORY_TRANSLATE_IND = 1;
	public static final int TEMPLATE_CATEGORY_TRANSLATE_ENG = 2;
	public static final int TEMPLATE_CATEGORY_PARENT_CATEGORY = 3;

	/** template xls Alokasi Anggaran **/
	public static final int TEMPLATE_ALOCATION_DRAFT_NO = 0;
	public static final int TEMPLATE_ALOCATION_ACCOUNT_NAME = 1;
	public static final int TEMPLATE_ALOCATION_TAHUN_ANGGARAN = 2;
	public static final int TEMPLATE_ALOCATION_JENIS_ANGGARAN = 3;
	public static final int TEMPLATE_ALOCATION_JUMLAH = 4;
	public static final int TEMPLATE_ALOCATION_UNIT = 5;

	/** status purchase order & purchase order item **/
	public static final int PO_STATUS_DELETE = 0;
	public static final int PO_STATUS_NEW = 1;
	public static final int PO_STATUS_EDIT = 2;
	public static final int PO_STATUS_APPROVAL = 3;
	public static final int PO_STATUS_HOLD = 4;
	public static final int PO_STATUS_REJECT = 5;
	public static final int PO_STATUS_PROCESS = 6;
	public static final int PO_STATUS_DONE = 7;

	/** NET INTEGRATION METHOD **/
	public static final String NET_GET_URL = "GET";
	public static final String NET_POST_URL = "POST";
	public static final int INTEGRATION_SUCCESS = 1;
	public static final int INTEGRATION_FAILED = 2;
	
	//scheduler staus

	public static Boolean AXIQOE_SYNC_INPROGRESS = false;

	// CATALOG KONTRAK STATUS
	public static final int CATALOG_KONTRAK_STATUS_ACTIVE = 1;
	public static final int CATALOG_KONTRAK_STATUS_NON_ACTIVE = 0;
	//KAI 28/01/2021 [21865]
	public static final int CATALOG_KONTRAK_STATUS_CATALOG_ACTIVE = 1;
	public static final int CATALOG_KONTRAK_STATUS_CATALOG_NONACTIVE = 0;
	public static final int CATALOG_KONTRAK_STATUS_CATALOG_EXPIRED = 2;
	public static final int STATUS_CATALOG_WAITING_FOR_APPROVAL = 1;
	public static final int STATUS_CATALOG_EXPIRED = 0;
	
	// PR STATUS
	// KAI - 20201123
	//public static final int PR_STATUS_NEW = 1;
	public static final int PR_STATUS_REJECT = 2;
	public static final int PR_STATUS_PROCESSING = 3;
	//public static final int PR_STATUS_REJECT_PR = 4;
	public static final int PR_STATUS_READY_TO_CREATE_PO = 5;
	public static final int PR_STATUS_PROCESSING_PO = 6;
	//public static final int PR_STATUS_REJECT_PO = 7;
	public static final int PR_STATUS_PO_COMPLETE = 8;
	//public static final int PR_STATUS_FAILED_TO_SEND_BO = 9;
	//public static final int PR_STATUS_PO_RECEIVED = 10;
	public static final int PR_STATUS_AWAITING_VENDOR_APPROVAL = 11;
	public static final int PR_STATUS_AWAITING_USER_APPROVAL = 12;
	public static final int PR_STATUS_VENDOR_REJECTED = 13;
	//public static final int PR_STATUS_PO_PUBLISHING_INVOICE = 14;
	//public static final int PR_STATUS_PO_PAYMENT_COMPLETED = 15;
	//public static final int PR_STATUS_PO_FAILED_SENDING_DR_TO_EBS = 16;
	
	public static final String PR_STATUS_TEXT_AWAITING_USER_APPROVAL = "Awaiting User Approval";
	public static final String PR_STATUS_TEXT_VENDOR_REJECTED = "Vendor Rejected";
	
	public static final String PO_STATUS_TEXT_PO_COMPLETE = "PO Complete";
	public static final String PO_STATUS_TEXT_FAILED_SENDING_PO_TO_SAP = "Failed Sending PO to SAP";
	
	//public static final String PO_RECEIVED = "PO Received";
	
	public static final int DR_STATUS_ISFINISH_TRUE = 1;
	
	public static final int URL_TYPE_INTERNAL_USER = 2;
	public static final int URL_TYPE_VENDOR = 1;
	
	// ROLE USER VENDOR
	public static final int ROLE_USER_VENDOR_PM = 2;
	public static final int ROLE_USER_VENDOR_CM = 10;
	
	//ACTION SYNC VENDOR
	public static final String ACTION_SYNC_REGISTER = "REGISTRASIVENDOR";
	public static final String ACTION_SYNC_EDIT = "EDITVENDOR";
	
	//ACTION CATALOG
	public static final String DELETE_ACTION = "DELETE";
	public static final String UPDATE_ACTION = "UPDATE";
	public static final String INSERT_ACTION = "INSERT NEW";
	
	//STATUS SYNC VENDOR LOG
	public static final Integer STATUS_SYNC_SUCCESS = 1;
	public static final Integer STATUS_SYNC_FAIL = 2;
	
	//STATUS LOGIN EBS
	public static final Integer LOGIN_EBS_NOT_ALLOW = 0;
	public static final Integer LOGIN_EBS_ALLOW = 1;
	
	//STATUS SYNC VENDOR LOG
	public static final String VENDOR_SYNC_LOG_ACTION_GET = "GET_VENDOR";
	public static final String VENDOR_SYNC_LOG_ACTION_GET_LIST = "GET_VENDOR_LIST";
	public static final String VENDOR_SYNC_LOG_ACTION_DELETE = "DELETE";
	
	//TAHAPAN NAME

	public static final String TAHAPAN_CREATE_PR = "CREATE_PR";
	public static final String TAHAPAN_CREATE_PO = "CREATE_PO";
	public static final String TAHAPAN_APPROVAL_BO = "Approval Purchase Request";
	public static final String TAHAPAN_APPROVAL_INSERT_CATALOG = "Approval New Catalog";
	public static final String TAHAPAN_APPROVAL_EDIT_CATALOG = "Approval Edit Catalog";
	
	public static final Integer TAHAPAN_BOOKING_ORDER = 1;
	//Organisasi Pusat
	public static final String ORGANISASI_PUSAT= "Kantor Pusat";
	
	//CEK ROLE SAAT LOGIN
	public static final String ROLE_CODE_PENGGUNA_DVP= "PENGGUNA_DVP";
	public static final String ROLE_CODE_VENDOR= "VE";
	public static final String ROLE_CODE_PENGGUNA_SPV="PENGGUNA_SPV";
	public static final String ROLE_CODE_DIREKTUR_PENGGUNA="DIREKTUR_PENGGUNA";
	public static final String ROLE_CODE_LOGISTIK="PRC";
	public static final String ROLE_CODE_USER="USR";
	public static final Integer ROLE_ID_PENGGUNA_SPV = 5;
	public static final Integer ROLE_ID_PENGGUNA_DVP = 4;
	
	public static final String ROLE_APPCODE_PM = "PM";
	//NAMA TAHAPAN
	public static final String STAGES_PURCHASE_REQUEST = "Purchase Request";
	
	public static final String RESPONSE_SUCCESS = "SUCCESS";
	
	public static final String INTERFACING_KEY = "babangfunction";
	
	//URL API Interfacing Aplikasi Surrounding
	public static final String INTERFACING_BACKEND_ADDRESS_EPROC = ParamContext.getParameterStringByName("INTERFACING_BACKEND_ADDRESS_EPROC");
	public static final String INTERFACING_BACKEND_ADDRESS_EBS = ParamContext.getParameterStringByName("INTERFACING_BACKEND_ADDRESS_EBS");
	public static final String INTERFACING_BACKEND_ADDRESS_EOFFICE = ParamContext.getParameterStringByName("INTERFACING_BACKEND_ADDRESS_EOFFICE");
	public static final String INTERFACING_BACKEND_ADDRESS_EDOC = ParamContext.getParameterStringByName("INTERFACING_BACKEND_ADDRESS_EDOC");
	
	//STORE PROCEDURE
	public static final String DO_INSERT_U_LOGIN = "DO_INSERT_U_LOGIN";
	public static final String DO_SYNC_HOLIDAY = "DO_SYNC_HOLIDAY";
	public static final String DO_SYNC_ITEM = "DO_SYNC_ITEM";
	public static final String DO_UPDATE_LOGIN_LOG = "doUpdateLoginLog";
	public static final String DO_SYNC_ORGANIZATION = "DO_SYNC_ORGANIZATION";
	public static final String DO_SYNC_UNIT = "DO_SYNC_UNIT";
	public static final String RESET_SEQUENCES = "RESET_SEQUENCES";
	public static final String SYNC_IN_PO_CAT_TO_CM = "SYNC_IN_PO_CAT_TO_CM";
	public static final String SYNC_IN_UP_VEND_CAT_TO_CM = "SYNC_IN_UP_VEND_CAT_TO_CM";
	public static final String SYNC_SSO_CATALOG_CM = "SYNC_SSO_CATALOG_CM";
	public static final String SYNC_IN_UP_DR_CAT_TO_CM = "SYNC_IN_UP_DR_CAT_TO_CM";
	public static final String SYNC_IN_UP_PO_INV_CAT_TO_CM = "SYNC_IN_UP_PO_INV_CAT_TO_CM";
	public static final String SYNC_IN_UP_ADD_PLH_TO_CM = "SYNC_IN_UP_ADD_PLH_TO_CM";
	
	public static final String AUTH_INTERFACING_SAG_USERNAME = ParamContext.getParameterStringByName("AUTH_INTERFACING_SAG_USERNAME");
	public static final String AUTH_INTERFACING_SAG_PASSWORD = ParamContext.getParameterStringByName("AUTH_INTERFACING_SAG_PASSWORD");
	
	public static final String AUTH_INTERFACING_SAP_DEV_USERNAME = ParamContext.getParameterStringByName("AUTH_INTERFACING_SAP_DEV_USERNAME");
	public static final String AUTH_INTERFACING_SAP_DEV_PASSWORD = ParamContext.getParameterStringByName("AUTH_INTERFACING_SAP_DEV_PASSWORD");
	public static final String AUTH_INTERFACING_SAP_PROD_USERNAME = ParamContext.getParameterStringByName("AUTH_INTERFACING_SAP_PROD_USERNAME");
	public static final String AUTH_INTERFACING_SAP_PROD_PASSWORD = ParamContext.getParameterStringByName("AUTH_INTERFACING_SAP_PROD_PASSWORD");
	public static final String CACHE_PATH = ParamContext.getParameterStringByName("CACHE_PATH");
	
	public static final boolean IS_INTERFACING_SAP = ParamContext.getParameterBooleanByName("IS_INTERFACING_SAP");
	
	
	public static final String TYPE_THRESHOLD_PUSAT = "PUSAT";
	public static final String TYPE_THRESHOLD_CABANG = "CABANG";
	
	public static final Integer TYPE_THRESHOLD_PUSAT_VALUE = 1;
	public static final Integer TYPE_THRESHOLD_CABANG_VALUE = 2;
	
	//EMAIL
	public static final String RILIS_BOOKING_ORDER_TO_EBS = "Pemberitahuan Rilis Booking Order ke aplikasi EBS";
	public static final String RILIS_DELIVERY_RECEIPT_TO_EBS = "Pemberitahuan Rilis Delivery Receipt ke aplikasi EBS";
	public static final String RILIS_PURCHASE_ORDER_TO_EBS = "Pemberitahuan Rilis Purchase Order ke aplikasi EBS";
	public static final String NOTIF_PURCHASE_REQUEST_TO_VENDOR = "Pemberitahuan Persetujuan Purchase Request kepada Penyedia";
	public static final String PEMBERITAHUAN_PEMBAYARAN_SELESAI = "Pemberitahuan Pembayaran Selesai";
	public static final String PEMBERITAHUAN_PENERBITAN_INVOICE = "Pemberitahuan Penerbitan Invoice";
	public static final String PEMBERITAHUAN_PERSETUJUAN_BOOKING_ORDER = "Pemberitahuan Persetujuan Booking Order";
	public static final String PEMBERITAHUAN_BERHASIL_MENDAFTAR= "Pemberitahuan Berhasil Mendaftar";
	public static final String NOTIF_EMAIL_DELIVERY_RECEIVED = "Notifikasi Delivery Received";
	public static final String NOTIF_EMAIL_AKSI_EDIT_CATALOG = "dilakukan Perubahan data Item Catalog oleh Vendor";
	public static final String NOTIF_EMAIL_AKSI_ADD_CATALOG = "disimpan oleh Vendor";
	
	public static final boolean IS_DEVELOPMENT_MODE = ParamContext.getParameterBooleanByName("IS_DEVELOPMENT_MODE");
	
	//DEFAULT PASSWORD
	public static final String DEFAULT_PASSWORD= "123456";
	
	//PLH
	public static final Integer IS_PLH= 1;
	
	//SET NILAI ASURANSI
	public static final Double ASURANSI= (double) 0;
	
	//SECURITY
	public static final String LOGIN_SALT = "bfcd13d88b8f05a3b91c7f77dc20c366";
    public static final String LOGIN_IV = "4bf264874e8e0b0f46e55616f2c76187";
    public static final String LOGIN_PASSPHRASE = "uWo5rx!y";
    public static final Integer LOGIN_KEY_SIZE = 128;
    public static final Integer LOGIN_ITERATION_COUNT= 1000;
    public static final String LOCATION_FILE_WSDL= ParamContext.getParameterStringByName("LOCATION_FILE_WSDL");

    public static final String PARAMETER_SESSION_TIMEOUT = "SESSION_TIMEOUT";
    
    public static final String KODE_JABATAN_PEMBANTU_MEKANIK = "PBMK";
    public static final String KODE_JABATAN_MEKANIK = "MK";
    public static final String KODE_JABATAN_KEPALA_MEKANIK = "KPLMK";
	
	static List<PRStatus> prStatusList = new ArrayList<PRStatus>();
//	{
//		{
			/*
			 * add(new PRStatus(PR_STATUS_REJECT, "REJECT", "Reject")); add(new
			 * PRStatus(PR_STATUS_APPROVAL_PROCESS, "APPROVAL_PROCESS",
			 * "Approval Process")); add(new PRStatus(PR_STATUS_HOLD, "HOLD", "Hold"));
			 * add(new PRStatus(PR_STATUS_ON_PROGRESS, "ON_PROGRESS", "On Progress"));
			 * add(new PRStatus(PR_STATUS_RECEIVED, "RECEIVED", "Received")); add(new
			 * PRStatus(PR_STATUS_PROCUREMENT_PROCESS, "PROCUREMENT_PROCESS",
			 * "Procurement Process")); add(new PRStatus(PR_STATUS_NEED_VERIFICATION,
			 * "NEED_VERIFICATION", "Need Verification"));
			 */
//		}
//	};

	public static PRStatus getPrStatusByValue(Integer value) {
		for (PRStatus status : prStatusList) {
			if (status.getValue().equals(value)) {
				return status;
			}
		}
		return null;
	}

	public static PRStatus getPrStatusByKey(String key) {
		for (PRStatus status : prStatusList) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status;
			}
		}
		return null;
	}

	public static final Map<Integer, String> CELLSTRINGTYPE;
	static {
		CELLSTRINGTYPE = new HashMap<Integer, String>();
		CELLSTRINGTYPE.put(0, "CELL_TYPE_NUMERIC");
		CELLSTRINGTYPE.put(1, "CELL_TYPE_STRING");
		CELLSTRINGTYPE.put(2, "CELL_TYPE_FORMULA");
		CELLSTRINGTYPE.put(3, "CELL_TYPE_BLANK");
		CELLSTRINGTYPE.put(4, "CELL_TYPE_ERROR");
	}

	/* for security all file must be listed */
	public static final Map<String, String> FILENAME_TEMPLATE;
	static {
		FILENAME_TEMPLATE = new HashMap<String, String>();
		FILENAME_TEMPLATE.put("PurchaseRequestTemplate.xlsx", "PurchaseRequestTemplate.xlsx");
		FILENAME_TEMPLATE.put("templateDR.xlsx","templateDR.xlsx");
		FILENAME_TEMPLATE.put("templateCatalog.xlsx","templateCatalog.xlsx");
		FILENAME_TEMPLATE.put("User_Manual_VM.pdf","User_Manual_VM.pdf");
		FILENAME_TEMPLATE.put("User_Manual.pdf","User_Manual.pdf");
		FILENAME_TEMPLATE.put("PanduanImportCatalog.xlsx","PanduanImportCatalog.xlsx");
	}

}
