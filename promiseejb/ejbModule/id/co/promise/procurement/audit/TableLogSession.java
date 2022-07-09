package id.co.promise.procurement.audit;

import id.co.promise.procurement.entity.TableLog;
import id.co.promise.procurement.modules.tablelog.TableLogDTO;
import id.co.promise.procurement.utils.AbstractFacade;
import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.ParamContext;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;



/**
 * @author : Reinhard MT
 */

@Stateless
@LocalBean
public class TableLogSession extends AbstractFacade<TableLog> {

	@PersistenceContext(unitName = "promisePU")
	private EntityManager em;

	public TableLogSession() {

		super(TableLog.class);
	}

	public TableLog get(int id) {

		return super.find(id);
	}

	@SuppressWarnings("unchecked")
	public List<TableLog> getList() {

		Query q = em.createNamedQuery("TableLog.getList");
		return q.getResultList();
	}

	public TableLog insert(TableLog tableLog) {
		super.create(tableLog);
		return tableLog;
	}

	public TableLog update(TableLog tableLog) {
		super.edit(tableLog);
		return tableLog;
	}

	

	@Override
	protected EntityManager getEntityManager() {

		// TODO Auto-generated method stub
		return em;
	}
	
	
	/* KAI 22042021 [23186] */
	@SuppressWarnings("unchecked")
	public List<TableLogDTO> findLogByMonthAndYearAndUserId(int month, int year, int userId) {
		List<TableLogDTO> tableLogList = new ArrayList<TableLogDTO>();
		String query = " SELECT"
				+ " tl.TABLE_LOG_ID,"
				+ " CASE"
				+ " 	WHEN tl.ACTION_TYPE = 1 THEN 'CREATE' "
				+ "		WHEN tl.ACTION_TYPE = 2 THEN 'UPDATE' "
				+ "		WHEN tl.ACTION_TYPE = 3 THEN 'DELETE' "
				+ "		WHEN tl.ACTION_TYPE = 4 THEN 'ROW_DELETE' "
				+ "		WHEN tl.ACTION_TYPE = 5 THEN 'UNDELETE' "
				+ "	END AS ACTION_TYPE,"
				+ " tl.NAMA_KOLOM, tl.NAMA_TABLE, tl.NILAI_AWAL, tl.NILAI_BARU, tl.WAKTU, u.NAMA_PENGGUNA"
				+ " FROM TABLE_LOG tl"
				+ " LEFT JOIN T1_USER u ON tl.USER_ID = u.USER_ID"
				+ " WHERE YEAR(tl.WAKTU) = :year "
				+ " AND MONTH(tl.WAKTU) = :month ";
				if(userId == Constant.ZERO_VALUES) {
					query += " AND tl.USER_ID IS NULL ";
				} else {
					query += " AND tl.USER_ID = :userId ";
				}
		Query q = em.createNativeQuery(query);
		q.setParameter("year", year);
		q.setParameter("month", month);
		if(userId != Constant.ZERO_VALUES) {
			q.setParameter("userId", userId);
		} 
//		tableLogList = (List<TableLog>)q.getResultList();
		List<Object[]> objectList = q.getResultList();
		for(int i=0; i<objectList.size(); i++) {
			TableLogDTO tableLog = new TableLogDTO();
			tableLog.setId(Integer.parseInt(objectList.get(i)[0].toString()));
			tableLog.setActionType(objectList.get(i)[1].toString());
			tableLog.setNamaKolom(objectList.get(i)[2].toString());
			tableLog.setNamaTable(objectList.get(i)[3].toString());
			tableLog.setNilaiAwal(objectList.get(i)[4].toString());
			tableLog.setNilaiBaru(objectList.get(i)[5].toString());
			tableLog.setWaktu(objectList.get(i)[6].toString());
//			String userTemp = objectList.get(i)[7].toString();
			if(objectList.get(i)[7] != null) {
				tableLog.setUser(objectList.get(i)[7].toString());
			} else {
				tableLog.setUser("SYSTEM");
			}
			tableLogList.add(tableLog);
		}
		return tableLogList;
	}
	
	@SuppressWarnings("unchecked")
	public List<TableLog> findLog(int userId, Date tanggalAwal,
			Date tanggalAkhir, String dataSebelum, String dataSesudah) {
		Query q = em.createNamedQuery("TableLog.findLog");
		q.setParameter("userId", userId);
		q.setParameter("tanggalAwal", tanggalAwal);
		q.setParameter("tanggalAkhir", tanggalAkhir);
		q.setParameter("dataAwal", dataSebelum.replaceAll("\\*", "%"));
		q.setParameter("dataAkhir", dataSesudah.replaceAll("\\*", "%"));
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getListMonthAndYearByUserId(Integer userId) {
		String query = " SELECT"
					 + " DISTINCT"
					 + "	MONTH(tl.WAKTU) as BULAN, YEAR(tl.WAKTU) as TAHUN"
					 + " FROM"
					 + "	TABLE_LOG tl"
					 + " WHERE ";
					 if(userId == Constant.ZERO_VALUES) {
						query += " tl.USER_ID IS NULL"; 
					 } else {
						query += " tl.USER_ID = :userId";
					 }
			   query += " ORDER BY"
					 + " 	BULAN, TAHUN DESC";
		Query q = em.createNativeQuery(query);
		if(userId != Constant.ZERO_VALUES) {
			q.setParameter("userId", userId);
		}
		return q.getResultList();
	}
	
	public void deleteByMonthAndYearAndUserId(Integer month, Integer year, Integer userId) {
		String query = " DELETE"
					 + " FROM"
					 + " 	TABLE_LOG"
					 + " WHERE"
					 + " 	YEAR(WAKTU) = :year"
					 + " AND"
					 + " 	MONTH(WAKTU) = :month"
					 + " AND";
					 if(userId == Constant.ZERO_VALUES) {
						 query += " USER_ID IS NULL ";
					 } else {
						 query += " USER_ID = :userId";
					 }
					 
		Query q = em.createNativeQuery(query);
		q.setParameter("year", year);
		q.setParameter("month", month);
		if(userId != Constant.ZERO_VALUES) {
			q.setParameter("userId", userId);
		}
		q.executeUpdate();
	}
	
	public List<TableLogDTO> readDataFromFileCsv(Integer month, Integer year, Integer userid, Integer limit, Integer offset, String keyword) {
		String logPath = ParamContext.getParameterStringByName("UPLOAD_FILE_LOG_PATH");
		String url = "jdbc:relique:csv:" + logPath + "?" + "separator=," + "&" + "fileExtension=.csv";
		String strMonth;
		
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if(month < 10) {
			strMonth = String.format("%02d", month);
			month = Integer.parseInt(strMonth);
		} else {
			strMonth = String.valueOf(month);
		}
		String fileName = userid+"_"+strMonth+"-"+year;
		java.nio.file.Path fileLocation = Paths.get(logPath);
  		java.nio.file.Path path = fileLocation.resolve(fileName + ".csv");
  		
		List<TableLogDTO> tableLogListFromCSV = new ArrayList<TableLogDTO>();

		try {
			
			Connection conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			ResultSet results;
			if(!Files.exists(path)) {
				throw new FileNotFoundException("File not found");
			} else {
				if(keyword.length() > 0) {
					results = stmt.executeQuery("SELECT"
							+ " TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"
							+ " FROM "+userid+"_"+strMonth+"-"+year+ ""
							+ " WHERE "
							+ " lower(NAMA_TABLE) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NAMA_KOLOM) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NILAI_AWAL) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NILAI_BARU) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(WAKTU) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NAMA_PENGGUNA) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(ACTION_TYPE) LIKE '%"+keyword.toLowerCase()+"%' "
							+ " limit " +limit+ " offset "+offset);
				} else {
					results = stmt.executeQuery("SELECT"
							+ " TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"
							+ " FROM "+userid+"_"+strMonth+"-"+year+ " limit " +limit+ " offset "+offset);//Sesuaikan dengan jumlah kolom yang akan ditampilkan
				}
				
			}
			while (results.next()) { //Sesuaikan dengan jumlah kolom yang akan ditampilkan
				TableLogDTO log = new TableLogDTO();
				log.setId(Integer.parseInt(results.getString(1)));
				log.setActionType(results.getString(2));
				log.setNamaKolom(results.getString(3));
				log.setNamaTable(results.getString(4));
				
				String nilaiAwal = results.getString(5);
				nilaiAwal = nilaiAwal.replaceAll("@COMMA@", ",");
				String nilaiBaru = results.getString(6);
				nilaiBaru = nilaiBaru.replaceAll("@COMMA@", ",");
				
				log.setNilaiAwal(nilaiAwal);
				log.setNilaiBaru(nilaiBaru);
				log.setWaktu(results.getString(7));
				log.setUser(results.getString(8));
				tableLogListFromCSV.add(log);
			}
			conn.close();
		} catch (SQLException | FileNotFoundException e) {
			e.getStackTrace();
			// TODO: handle exception
		}
		
		return tableLogListFromCSV;
	}
	
	public Integer readCountDataFromFileCsv(Integer month, Integer year, Integer userid, Integer limit, Integer offset, String keyword) {
		String logPath = ParamContext.getParameterStringByName("UPLOAD_FILE_LOG_PATH");
		String url = "jdbc:relique:csv:" + logPath + "?" + "separator=," + "&" + "fileExtension=.csv";
		String strMonth;
		
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if(month < 10) {
			strMonth = String.format("%02d", month);
			month = Integer.parseInt(strMonth);
		} else {
			strMonth = String.valueOf(month);
		}
		String fileName = userid+"_"+strMonth+"-"+year;
		java.nio.file.Path fileLocation = Paths.get(logPath);
  		java.nio.file.Path path = fileLocation.resolve(fileName + ".csv");
  		
		List<TableLogDTO> tableLogListFromCSV = new ArrayList<TableLogDTO>();

		try {
			
			Connection conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			ResultSet results;
			if(!Files.exists(path)) {
				throw new FileNotFoundException("File not found");
			} else {
				if(keyword.length() > 0) {
					results = stmt.executeQuery("SELECT"
							+ " TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"
							+ " FROM "+userid+"_"+strMonth+"-"+year+ ""
							+ " WHERE "
							+ " lower(NAMA_TABLE) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NAMA_KOLOM) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NILAI_AWAL) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NILAI_BARU) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(WAKTU) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(NAMA_PENGGUNA) LIKE '%"+keyword.toLowerCase()+"%' OR "
							+ " lower(ACTION_TYPE) LIKE '%"+keyword.toLowerCase()+"%' ");
//							+ " limit " +limit+ " offset "+offset);
				} else {
					results = stmt.executeQuery("SELECT"
							+ " TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"
							+ " FROM "+userid+"_"+strMonth+"-"+year);//Sesuaikan dengan jumlah kolom yang akan ditampilkan
				}
			}
			while (results.next()) { //Sesuaikan dengan jumlah kolom yang akan ditampilkan
				TableLogDTO log = new TableLogDTO();
				log.setId(Integer.parseInt(results.getString(1)));
				log.setActionType(results.getString(2));
				log.setNamaKolom(results.getString(3));
				log.setNamaTable(results.getString(4));
				
				String nilaiAwal = results.getString(5);
				nilaiAwal = nilaiAwal.replaceAll("@COMMA@", ",");
				String nilaiBaru = results.getString(6);
				nilaiBaru = nilaiBaru.replaceAll("@COMMA@", ",");
				
				log.setNilaiAwal(nilaiAwal);
				log.setNilaiBaru(nilaiBaru);
				log.setWaktu(results.getString(7));
				log.setUser(results.getString(8));
				tableLogListFromCSV.add(log);
			}
			conn.close();
		} catch (SQLException | FileNotFoundException e) {
			e.getStackTrace();
			// TODO: handle exception
		}
		
		return tableLogListFromCSV.size();
	}
	
	public TableLogDTO readDataFromFileCsvByIdTableLog(Integer month, Integer year, Integer userid, Integer idTableLog) {
		String logPath = ParamContext.getParameterStringByName("UPLOAD_FILE_LOG_PATH");
		String url = "jdbc:relique:csv:" + logPath + "?" + "separator=," + "&" + "fileExtension=.csv";
		String strMonth;
		
		try {
			Class.forName("org.relique.jdbc.csv.CsvDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if(month < 10) {
			strMonth = String.format("%02d", month);
			month = Integer.parseInt(strMonth);
		} else {
			strMonth = String.valueOf(month);
		}
		String fileName = userid+"_"+strMonth+"-"+year;
		java.nio.file.Path fileLocation = Paths.get(logPath);
  		java.nio.file.Path path = fileLocation.resolve(fileName + ".csv");
  		
		TableLogDTO log = new TableLogDTO();

		try {
			
			Connection conn = DriverManager.getConnection(url);

			Statement stmt = conn.createStatement();

			ResultSet results;
			if(!Files.exists(path)) {
				throw new FileNotFoundException("File not found");
			} else {
//				if(keyword.length() > 0) {
					results = stmt.executeQuery("SELECT"
							+ " TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"
							+ " FROM "+userid+"_"+strMonth+"-"+year+ ""
							+ " WHERE "
							+ " lower(TABLE_LOG_ID) LIKE '%"+idTableLog+"%'");
//							+ " limit " +limit+ " offset "+offset);
//				} else {
//					results = stmt.executeQuery("SELECT"
//							+ " TABLE_LOG_ID,ACTION_TYPE,NAMA_KOLOM,NAMA_TABLE,NILAI_AWAL,NILAI_BARU,WAKTU,NAMA_PENGGUNA"
//							+ " FROM "+userid+"_"+strMonth+"-"+year);//Sesuaikan dengan jumlah kolom yang akan ditampilkan
//				}
			}
			while (results.next()) { //Sesuaikan dengan jumlah kolom yang akan ditampilkan
				
				log.setId(Integer.parseInt(results.getString(1)));
				log.setActionType(results.getString(2));
				log.setNamaKolom(results.getString(3));
				log.setNamaTable(results.getString(4));
				
				String nilaiAwal = results.getString(5);
				nilaiAwal = nilaiAwal.replaceAll("@COMMA@", ",");
				String nilaiBaru = results.getString(6);
				nilaiBaru = nilaiBaru.replaceAll("@COMMA@", ",");
				
				log.setNilaiAwal(nilaiAwal);
				log.setNilaiBaru(nilaiBaru);
				log.setWaktu(results.getString(7));
				log.setUser(results.getString(8));
//				tableLogListFromCSV.add(log);
			}
			conn.close();
		} catch (SQLException | FileNotFoundException e) {
			e.getStackTrace();
			// TODO: handle exception
		}
		
		return log;
	}
}
