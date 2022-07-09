package id.co.promise.procurement.utils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.util.SubnetUtils;
import org.jboss.logging.Logger;


public class IPRangeChecker {
	
	private static final Logger logger = Logger.getLogger(IPRangeChecker.class);
	
	private static final String[] HEADERS_TO_TRY = { 
	    "X-Forwarded-For",
	    "Proxy-Client-IP",
	    "WL-Proxy-Client-IP",
	    "HTTP_X_FORWARDED_FOR",
	    "HTTP_X_FORWARDED",
	    "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP",
	    "HTTP_FORWARDED_FOR",
	    "HTTP_FORWARDED",
	    "HTTP_VIA",
	    "REMOTE_ADDR" };

	public static String getClientIpAddress(HttpServletRequest request) {
	    for (String header : HEADERS_TO_TRY) {
	        String ip = request.getHeader(header);
	        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
	            return ip;
	        }
	    }
	    return request.getRemoteAddr();
	}
	
	public static long ipToLong(InetAddress ip) {
		byte[] octets = ip.getAddress();
		long result = 0;
		for (byte octet : octets) {
			result <<= 8;
			result |= octet & 0xff;
		}
		return result;
	}

	public static Boolean isValidRange(String ipStart, String ipEnd, String ipToCheck) {
		try {
			long ipLo = ipToLong(InetAddress.getByName(ipStart));
			long ipHi = ipToLong(InetAddress.getByName(ipEnd));
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			return (ipToTest >= ipLo && ipToTest <= ipHi);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}

	public static Boolean isValid(String ipStart, String ipToCheck) {
		try {
			long ipLo = ipToLong(InetAddress.getByName(ipStart));
			long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
			return (ipLo == ipToTest);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error(e);
			return false;
		}
	}

	public static Boolean isValidIpInternalRange(String ipToCheck, String param) {
		Boolean check = false;
		if(!ipToCheck.equals("0:0:0:0:0:0:0:1")){
			if(param != null){
				try{
					String[] subnet = param.split(";");
					if(subnet.length > 0){
						for(int i=0; i<subnet.length; i++){
							if(subnet[i].contains("/")){
								SubnetUtils utils = new SubnetUtils(subnet[0]);
								logger.info("ip check address: " + ipToCheck.split(",")[0].trim());
								logger.info("lower address: " + utils.getInfo().getLowAddress());
								logger.info("higher address: " + utils.getInfo().getHighAddress());
								logger.info("net mask address: " + utils.getInfo().getNetmask());
								if(utils.getInfo().isInRange(ipToCheck.split(",")[0].trim())){
									check = true;
								}
							} else{
								if(subnet[i].contains("-")){
									String[] iprange = subnet[i].split("-");
									if(isValidRange(iprange[0], iprange[1], ipToCheck.split(",")[0].trim())){
										check = true;
									}
								} else{
									if(isValid(subnet[i], ipToCheck.split(",")[0].trim())){
										check = true;
									}
								}
							}
						}
					}
					return check;
				} catch (Exception e){
					logger.error(e);
					return check;
				}
			}else{
				return check;
			}
		}else{
			return check;
		}
	}
	
	public static String getMACAddress(String ipAddress){
		StringBuilder sb = new StringBuilder();
		InetAddress ip;
		try {

			ip = InetAddress.getByName(ipAddress);

			NetworkInterface network = NetworkInterface.getByInetAddress(ip);

			byte[] mac = network.getHardwareAddress();

			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e){
			e.printStackTrace();
		}


        return sb.toString(); 
    }


	public static void main(String[] args) {

		logger.info(isValidRange("122.170.122.0", "122.170.122.255", "122.170.122.215"));

	}
}
