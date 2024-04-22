package com.saneforce.godairy.assistantClass;

import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Base64ToFileConverter {

    public static File convert(String base64String) {
        File pdfFile = null;
        try {
            byte[] pdfAsBytes = Base64.decode(base64String, Base64.DEFAULT);
            pdfFile = File.createTempFile("converted_pdf", ".pdf");
            FileOutputStream os = new FileOutputStream(pdfFile);
            os.write(pdfAsBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFile;
    }
}
