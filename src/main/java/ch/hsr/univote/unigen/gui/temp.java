/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.Publisher;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gian
 */
public class temp {
   
//    
//    
//    /**
//     * This method is called from within the constructor to initialize the form.
//     */
//    private void initComponents() {
//
//        jBtnStartStop = new javax.swing.JButton();
//        jPanel = new javax.swing.JPanel();
//
//        startpage = new ch.hsr.univote.unigen.gui.Startpage();
//        cryptoConfiguration = new CryptoConfiguration();
//        voteGeneration = new ch.hsr.univote.unigen.gui.VoteGeneration();
//
//        jMenuBar = new javax.swing.JMenuBar();
//        jMenuFile = new javax.swing.JMenu();
//        jMenuItemOpen = new javax.swing.JMenuItem();
//        jMenuItemSave = new javax.swing.JMenuItem();
//        jMenuItemQuit = new javax.swing.JMenuItem();
//        jMenuLanguage = new javax.swing.JMenu();
//        jMenuItemGerman = new javax.swing.JMenuItem();
//        jMenuItemEnglish = new javax.swing.JMenuItem();
//        jMenuHelp = new javax.swing.JMenu();
//        jMenuItemDocumentation = new javax.swing.JMenuItem();
//        jMenuItemAbout = new javax.swing.JMenuItem();
//
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
//        setTitle(bundle.getString("title")); // NOI18N
//        setMinimumSize(new java.awt.Dimension(500, 500));
//        setPreferredSize(new java.awt.Dimension(500, 500));
//        setResizable(false);
//
//        jBtnStartStop.setText(bundle.getString("generatevote")); // NOI18N
//        jBtnStartStop.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jBtnStartStopActionPerformed(evt);
//            }
//        });
//        getContentPane().add(jBtnStartStop, java.awt.BorderLayout.CENTER);
//
//        startpage.setName(""); // NOI18N
//        jPanel.add(startpage);
//
//        getContentPane().add(jPanel, java.awt.BorderLayout.PAGE_END);
//
//        jPanel.setToolTipText(bundle.getString("cryptoconfiguration")); // NOI18N
//        jPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
//        jPanel.setMinimumSize(new java.awt.Dimension(450, 400));
//        jPanel.setPreferredSize(new java.awt.Dimension(450, 400));
//        getContentPane().add(jPanel, java.awt.BorderLayout.PAGE_START);
//        jPanel.getAccessibleContext().setAccessibleName("");
//        jPanel.getAccessibleContext().setAccessibleDescription(bundle.getString("cryptoconfiguration")); // NOI18N
//
//    
// 
//
//
//        pack();
//    }// </editor-fold>                        
//
//    boolean votestarted = false;
//
//    //Btn Start/Stop
//    private void jBtnStartStopActionPerformed(java.awt.event.ActionEvent evt) {
//        if (!votestarted) {
//            jPanel.add(bundle.getString("votegeneration"), voteGeneration);
//            jPanel.remove(initialConfiguration);
//            jPanel.remove(cryptoConfiguration);
//            jPanel.remove(candidates);
//            jPanel.remove(failureConfiguration);
//            jBtnStartStop.setLabel(bundle.getString("stopservice"));
//            new Thread() {
//                public void run() {
//                    try {
//                        VoteGenerator.electionSequence();
//                    } catch (Exception ex) {
//                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    try {
//                        Publisher.startWebSrv();
//                    } catch (IOException ex) {
//                        Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }.start();
//            votestarted = true;
//        } else {
//            Publisher.stopWebSrv();
//            jPanel.add(this);
//            VoteGeneration.resetProgress();
//            VoteGeneration.resetText();
//            jBtnStartStop.setLabel(bundle.getString("generatevote"));
//            votestarted = false;
//        }
//    }
//
//    //Menu Quit
//    private void jMenuItemQuitActionPerformed(java.awt.event.ActionEvent evt) {
//        System.exit(0);
//    }
//
//    //Menu About
//    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {
//        JOptionPane.showMessageDialog(null,
//                "Copyright:\nGian Poltéra 2013-2014\n\nVersion 0.3",
//                "About",
//                JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    //Menu Save
//    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {
//        JFileChooser fc = new JFileChooser();
//        fc.setFileFilter(new FileNameExtensionFilter("VoteGenerator Config", "vgc"));
//
//        int state = fc.showSaveDialog(null);
//
//        if (state == JFileChooser.APPROVE_OPTION) {
//            File file = fc.getSelectedFile();
//            String filename = file.getName();
//            String filetype = "";
//            if (!file.getName().endsWith(".vgc")) {
//                filetype = ".vgc";
//            }
//
//            try {
//                ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(file + filetype));
//                byte[] buffer = new byte[4096];
//                int len;
//                FileInputStream in1 = new FileInputStream("properties/SystemConfigFile.properties");
//                FileInputStream in2 = new FileInputStream("properties/CryptoConfigFile.properties");
//                FileInputStream in3 = new FileInputStream("properties/FaultConfigFile.properties");
//                zipout.putNextEntry(new ZipEntry("SystemConfigFile.properties"));
//                while ((len = in1.read(buffer)) > 0) {
//                    zipout.write(buffer, 0, len);
//                }
//                zipout.putNextEntry(new ZipEntry("CryptoConfigFile.properties"));
//                while ((len = in2.read(buffer)) > 0) {
//                    zipout.write(buffer, 0, len);
//                }
//                zipout.putNextEntry(new ZipEntry("FaultConfigFile.properties"));
//                while ((len = in3.read(buffer)) > 0) {
//                    zipout.write(buffer, 0, len);
//                }
//                zipout.close();
//                in1.close();
//                in2.close();
//                in3.close();
//
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//
//    //Menu Open
//    private void jMenuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {
//        JFileChooser fc = new JFileChooser();
//        fc.setFileFilter(new FileNameExtensionFilter("VoteGenerator Config", "vgc"));
//
//        int state = fc.showOpenDialog(null);
//
//        if (state == JFileChooser.APPROVE_OPTION) {
//            File file = fc.getSelectedFile();
//
//            try {
//                ZipFile zipFile = new ZipFile(file);
//                Enumeration enu = zipFile.entries();
//
//                while (enu.hasMoreElements()) {
//                    ZipEntry zipEntry = (ZipEntry) enu.nextElement();
//                    BufferedInputStream bis = null;
//                    bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
//                    byte[] buffer = new byte[4096];
//                    int avail = bis.available();
//                    if (avail > 0) {
//                        buffer = new byte[avail];
//                        bis.read(buffer, 0, avail);
//                    }
//
//                    String fileName = zipEntry.getName();
//                    BufferedOutputStream bos = null;
//                    bos = new BufferedOutputStream(new FileOutputStream("properties/" + fileName));
//                    bos.write(buffer, 0, buffer.length);
//                    bos.close();
//                    bis.close();
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            new MainGUI().setVisible(true);
//            this.dispose();
//        }
//    }
//
//    //Menu Language German
//    private void jMenuItemGermanActionPerformed(java.awt.event.ActionEvent evt) {
//        Locale locale = new Locale("de", "DE");
//        Locale.setDefault(locale);
//
//        new MainGUI().setVisible(true);
//
//        this.dispose();
//    }
//
//    //Menu Language English
//    private void jMenuItemEnglishActionPerformed(java.awt.event.ActionEvent evt) {
//        Locale locale = new Locale("en", "EN");
//        Locale.setDefault(locale);
//
//        new MainGUI().setVisible(true);
//
//        this.dispose();
//    }
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MainGUI().setVisible(true);
//            }
//        });
//    } 
}
