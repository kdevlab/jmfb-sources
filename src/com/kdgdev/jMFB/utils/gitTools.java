package com.kdgdev.jMFB.utils;


import brut.androlib.Androlib;
import brut.common.BrutException;
import brut.util.OS;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created with IntelliJ IDEA.
 * User: kirill
 * Date: 26.07.12
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class gitTools {

    private final static Logger LOGGER = Logger.getLogger(Androlib.class.getName());
    private String saveFolder;
    private byte[] readbuffer = new byte[65536];
    private static final String GITPATH_GITHUB = "https://github.com";
    private static final String GITPATH_BITBUCKET = "https://bitbucket.org";

    public void downloadFromGit(String project, String saveFolder) {
        downloadFromGit(project, saveFolder, "master");
    }

    public void downloadFromBitBucket(String project, String saveFolder) {
        downloadFromBitBucket(project, saveFolder, "master");
    }

    public void downloadFileFromGit(String project, String filePath, String saveFile, String Branch) {
        String url;
        if (project.startsWith("/?"))
            url = project;
        else {
            url = "/" + project + "/" + Branch + "/" + filePath;
        }
        try {
            LOGGER.info(url);
            downloadFile(url, saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void downloadFileFromGit(String project, String filePath, String saveFile) {
        downloadFileFromGit(project, filePath, saveFile, "master");
    }

    public void downloadFromGit(String project, String saveFolder, String Branch) {
        String url;
        if (project.startsWith("/?"))
            url = project;
        else {
            url = "/" + project + "/zipball/" + Branch + "/";
        }
        this.saveFolder = saveFolder;
        try {
            new File(saveFolder).mkdirs();

            downloadFile(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFromBitBucket(String project, String saveFolder, String Branch) {
        String url;
        if (project.startsWith("/?"))
            url = project;
        else {
            url = "/" + project + "/get/" + Branch + ".zip";
        }
        this.saveFolder = saveFolder;
        try {
            new File(saveFolder).mkdirs();

            downloadFileFromBitBucket(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(String href)
            throws IOException {
        String name = href;

        LOGGER.info("Downloading sources from github.com...");

        File outputFile = new File(this.saveFolder, "src.zip");
        if (outputFile.exists()) {
            LOGGER.info(" [already exists]");
            return;
        }

        OutputStream output = new FileOutputStream(outputFile);

        InputStream input = new URL(GITPATH_GITHUB + href).openStream();

        int size = 0;
        for (int n = 0; -1 != (n = input.read(this.readbuffer)); ) {
            size += n;
            output.write(this.readbuffer, 0, n);
        }
        BigDecimal fSize = new BigDecimal(((size / 1024.0) / 1024.0));
        LOGGER.info(" [" + fSize.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " MB downloaded]");

        output.close();

        unzipFile(this.saveFolder + File.separatorChar + "src.zip", this.saveFolder);
    }

    private void downloadFileFromBitBucket(String href)
            throws IOException {
        String name = href;

        LOGGER.info("Downloading sources from bitbucket.org... :" + href);

        File outputFile = new File(this.saveFolder, "src.zip");
        if (outputFile.exists()) {
            LOGGER.info(" [already exists]");
            return;
        }

        OutputStream output = new FileOutputStream(outputFile);

        InputStream input = new URL(GITPATH_BITBUCKET + href).openStream();

        int size = 0;
        for (int n = 0; -1 != (n = input.read(this.readbuffer)); ) {
            size += n;
            output.write(this.readbuffer, 0, n);
        }
        BigDecimal fSize = new BigDecimal(((size / 1024.0) / 1024.0));
        LOGGER.info(" [" + fSize.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " MB downloaded]");

        output.close();

        unzipFile(this.saveFolder + File.separatorChar + "src.zip", this.saveFolder);
    }

    private void downloadFile(String href, String fileName) throws IOException {
        String name = href;

        LOGGER.info("Downloading sources from github.com...");

        File outputFile = new File(fileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }

        OutputStream output = new FileOutputStream(outputFile);

        LOGGER.info("https://raw.github.com" + href);

        InputStream input = new URL("https://raw.github.com" + href).openStream();

        int size = 0;
        for (int n = 0; -1 != (n = input.read(this.readbuffer)); ) {
            size += n;
            output.write(this.readbuffer, 0, n);
        }
        BigDecimal fSize = new BigDecimal(((size / 1024.0) / 1024.0));
        LOGGER.info(" [" + fSize.setScale(2, BigDecimal.ROUND_HALF_DOWN) + " MB downloaded]");

        output.close();
    }

    public static final void writeFile(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();
    }

    public static void unzipFile(String zipFileName, String directoryToExtractTo) {
        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            Enumeration entriesEnum = zipFile.entries();

            File directory = new File(directoryToExtractTo);

            if (!directory.exists()) {
                new File(directoryToExtractTo).mkdir();
                LOGGER.info("...Directory Created -" + directoryToExtractTo);
            }

            LOGGER.info("Extracting sources to local directory...");
            while (entriesEnum.hasMoreElements()) {
                try {
                    ZipEntry entry = (ZipEntry) entriesEnum.nextElement();

                    if (entry.isDirectory()) {
                        new File(directory + "/" + entry.getName()).mkdir();
                    } else {
                        int index = 0;
                        String name = entry.getName();
                        index = entry.getName().lastIndexOf("/");
                        if ((index > 0) && (index != name.length())) {
                            name = entry.getName().substring(index + 1);
                        }

                        writeFile(zipFile.getInputStream(entry),
                                new BufferedOutputStream(
                                        new FileOutputStream(directory + "/" + entry.getName())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            File srcdirOfgit = new File(((ZipEntry) zipFile.entries().nextElement()).getName());

            zipFile.close();
            File f = new File(zipFileName);
            f.delete();

            File directoryOfgit = new File(directoryToExtractTo);

            if (directoryOfgit.isDirectory()) {
                String[] filenames = directoryOfgit.list();

                File destination = new File(directoryToExtractTo);
                File source = new File(directoryToExtractTo + "/" + srcdirOfgit);

                copyFolder(source, destination);
                deleteDir(source);
            }
        } catch (BrutException e) {
            LOGGER.info(e.getMessage());
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        File f;
        ZipFile zipFile;
        Enumeration entriesEnum;
    }

    public static void copyFolder(File src, File dest) throws BrutException {
        OS.cpdir(src, dest);
    }

    public static void deleteDir(File dir) {
        try {
            OS.rmdir(dir);
        } catch (BrutException e) {
            LOGGER.info(e.getMessage());
        }
    }

}
