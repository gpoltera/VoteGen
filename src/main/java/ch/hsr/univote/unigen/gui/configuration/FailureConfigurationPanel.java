/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.configuration;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.gui.listener.CheckBoxChangeListener;
import ch.hsr.univote.unigen.gui.listener.ComboBoxChangeListener;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Dimension;
import java.util.ResourceBundle;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class FailureConfigurationPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private JPanel panel;
    private PanelBuilder builder;
    private CellConstraints cellConstraints;
    private int x, y;

    public FailureConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();

        createFailureConfigurationPanel();
        this.add(panel);
        this.setName(bundle.getString("failureconfiguration"));
    }

    private void createFailureConfigurationPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("failureconfiguration"));

        final String[] labels = {
            //SchnorrParameters
            "faultSchnorrPIsprime",
            "faultSchnorrPIssafeprime",
            "faultSchnorrQIsprime",
            "faultSchnorrGIsgenerator",
            "faultSchnorrParameterLength",
            //ElGamalParameters
            "faultElGamalPIsprime",
            "faultElGamalPIssafeprime",
            "faultElGamalQIsprime",
            "faultElGamalGIsgenerator",
            "faultElGamalParameterLength",
            //Certificates
            "faultCertificateCA",
            "faultCertificateEM",
            "faultCertificateEA",
            "faultCertificateMixer",
            "faultCertificateMixer-Mixer",
            "faultCertificateTallier",
            "faultCertificateTallier-Tallier",
            "faultCertificateVoter",
            "faultCertificateVoter-Voter",
            //Basics
            "faultEncryptionKey",
            "faultElectionGenerator",
            "faultVerificationKeys",
            "faultBallots",
            //Signatures
            "faultSignatureEACertificate",
            "faultSignatureElGamalParameter",
            "faultSignatureEncryptionKeys",
            "faultSignatureTallierMixerCertificates",
            "faultSignatureElectionBasicParameters",
            "faultSignatureElectionGenerator",
            "faultSignatureElectionOptions",
            "faultSignatureElectionData",
            "faultSignatureEncryptionKeyShare",
            "faultSignatureEncryptionKeyShare-Tallier",
            "faultSignatureBlindedGenerator",
            "faultSignatureBlindedGenerator-Mixer",
            //NIZKP    
            "faultNIZKPBlindedGenerator",
            "faultNIZKPBlindedGenerator-Mixer",
            "faultNIZKPEncryptionKeyShare",
            "faultNIZKPEncryptionKeyShare-Tallier"
        };

        builder = new PanelBuilder(new FormLayout(""));
        //builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("fill:max(pref; 250px)"); //1.Column
        builder.appendColumn("20dlu"); //2.Column Space
        builder.appendColumn("fill:max(pref; 250px)"); //3.Column

        cellConstraints = new CellConstraints();

        x = 1;
        y = 0;

        //Schnorr Parameters
        createTitle("schnorrparameters", x);
        for (int i = 0; i < 5; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);

        //ElGamal Parameters
        createTitle("elgamalparameters", x);
        for (int i = 5; i < 10; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);

        //Certificates
        createTitle("certificates", x);
        for (int i = 10; i < 13; i++) {
            createCheckBox(labels[i], x);
        }
        for (int i = 13; i < 19; i++) {
            createCheckBox(labels[i], x);
            i++;
            createComboBox(labels[i], x);
        }

        x = 3;
        y = 0;

        //Basics
        createTitle("basics", x);
        for (int i = 19; i < 23; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);

        //Signatures
        createTitle("signatures", x);
        for (int i = 23; i < 31; i++) {
            createCheckBox(labels[i], x);
        }
        for (int i = 31; i < 35; i++) {
            createCheckBox(labels[i], x);
            i++;
            createComboBox(labels[i], x);
        }
        createSeparator(x);

        //NIZKP
        createTitle("nizkp", x);
        for (int i = 35; i < 39; i++) {
            createCheckBox(labels[i], x);
            i++;
            createComboBox(labels[i], x);
        }

        panel.add(builder.getPanel());
    }

    private void createTitle(String key, int x) {
        y++;
        builder.appendRow("bottom:pref");
        builder.addTitle("<html><b>" + bundle.getString(key) + "</b></html>", cellConstraints.xy(x, y, "l, b"));
    }

    private void createSeparator(int x) {
        y++;
        builder.appendRow("top:pref");
        builder.addSeparator("", cellConstraints.xy(x, y, "f, t"));
    }

    private void createCheckBox(String key, int x) {
        y++;
        builder.appendRow("top:pref");
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(true);
        if (config.existProperty(key)) {
            checkBox.setSelected(config.getFault(key));
        }
        checkBox.setLabel(bundle.getString(key));
        checkBox.setName(key);
        checkBox.addChangeListener(new CheckBoxChangeListener(checkBox));
        builder.add(checkBox, cellConstraints.xy(x, y, "l, t"));
    }

    private void createComboBox(String key, int x) {
        JComboBox comboBox = new JComboBox();

        String type = key.split("-")[1];

        for (int i = 0; i < Integer.parseInt(config.getProperty(type + "s")); i++) {
            if (config.existProperty(type + (i + 1) + "Id")) {
                comboBox.addItem(config.getProperty(type + (i + 1) + "Id"));
            } else {
                comboBox.addItem(type + (i + 1));
            }
        }
        comboBox.setPreferredSize(new Dimension(100, 20));
        comboBox.setName(key);
        comboBox.addItemListener(new ComboBoxChangeListener(comboBox));
        builder.add(comboBox, cellConstraints.xy(x, y, "r, t"));
    }
}
