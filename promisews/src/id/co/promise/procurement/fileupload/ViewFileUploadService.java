package id.co.promise.procurement.fileupload;

import id.co.promise.procurement.utils.Constant;
import id.co.promise.procurement.utils.ParamContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jboss.logging.Logger;

/**
 * 
 * Project Name : promisews Package Name : id.co.promise.procurement.fileupload
 * File Name : ViewFileUploadService.java Description : Decrypt and view
 * encrypted uploaded file
 * 
 * @author : reinhard
 */

@Path("/file")
public class ViewFileUploadService {
	final static Logger logger = Logger.getLogger(ViewFileUploadService.class);

	private String ENCRYPTION_PASSWORD = "pr0m1sE2015";

	@GET
	@Path("/view/{fileName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response viewFile(@PathParam("fileName") final String fileName) {

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				String uploadFilePath = ParamContext.getParameterStringByName("UPLOAD_FILE_PATH");
				String filePath = uploadFilePath + fileName;
				File f = new File(filePath);
				logger.info("### uploadFilePath  = " + uploadFilePath);
				logger.info("### viewFile, fileName = " + filePath);
				java.nio.file.Path path = Paths.get(filePath);

				// If File name Exist in table but not in Upload File
				if (f.exists()) {
					byte[] encryptedData = Files.readAllBytes(path);

					BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
					binaryEncryptor.setPassword(ENCRYPTION_PASSWORD);
					byte[] data = binaryEncryptor.decrypt(encryptedData);

					output.write(data);
					output.flush();
				} else {
					logger.error("### Error File, fileName = " + filePath + " NOT exist in Upload Folder!!");
				}
			}
		};
		return Response.ok(stream).build();
	}

	@GET
	@Path("/viewTemplate/{filenameKey}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response viewTemplate(@PathParam("filenameKey") final String filenameKey, @Context final HttpServletRequest httpServletRequest, @Context HttpServletResponse httpServletResponse) {

		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				
				String fileName = Constant.FILENAME_TEMPLATE.get(filenameKey);
				String uploadFilePath = httpServletRequest.getRealPath("/") + new File("/").toString()+ new File("WEB-INF", "template").toString() + new File("/").toString(); //penambahan '/' sebelum web-INF due to deprecated function
				String filePath = uploadFilePath + fileName;
				File f = new File(filePath);
				logger.info("### filenameKey        = " + filenameKey);
				logger.info("### fileName           = " + fileName);
				logger.info("### uploadFilePath     = " + uploadFilePath);
				logger.info("### viewFile, fileName = " + filePath);
				java.nio.file.Path path = Paths.get(filePath);

				// If File name Exist in table but not in Upload File
				if (f.exists()) {
					byte[] data = Files.readAllBytes(path);
					logger.info("## viewTemplate = " + data);
					logger.info("## viewTemplate size = " + data.length);
					output.write(data);
					output.flush();
				} else {
					logger.error("### Error File, fileName = " + filePath + " NOT exist in Upload Folder!!");
				}
			}
		};
		return Response.ok(stream).build();
	}

	@GET
	@Path("/downloadFile/{fileName}/{typeFile}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("fileName") final String fileName, @PathParam("typeFile") final String typeFile, @Context HttpServletRequest httpServletRequest,
			@Context final HttpServletResponse httpServletResponse) {
		StreamingOutput stream = new StreamingOutput() {
			@Override
			public void write(OutputStream output) throws IOException, WebApplicationException {
				String uploadFilePath = ParamContext.getParameterStringByName("UPLOAD_FILE_PATH");
				String filePath = new File(uploadFilePath, new File(fileName).getName()).toString();
				File f = new File(filePath);
				logger.info("### uploadFilePath  = " + uploadFilePath);
				logger.info("### viewFile, fileName = " + filePath);
				logger.info("### typeFile, typeFile = " + new File(typeFile).getName());
				java.nio.file.Path path = Paths.get(filePath);

				// If File name Exist in table but not in Upload File
				if (f.exists()) {
					byte[] encryptedData = Files.readAllBytes(path);

					BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
					binaryEncryptor.setPassword(ENCRYPTION_PASSWORD);
					byte[] data = encryptedData;
					httpServletResponse.setHeader("Content-disposition", "attachment; filename=" + filePath + "." + typeFile);
					output.write(data);
					output.flush();
				} else {
					logger.error("### Error File, fileName = " + filePath + " NOT exist in Upload Folder!!");
				}
			}
		};
		return Response.ok(stream).build();
	}

}
