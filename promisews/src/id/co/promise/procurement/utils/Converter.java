package id.co.promise.procurement.utils;

import java.util.Calendar;
import java.util.Date;

import id.co.promise.procurement.entity.SertifikatVendor;

public class Converter {
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
	public static Boolean checkUpdateSertifikat(SertifikatVendor sertifikatVendor){
		Boolean updateTDR = false;

		Calendar calAkhirAktif = Calendar.getInstance();
		calAkhirAktif.setTime(sertifikatVendor.getTanggalBerakhir());
		calAkhirAktif.set(Calendar.HOUR_OF_DAY, 0);
		calAkhirAktif.set(Calendar.MINUTE, 0);
		calAkhirAktif.set(Calendar.SECOND, 0);
		calAkhirAktif.set(Calendar.MILLISECOND, 0);

		Calendar calHariIni = Calendar.getInstance();
		calHariIni.setTime(new Date());
		calHariIni.set(Calendar.HOUR_OF_DAY, 0);
		calHariIni.set(Calendar.MINUTE, 0);
		calHariIni.set(Calendar.SECOND, 0);
		calHariIni.set(Calendar.MILLISECOND, 0);

		if(calHariIni.getTime().getTime() > calAkhirAktif.getTime().getTime()){
			updateTDR = true;
		}else{
			long bulanminimalupdate = 60L;
			if(Converter.getSelisihTglWaktu(sertifikatVendor.getTanggalBerakhir()) <= bulanminimalupdate){
				updateTDR = true;
			}
		}
		
		return updateTDR;
	}
	

	
	public static long getSelisihTglWaktu(Date tglAkhir){
		
		Calendar calAkhirAktif = Calendar.getInstance();
		calAkhirAktif.setTime(tglAkhir);
		calAkhirAktif.set(Calendar.HOUR_OF_DAY, 0);
		calAkhirAktif.set(Calendar.MINUTE, 0);
		calAkhirAktif.set(Calendar.SECOND, 0);
		calAkhirAktif.set(Calendar.MILLISECOND, 0);
		Date newTglAkhAktf =  calAkhirAktif.getTime();
				
		Calendar calHariIni = Calendar.getInstance();
		calHariIni.setTime(new Date());
		calHariIni.set(Calendar.HOUR_OF_DAY, 0);
		calHariIni.set(Calendar.MINUTE, 0);
		calHariIni.set(Calendar.SECOND, 0);
		calHariIni.set(Calendar.MILLISECOND, 0);
		Date tglHari = calHariIni.getTime();
		
		long tes = newTglAkhAktf.getTime() - tglHari.getTime();
		long hasil = tes / (24 * 60 * 60 * 1000);

		return hasil;
	}
}
