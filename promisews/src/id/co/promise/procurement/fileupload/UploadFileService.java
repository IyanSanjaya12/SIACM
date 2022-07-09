package id.co.promise.procurement.fileupload;

import id.co.promise.procurement.utils.ParamContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
/**
 * 
 * Project Name : promisews
 * Package Name : id.co.promise.procurement.fileupload
 * File Name    : UploadFileService.java
 * Description  : Upload and encrypt file using : http://sourceforge.net/projects/jasypt/files/jasypt/
 * @author      : reinhard
 */


@Path("/procurement/file")
public class UploadFileService {
	final static Logger logger = Logger.getLogger(UploadFileService.class);
	private final String ENCRYPTION_PASSWORD = "pr0m1sE2015";
	
	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response uploadFile(MultipartFormDataInput input, @HeaderParam("Authorization") String token) {

		String fileName = "";

		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);
				fileName = generateRandomStringForFileName(fileName);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] bytes = IOUtils.toByteArray(inputStream);
				
				//encrypt 
				BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
				binaryEncryptor.setPassword(ENCRYPTION_PASSWORD);
				byte[] myEncryptedBytes = binaryEncryptor.encrypt(bytes);
				String uploadFilePath  = ParamContext.getParameterStringByName("UPLOAD_FILE_PATH");
				logger.info("### uploadFilePath  = " + uploadFilePath);
				writeFile(myEncryptedBytes, uploadFilePath + fileName);
				logger.info("### Done, fileName = " + fileName);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String json = "{\"fileName\":\"" + fileName + "\"}";
		return Response.ok(json, MediaType.APPLICATION_JSON).build();

	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename, is there a easy way in RESTEasy?
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();

	}

	/* sample 1453227624935bipji_filename */
	public String generateRandomStringForFileName(String filename) {
		final Integer target_length = 18;
		final String strCurrentTimeMillis = String.valueOf(System.currentTimeMillis());
		final Integer nRandomChar = (target_length - strCurrentTimeMillis.length());

		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder randomChar = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < nRandomChar; i++) {
			char c = chars[random.nextInt(chars.length)];
			randomChar.append(c);
		}

		return strCurrentTimeMillis + randomChar.toString() + "_" + filename;
	}
}
