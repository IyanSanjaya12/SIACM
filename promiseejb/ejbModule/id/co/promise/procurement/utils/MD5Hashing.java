package id.co.promise.procurement.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.jboss.logging.Logger;

public class MD5Hashing {
	final static Logger log = Logger.getLogger(MD5Hashing.class);
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException{
		
		log.info("Heloo : "+MD5Hashing.getMD5Hashing("1234567"));
		Date d = new Date();
		log.info("Heloo : "+MD5Hashing.getMD5Hashing(""+d.getTime()));
		log.info("Heloo : "+MD5Hashing.getMD5Hashing("agus.rochmad:"+d.getTime()));
	}
	
	public static String getMD5Hashing(String value)throws NoSuchAlgorithmException, IOException{
		String result = "";
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(value.getBytes());			
		byte byteData[] = md.digest();
 
        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	result = hexString.toString();
		return result;
	}

}
