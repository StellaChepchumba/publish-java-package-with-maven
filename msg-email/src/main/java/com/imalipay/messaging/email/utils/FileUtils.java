package com.imalipay.messaging.email.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils 
{
	public static File downloadFromUrl(URL url) throws IOException 
	{
		log.trace("downloading file {}", url.getFile());
        var tempDir =  new File(System.getProperty("java.io.tmpdir"));
        var outputPath = File.createTempFile("email_attachment",null,tempDir);

        try (InputStream is = url.openConnection().getInputStream();
        		FileOutputStream fos =	new FileOutputStream(outputPath)) {

            // 4KB buffer
            byte[] buffer = new byte[4096];
            int length;

            // read from source and write into local file
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            return outputPath;
        } catch (IOException e) {
			log.error("failed to download file {}",e.getMessage());
	    	throw new IOException(e);
		}
    }
}
