package br.sp.gov.fatec.cancerconsultapi.service;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class FileManagementService {
    public String saveFile(InputStream image) {
        String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        try {
            File targetFile = new File(filename);
            OutputStream outStream = new FileOutputStream(targetFile);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = image.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(image);
            IOUtils.closeQuietly(outStream);
            return filename;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
