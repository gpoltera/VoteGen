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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
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

    private final static String APP_FOLDER = ".votegenerator";
    private final static String DB_FOLDER = "db";
    private final static String DB_FILE = "db";
    private final static String DB_KEYSTORE = "_keystore";
    private final static String CONFIG_FOLDER = "properties";
    private final static String CONFIG_FILE = "config.properties";
    private final static String VV_FOLDER = "voteverifier";
    private final static String VV_FILE = "VoteVerifier.jar";
    private final static String DOCU_FOLDER = "documentation";
    private final static String DOCU_FILE = "VoteGeneratorDocumentation.pdf";
    private final static String EXTERNAL_FILE = "vgc";
    private final static String MB_TEXT = "MB";
    private final static String KB_TEXT = "KB";
    private String userDirectoryPath;
    private String userConfigFilePath;
    public String userDBFolderPath;
    public String voteVerifierJARPath;
    public String documentationPDFPath;

    public FileHandler() {
        this.userDirectoryPath = System.getProperty("user.home");

        createFolders();
    }

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
            File configFile = new File(userConfigFilePath);
            if (configFile.exists()) {
                FileInputStream configInputStream = new FileInputStream(userConfigFilePath);
                zipout.putNextEntry(new ZipEntry(CONFIG_FOLDER + "/" + CONFIG_FILE));
                while ((len = configInputStream.read(buffer)) > 0) {
                    zipout.write(buffer, 0, len);
                }
            }

            //DBs
            List<File> dbs = getDBFileList();
            for (File db : dbs) {
                FileInputStream dbInputStream = new FileInputStream(db);
                zipout.putNextEntry(new ZipEntry(DB_FOLDER + "/" + db.getName()));
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
                    BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    byte[] buffer = new byte[4096];
                    int avail = inputStream.available();
                    if (avail > 0) {
                        buffer = new byte[avail];
                        inputStream.read(buffer, 0, avail);
                    }
                    String fileName = zipEntry.getName();
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(userDirectoryPath + "/" + APP_FOLDER + "/" + fileName));
                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.close();
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String[][] getElectionDBFileList() {
        File dir = new File(userDBFolderPath);
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

    public Properties getConfigProperties() {
        Properties properties = new Properties();

        try {
            File file = new File(userConfigFilePath);
            if (file.exists()) {
                properties.load(new FileInputStream(file));
            } else {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FOLDER + "/" + CONFIG_FILE);
                properties.load(inputStream);
                inputStream.close();
            }
        } catch (IOException e) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, e);
        }

        return properties;
    }

    public void saveConfigProperties(Properties properties) {
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(userConfigFilePath));
            properties.store(outputStream, userConfigFilePath);
            outputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private List<File> getDBFileList() {
        File dir = new File(userDBFolderPath);
        File[] loadedfiles = dir.listFiles();
        List<File> checkedfiles = new ArrayList<>();

        if (loadedfiles != null) {
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

    private void createFolders() {

        try {
            //Basedir
            File dir = new File(userDirectoryPath + "/" + APP_FOLDER);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //Config Folder
            dir = new File(userDirectoryPath + "/" + APP_FOLDER + "/" + CONFIG_FOLDER);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //DB Folder
            dir = new File(userDirectoryPath + "/" + APP_FOLDER + "/" + DB_FOLDER);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //VoteVerifier Folder
            dir = new File(userDirectoryPath + "/" + APP_FOLDER + "/" + VV_FOLDER);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //Documentation Folder
            dir = new File(userDirectoryPath + "/" + APP_FOLDER + "/" + DOCU_FOLDER);
            if (!dir.exists()) {
                dir.mkdir();
            }

            //Set Variables for futer use
            userConfigFilePath = userDirectoryPath + "/" + APP_FOLDER + "/" + CONFIG_FOLDER + "/" + CONFIG_FILE;
            userDBFolderPath = userDirectoryPath + "/" + APP_FOLDER + "/" + DB_FOLDER + "/";
            voteVerifierJARPath = userDirectoryPath + "/" + APP_FOLDER + "/" + VV_FOLDER + "/" + VV_FILE;
            documentationPDFPath = userDirectoryPath + "/" + APP_FOLDER + "/" + DOCU_FOLDER + "/" + DOCU_FILE;

            //Load the standard properties
            File configFile = new File(userConfigFilePath);
            if (!configFile.exists()) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FOLDER + "/" + CONFIG_FILE);
                if (!(inputStream == null)) {
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    inputStream.close();
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(userConfigFilePath));
                    properties.store(outputStream, userConfigFilePath);
                    outputStream.close();
                }
            }

            //Copy the VoteVerifier
            File vvFile = new File(voteVerifierJARPath);
            if (!vvFile.exists()) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(VV_FOLDER + "/" + VV_FILE);
                if (!(inputStream == null)) {
                    int readBytes;
                    byte[] buffer = new byte[4096];
                    OutputStream outputStream = new FileOutputStream(new File(voteVerifierJARPath));
                    while ((readBytes = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, readBytes);
                    }
                    inputStream.close();
                    outputStream.close();
                }
            }

            //Copy the Documentation
            File docuFile = new File(documentationPDFPath);
            if (!docuFile.exists()) {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DOCU_FOLDER + "/" + DOCU_FILE);
                if (!(inputStream == null)) {
                    int readBytes;
                    byte[] buffer = new byte[4096];
                    OutputStream outputStream = new FileOutputStream(new File(documentationPDFPath));
                    while ((readBytes = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, readBytes);
                    }
                    inputStream.close();
                    outputStream.close();
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
