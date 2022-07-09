package id.co.promise.procurement.scheduler;

import id.co.promise.procurement.audit.TableLogSession;
import id.co.promise.procurement.entity.User;
import id.co.promise.procurement.modules.tablelog.TableLogDTO;
/*import id.co.promise.procurement.entity.modulform.TableLogDTO;*/
import id.co.promise.procurement.security.UserSession;
import id.co.promise.procurement.utils.ParamContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.ejb3.annotation.TransactionTimeout;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BackupTableLogSchedulerServices {

	@EJB
	TableLogSession tableLogSession;
	
	@EJB
	UserSession userSession;
	
	@Resource
	private UserTransaction userTransaction;
	
	public void moveTableLogToCsv(Integer month, Integer year, Integer userId) throws SQLException, Exception{
		List<TableLogDTO> tableLogList = new ArrayList<TableLogDTO>();
		String strMonth;
		tableLogList = tableLogSession.findLogByMonthAndYearAndUserId(month, year, userId);
		
		if(month < 10) {
			strMonth = String.format("%02d", month);
			month = Integer.parseInt(strMonth);
		} else {
			strMonth = String.valueOf(month);
		}
		String fileName = userId+"_"+strMonth+"-"+year;
		String logPath = ParamContext.getParameterStringByName("UPLOAD_FILE_LOG_PATH");
		String filePath = logPath + fileName + ".csv";
		Path fileLocation = Paths.get(logPath);
  		Path path = fileLocation.resolve(fileName + ".csv");
  		FileWriter fw;
		BufferedWriter bw;
		if(!Files.exists(path)) {
			File file = new File(filePath);
			file.createNewFile();
			fw = new FileWriter(filePath, true);
			bw = new BufferedWriter(fw);
			bw.write("TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"); //Sesuaikan dengan jumlah kolom yang akan ditampilkan
			bw.newLine();
		} else {
			fw = new FileWriter(filePath, true);
			bw = new BufferedWriter(fw);
		}
		for(TableLogDTO tableLog: tableLogList){		
			Integer tabelLogId = tableLog.getId();
			String actionType = tableLog.getActionType();
			String namaKolom = tableLog.getNamaKolom(); 
			String namaTable = tableLog.getNamaTable();       	
        	String nilaiAwal = tableLog.getNilaiAwal();
        	
        	nilaiAwal = nilaiAwal.replace("\n", "").replace("\r", "");
        	nilaiAwal = nilaiAwal.replaceAll(",", "@COMMA@");
        	
        	String nilaiBaru = tableLog.getNilaiBaru(); 
        	
        	nilaiBaru = nilaiBaru.replace("\n", "").replace("\r", "");
        	nilaiBaru = nilaiBaru.replaceAll(",", "@COMMA@");
        	
        	String waktu = tableLog.getWaktu(); 
        	String user = tableLog.getUser(); 
        	String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s",tabelLogId,actionType,namaKolom,namaTable,nilaiAwal,nilaiBaru,waktu,user);//Sesuaikan dengan jumlah kolom yang akan ditampilkan
        	bw.write(line);
        	bw.newLine();
		}
		bw.flush();
        bw.close();		
	}
	
	@Lock(LockType.READ)
	@TransactionTimeout(value = 4, unit = TimeUnit.HOURS)
	@Schedule(hour = "00", minute = "30", persistent = false)
	public void backUpAndCleanTableLog() throws IllegalStateException, SecurityException, SystemException {
		try {
			List<User> userList = new ArrayList<User>();
			
			Integer idUserSystem = 0;
			String userNameSystem = "SYSTEM";
			User userSystem = new User();
			userSystem.setId(idUserSystem);
			userSystem.setNamaPengguna(userNameSystem);
			userList.add(userSystem);
			
			userList.addAll(userSession.getDataIndexTableLogByTableUser());
			List<Object[]> listMonthAndYearUserTableLog = new ArrayList<Object[]>();
			if(userList.size() > 0) {

				for(User user: userList) {			
					listMonthAndYearUserTableLog = tableLogSession.getListMonthAndYearByUserId(user.getId());
					if(listMonthAndYearUserTableLog.size() > 0) {
						for(Object[] obj: listMonthAndYearUserTableLog) {
							// user transaction begin
							userTransaction.begin();

							Integer month = Integer.parseInt(String.valueOf(obj[0]));
							Integer year = Integer.parseInt(String.valueOf(obj[1]));
							moveTableLogToCsv(month, year, user.getId());
							tableLogSession.deleteByMonthAndYearAndUserId(month, year, user.getId());
							
							// user transaction commit
							userTransaction.commit();
						}
					}
				}
			}
		} catch (Exception e) {
			userTransaction.rollback();
			// user transaction rollback
		}
	}
}
