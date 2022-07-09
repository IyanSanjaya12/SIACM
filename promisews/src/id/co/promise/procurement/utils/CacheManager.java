package id.co.promise.procurement.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;

public class CacheManager {
	final static Logger logger = Logger.getLogger(CacheManager.class);
	
	private static String file_cache_path = Constant.CACHE_PATH;

	public static String saveByKey(String key, Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		return saveByKey(key, json);
	}

	public static String saveByKey(String key, String str) {
		String filePath = file_cache_path + key + ".json";
		return saveToFileJson(str, filePath);
	}
	
	private static String saveToFileJson(String str, String filePath) {
		logger.info("Save cache to file: " + filePath);
		MinifyJSON minifyJSON = new MinifyJSON();
		String minifyJson = minifyJSON.minify(str);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(minifyJson);
			writer.close();
			logger.info("Save cache success!");
		} catch (IOException e) {
			logger.error("Save cache error!");
		}
		return minifyJson;
	}

	public static Object getByKey(String key, Class<?> className) {
		String str = getByKey(key);
		Gson gson = new Gson();
		return gson.fromJson(str, className);
	}

	public static String getByKey(String key) {
		String filePath = file_cache_path + key + ".json";
		return getFromFileJson(filePath);
	}
	
	private static String getFromFileJson(String filePath) {
		logger.info("Load cache from file: " + filePath);
		String str = null;
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(filePath));
			str = new String(encoded);
		} catch (NoSuchFileException e) {
			logger.warn("Cache file not found!");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return str;
	}

	public static void deleteByKey(String key) {
		String filePath = file_cache_path + key + ".json";
		try {
			Files.delete(Paths.get(filePath));
		} catch (NoSuchFileException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public static void deleteAll() throws IOException {
		File dirFile = new File(file_cache_path);
		if (dirFile.exists()) {
			FileUtils.cleanDirectory(dirFile);
		}
	}

}
