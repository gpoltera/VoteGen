/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui;

/**
 *
 * @author Gian
 */
public class SystemConfiguration extends javax.swing.JPanel {

    /**
     * Creates new form ts
     */
    public SystemConfiguration() {
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
        java.awt.GridBagConstraints gridBagConstraints;

        jLblElectionId = new javax.swing.JLabel();
        jTxtElectionId = new javax.swing.JTextPane();
        jLblElectionDescription = new javax.swing.JLabel();
        jTxtElectionDescription = new javax.swing.JTextPane();
        jLblVoterNumbers = new javax.swing.JLabel();
        jSPVoteNumbers = new javax.swing.JSpinner();
        jLblMixerNumbers = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLblTallierNumbers = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLblMaxCandidates = new javax.swing.JLabel();
        jSpinner5 = new javax.swing.JSpinner();
        jLblMaxCumulation = new javax.swing.JLabel();
        jSpinner4 = new javax.swing.JSpinner();
        jLblEncryptionKeyLength = new javax.swing.JLabel();
        jCBEncryptionKeyLength = new javax.swing.JComboBox();
        jLblSignatureKeyLength = new javax.swing.JLabel();
        jCBSignatureKeyLength = new javax.swing.JComboBox();
        jLblSignatureAlgorithm = new javax.swing.JLabel();
        jCBSignatureAlgorithm = new javax.swing.JComboBox();

        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.columnWidths = new int[] {0, 1, 0};
        layout.rowHeights = new int[] {0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0, 3, 0};
        layout.columnWeights = new double[] {0.0};
        layout.rowWeights = new double[] {0.0};
        setLayout(layout);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        jLblElectionId.setText(bundle.getString("electionid")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        add(jLblElectionId, gridBagConstraints);

        jTxtElectionId.setText("Election2014");
        jTxtElectionId.setMinimumSize(new java.awt.Dimension(250, 25));
        jTxtElectionId.setPreferredSize(new java.awt.Dimension(250, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jTxtElectionId, gridBagConstraints);

        jLblElectionDescription.setText("Wahl Text");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblElectionDescription, gridBagConstraints);

        jTxtElectionDescription.setMinimumSize(new java.awt.Dimension(250, 25));
        jTxtElectionDescription.setName(""); // NOI18N
        jTxtElectionDescription.setPreferredSize(new java.awt.Dimension(250, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jTxtElectionDescription, gridBagConstraints);

        jLblVoterNumbers.setText("Anzahl Wähler");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblVoterNumbers, gridBagConstraints);

        jSPVoteNumbers.setModel(new javax.swing.SpinnerNumberModel(25, 1, 10000000, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jSPVoteNumbers, gridBagConstraints);

        jLblMixerNumbers.setText("Anzahl Mixer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblMixerNumbers, gridBagConstraints);

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jSpinner3, gridBagConstraints);

        jLblTallierNumbers.setText("Anzahl Tallier");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblTallierNumbers, gridBagConstraints);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jSpinner2, gridBagConstraints);

        jLblMaxCandidates.setText("max Kandidaten");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblMaxCandidates, gridBagConstraints);

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jSpinner5, gridBagConstraints);

        jLblMaxCumulation.setText("max Cumulationen");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblMaxCumulation, gridBagConstraints);

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(5, 1, 100, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jSpinner4, gridBagConstraints);

        jLblEncryptionKeyLength.setText("Encryption key length");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblEncryptionKeyLength, gridBagConstraints);

        jCBEncryptionKeyLength.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "64 Bit", "128 Bit", "256 Bit", "512 Bit" }));
        jCBEncryptionKeyLength.setSelectedIndex(2);
        jCBEncryptionKeyLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBEncryptionKeyLengthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jCBEncryptionKeyLength, gridBagConstraints);

        jLblSignatureKeyLength.setText("Signature key length");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblSignatureKeyLength, gridBagConstraints);

        jCBSignatureKeyLength.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "512 Bit", "1024 Bit", "2048 Bit", "4096 Bit" }));
        jCBSignatureKeyLength.setSelectedIndex(2);
        jCBSignatureKeyLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBSignatureKeyLengthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jCBSignatureKeyLength, gridBagConstraints);

        jLblSignatureAlgorithm.setText("Signature algorithm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLblSignatureAlgorithm, gridBagConstraints);

        jCBSignatureAlgorithm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SHA256withRSA" }));
        jCBSignatureAlgorithm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBSignatureAlgorithmActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        add(jCBSignatureAlgorithm, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jCBSignatureAlgorithmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBSignatureAlgorithmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBSignatureAlgorithmActionPerformed

    private void jCBSignatureKeyLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBSignatureKeyLengthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBSignatureKeyLengthActionPerformed

    private void jCBEncryptionKeyLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBEncryptionKeyLengthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCBEncryptionKeyLengthActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jCBEncryptionKeyLength;
    private javax.swing.JComboBox jCBSignatureAlgorithm;
    private javax.swing.JComboBox jCBSignatureKeyLength;
    private javax.swing.JLabel jLblElectionDescription;
    private javax.swing.JLabel jLblElectionId;
    private javax.swing.JLabel jLblEncryptionKeyLength;
    private javax.swing.JLabel jLblMaxCandidates;
    private javax.swing.JLabel jLblMaxCumulation;
    private javax.swing.JLabel jLblMixerNumbers;
    private javax.swing.JLabel jLblSignatureAlgorithm;
    private javax.swing.JLabel jLblSignatureKeyLength;
    private javax.swing.JLabel jLblTallierNumbers;
    private javax.swing.JLabel jLblVoterNumbers;
    private javax.swing.JSpinner jSPVoteNumbers;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JTextPane jTxtElectionDescription;
    private javax.swing.JTextPane jTxtElectionId;
    // End of variables declaration//GEN-END:variables
}