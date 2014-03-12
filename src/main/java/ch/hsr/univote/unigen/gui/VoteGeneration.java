/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.VoteGenerator;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Gian
 */
public class VoteGeneration extends javax.swing.JPanel {

    static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N

    /**
     * Creates new form NewJPanel
     */
    public VoteGeneration() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar = new javax.swing.JProgressBar();
        jStartVoteVerifier = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLogOutput = new javax.swing.JTextPane();

        setPreferredSize(new java.awt.Dimension(400, 300));

        jProgressBar.setMaximum(9);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        jStartVoteVerifier.setText(bundle.getString("startvoteverifier")); // NOI18N
        jStartVoteVerifier.setEnabled(false);
        jStartVoteVerifier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStartVoteVerifierActionPerformed(evt);
            }
        });

        jLogOutput.setEditable(false);
        jLogOutput.setText(bundle.getString("votewillbegenerated")); // NOI18N
        jScrollPane1.setViewportView(jLogOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jStartVoteVerifier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jStartVoteVerifier)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jStartVoteVerifierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStartVoteVerifierActionPerformed
        java.net.URL vvURL = VoteGenerator.class.getResource("/VoteVerifier.jar");
        try {
            // Edit to Mac/Linux compatibility
            //Process pc = Runtime.getRuntime().exec(new String[]{"java","-jar",vvURL.toString()});
            //Process pc = Runtime.getRuntime().exec("java -jar " + vvURL);
            Process pc = Runtime.getRuntime().exec("cmd.exe /c start " + vvURL);
        } catch (IOException ex) {
            Logger.getLogger(VoteGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jStartVoteVerifierActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTextPane jLogOutput;
    private static javax.swing.JProgressBar jProgressBar;
    private javax.swing.JScrollPane jScrollPane1;
    private static javax.swing.JButton jStartVoteVerifier;
    // End of variables declaration//GEN-END:variables

    public static void appendText(String text) {
        StyledDocument doc = jLogOutput.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "\n" + text, null);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void appendFailure(String text) {
        StyledDocument doc = jLogOutput.getStyledDocument();

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        StyleConstants.setBold(keyWord, true);

        try {
            doc.insertString(doc.getLength(), "\n FEHLER: " + text, keyWord);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static int i = 0;

    public static void updateProgress() {
        i++;
        jProgressBar.setValue(i);

        if (i == 9) {
            jStartVoteVerifier.setEnabled(true);
        }
    }

    public static void resetProgress() {
        i = 0;
        jProgressBar.setValue(i);
    }

    public static void resetText() {
        jLogOutput.setText(bundle.getString("votewillbegenerated"));
    }
}
