package com.kdgdev.jMFB.utils;

/**
 * Created with IntelliJ IDEA.
 * User: kirill
 * Date: 18.07.12
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class zipTools {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static void zipFile(String fileToZip, String zipFile, boolean excludeContainingFolder)
            throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));

        File srcFile = new File(fileToZip);
        if (excludeContainingFolder && srcFile.isDirectory()) {
            for (String fileName : srcFile.list()) {
                addToZip("", fileToZip + "/" + fileName, zipOut);
            }
        } else {
            addToZip("", fileToZip, zipOut);
        }

        zipOut.flush();
        zipOut.close();

        System.out.println("Successfully created " + zipFile);
    }

    private static void addToZip(String path, String srcFile, ZipOutputStream zipOut)
            throws IOException {
        File file = new File(srcFile);
        String filePath = "".equals(path) ? file.getName() : path + "/" + file.getName();
        if (file.isDirectory()) {
            for (String fileName : file.list()) {
                addToZip(filePath, srcFile + "/" + fileName, zipOut);
            }
        } else {
            zipOut.putNextEntry(new ZipEntry(filePath));
            FileInputStream in = new FileInputStream(srcFile);

            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }

            in.close();
        }
    }
}
