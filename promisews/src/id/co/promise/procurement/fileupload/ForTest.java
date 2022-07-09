package id.co.promise.procurement.fileupload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jasypt.util.binary.BasicBinaryEncryptor;
import org.jboss.logging.Logger;

import id.co.promise.procurement.purchaseorder.PurchaseOrderItemServices;

public class ForTest {
	final static Logger log = Logger.getLogger(ForTest.class);
	public static void main(String[] args) {

		String myEncryptionPassword = "ProMISE2015";
		BasicBinaryEncryptor binaryEncryptor = new BasicBinaryEncryptor();
		binaryEncryptor.setPassword(myEncryptionPassword);

		Path path = Paths.get("/home/hardnrei/upload/a.pdf");
		byte[] myBytes;
		try {
			myBytes = Files.readAllBytes(path);
			byte[] myEncryptedBytes = binaryEncryptor.encrypt(myBytes);
			FileUtils.writeByteArrayToFile(new File("/home/hardnrei/upload/b.pdf"), myEncryptedBytes);
			log.info(">>  SUKSES  <<");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info(">>  GAGAL  <<");
			e.printStackTrace();
		}

	}

}
