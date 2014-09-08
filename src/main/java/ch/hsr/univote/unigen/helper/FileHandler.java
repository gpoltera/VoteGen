/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Gian
 */
public class FileHandler {

    private final static String DB_FOLDER = "db/";
    private final static String DB_FILE = "db";
    private final static String DB_KEYSTORE = "_keystore";
    private final static String CONFIG_FOLDER = "properties/";
    private final static String CONFIG_FILE = "config.properties";
    private final static String EXTERNAL_FILE = "vgc";
    private final static String MB_TEXT = "MB";
    private final static String KB_TEXT = "KB";

    public void saveInExternalFile(File file) {
        String filetype = "";
        if (!file.getName().endsWith("." + EXTERNAL_FILE)) {
            filetype = "." + EXTERNAL_FILE;
        }
        try {
            ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(file + filetype));
            byte[] buffer = new byte[4096];
            int len;

            //ConfigFile
            FileInputStream configInputStream = new FileInputStream(CONFIG_FOLDER + CONFIG_FILE);
            zipout.putNextEntry(new ZipEntry(CONFIG_FOLDER + CONFIG_FILE));
            while ((len = configInputStream.read(buffer)) > 0) {
                zipout.write(buffer, 0, len);
            }

            //DBs
            List<File> dbs = getDBFileList();
            for (File db : dbs) {
                FileInputStream dbInputStream = new FileInputStream(db);
                zipout.putNextEntry(new ZipEntry(DB_FOLDER + db.getName()));
                while ((len = dbInputStream.read(buffer)) > 0) {
                    zipout.write(buffer, 0, len);
                }
            }
            zipout.close();
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadFromExternalFile(File file) {
        if (file.getName().endsWith("." + EXTERNAL_FILE)) {
            try {
                ZipFile zipFile = new ZipFile(file);
                Enumeration enumeration = zipFile.entries();
                while (enumeration.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
                    BufferedInputStream bis = null;
                    bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    byte[] buffer = new byte[4096];
                    int avail = bis.available();
                    if (avail > 0) {
                        buffer = new byte[avail];
                        bis.read(buffer, 0, avail);
                    }
                    String fileName = zipEntry.getName();
                    BufferedOutputStream bos = null;
                    bos = new BufferedOutputStream(new FileOutputStream(fileName));
                    bos.write(buffer, 0, buffer.length);
                    bos.close();
                    bis.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String[][] getElectionDBFileList() {
        File dir = new File(DB_FOLDER);
        File[] loadedfiles = dir.listFiles();
        List<File> checkedfiles = new ArrayList<>();

        if (loadedfiles != null) { // Permissions are available
            for (File file : loadedfiles) {
                if (file.isFile()) {
                    if (file.getName().endsWith("." + DB_FILE) && !file.getName().endsWith(DB_KEYSTORE + "." + DB_FILE)) {
                        checkedfiles.add(file);
                    }
                }
            }
        }

        String[][] filelist = new String[checkedfiles.size()][2];

        for (int i = 0; i < checkedfiles.size(); i++) {
            String filename = checkedfiles.get(i).getName();
            long filesize = checkedfiles.get(i).length() / 1024;

            String filedescription;
            if (filesize < 1024) {
                filedescription = filesize + " " + KB_TEXT;
            } else {
                filedescription = filesize / 1024 + " " + MB_TEXT;
            }

            filelist[i][0] = filename;
            filelist[i][1] = filedescription;
        }

        return filelist;
    }

    private List<File> getDBFileList() {
        File dir = new File(DB_FOLDER);
        File[] loadedfiles = dir.listFiles();
        List<File> checkedfiles = new ArrayList<>();

        if (loadedfiles != null) { // Permissions are available
            for (File file : loadedfiles) {
                if (file.isFile()) {
                    if (file.getName().endsWith(DB_FILE)) {
                        checkedfiles.add(file);
                    }
                }
            }
        }

        return checkedfiles;
    }
}
