/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.gui.listener.CheckBoxListener;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
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
    private int x,y;

    public FailureConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();

        createFailureConfigurationPanel();

        this.add(panel);
    }

    private void createFailureConfigurationPanel() {
        panel.setBorder(new EtchedBorder());

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
            "faultElGamalGIsprime",
            "faultElGamalParameterLength",
            //??
            "faultEncryptionKey",
            "faultElectionGenerator",
            "faultVerificationKeys",
            //Certificates
            "faultCaCertificate",
            "faultEmCertificate",
            "faultEaCertificate",
            "faultMixerCertificate",
            "faultTallierCertificate",
            "faultVotersCertificate",
            //Signatures
            "faultEaCertificateSignature",
            "faultElGamalParameterSignature",
            "faultTallierNIZKPSignature",
            "faultEncryptionKeysSignature",
            "faultTallierMixerCertificateSignature",
            "faultElectionBasicParametersSignature",
            "faultElectionGeneratorSignature",
            "faultElectionOptionsSignature",
            "faultElectionDataSignature",
            "faultMixersNIZKPBlindedGeneratorSignature",
            //Varia    
            "faultMixerNIZKPBlindedGenerator",
            "faultTallierNIZKPEncryptionKeyShare"
        };

        builder = new PanelBuilder(new FormLayout(""));
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("fill:max(pref; 100px)");
        builder.appendColumn("fill:max(pref; 100px)");

        cellConstraints = new CellConstraints();     
    
        //Schnorr Parameters
        x = 1;
        y = 0;
        createTitle("schnorrparameters", x);
        for (int i = 0; i < 5; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);
        
        //ElGamal Parameters
        x = 2;
        y = 0;
        createTitle("elgamalparameters", x);
        for (int i = 5; i < 10; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);
        
        //??
        
        //Certificates
        x = 1;
        createTitle("certificates", x);
        for (int i = 13; i < 19; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);
        
        //Signatures
        x = 2;
        createTitle("signatures", x);
        for (int i = 19; i < 29; i++) {
            createCheckBox(labels[i], x);
        }
        createSeparator(x);
        

        panel.add(builder.getPanel());
    }

    private void createTitle(String key, int x) {
        y++;
        builder.appendRow("top:pref");
        builder.addTitle(bundle.getString(key), cellConstraints.xy(x, y));
    }

    private void createSeparator(int x) {
        y++;
        builder.appendRow("top:pref");
        builder.addSeparator("", cellConstraints.xy(x, y));
    }

    private void createCheckBox(String key, int x) {
        y++;
        builder.appendRow("top:pref");
        JCheckBox checkBox = new JCheckBox();
        checkBox.setSelected(true);
        checkBox.setLabel(bundle.getString(key));
        checkBox.setName(key);
        checkBox.addChangeListener(new CheckBoxListener(checkBox));
        builder.add(checkBox, cellConstraints.xy(x, y));
    }
}
