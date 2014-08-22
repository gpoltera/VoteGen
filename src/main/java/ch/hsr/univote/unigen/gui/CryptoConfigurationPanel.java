/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.gui.listener.ComboBoxChangeListener;
import ch.hsr.univote.unigen.gui.listener.SliderChangeListener;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class CryptoConfigurationPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private JPanel panel;
    private PanelBuilder builder;
    private CellConstraints cellConstraints;
    private int y;

    public CryptoConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();

        createCryptoConfigurationPanel();
        this.add(panel);
    }

    private void createCryptoConfigurationPanel() {
        panel.setBorder(new EtchedBorder());

        builder = new PanelBuilder(new FormLayout(""));
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("left:pref");
        builder.appendColumn("fill:max(pref; 100px)");

        cellConstraints = new CellConstraints();

        y = 0;

        //Encryption
        createTitel("encryption");
        String[] values = {"ElGamal"};
        createComboBox("encryptionAlgorithm", values);
        createSlider("encryptionKeyLength");
        createSeparator();

        //Signature
        createTitel("signature");
        String[] values2 = {"RSA"};
        createComboBox("signatureKeyType", values2);
        String[] values3 = {"SHA256withRSA"};
        createComboBox("signatureAlgorithm", values3);
        createSlider("signatureKeyLength");
        createSeparator();

        //Hash
        createTitel("hash");
        String[] values4 = {"SHA-256"};
        createComboBox("hashAlgorithm", values4);        
        String[] values5 = {"UTF-8"};
        createComboBox("charEncoding", values5);

        panel.add(builder.getPanel());
    }

    private void createTitel(String key) {
        y++;
        builder.appendRow("top:pref");
        builder.addTitle(bundle.getString(key), cellConstraints.xyw(1, y, 2));
    }

    private void createSeparator() {
        y++;
        builder.appendRow("top:pref");
        builder.addSeparator("", cellConstraints.xyw(1, y, 2));
    }

    private void createComboBox(String key, String[] values) {
        y++;
        builder.appendRow("top:pref");
        builder.addLabel(bundle.getString(key), cellConstraints.xy(1, y));
        JComboBox comboBox = new JComboBox();
        for (String value : values) {
            comboBox.addItem(value);
            if (value.equals(config.getProperty(key))) {
                comboBox.setSelectedItem(value);
            }
        }
        comboBox.setName(key);
        comboBox.addItemListener(new ComboBoxChangeListener(comboBox));
        builder.add(comboBox, cellConstraints.xy(2, y));
    }

    private void createSlider(String key) {
        y++;
        builder.appendRow("top:pref");
        builder.addLabel(bundle.getString(key), cellConstraints.xy(1, y));
        JSlider slider = new JSlider();
        slider.setMajorTickSpacing(1024);
        slider.setMaximum(4096);
        slider.setMinimum(1024);
        slider.setValue(Integer.parseInt(config.getProperty(key)));
        slider.setMinorTickSpacing(1024);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setName(key);
        slider.addChangeListener(new SliderChangeListener(slider));
        builder.add(slider, cellConstraints.xy(2, y));
    }
}
