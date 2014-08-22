/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.gui.listener.TextFieldChangeListener;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class AdminsConfigurationPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private JPanel panel;
    private List<String> labels;

    public AdminsConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();

        createAdminsConfigurationPanel();

        this.add(panel);
    }

    private void createAdminsConfigurationPanel() {
        panel.setBorder(new EtchedBorder());
        
        labels = new ArrayList<>();
        labels.add("certificateAuthorityId");
        labels.add("electionManagerId");
        labels.add("electionAdministratorId");

        PanelBuilder builder = new PanelBuilder(new FormLayout(""));
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("left:pref");
        builder.appendColumn("fill:max(pref; 100px)");

        CellConstraints cellConstraints = new CellConstraints();

        int y = 0;

        //Admins
        for (String label : labels) {
            y++;
            builder.appendRow("top:pref");
            builder.addLabel(bundle.getString(label) + ": ", cellConstraints.xy(1, y));
            JTextField textField = new JTextField(config.getProperty(label), 15);
            textField.setName(label);
            textField.getDocument().addDocumentListener(new TextFieldChangeListener(textField));
            builder.add(textField, cellConstraints.xy(2, y));
        }

        //Mixers
        for (int i = 0; i < config.getMixersNumber(); i++) {
            y++;
            String mixerName = "mixer" + (i + 1);
            if (config.getMixerIds().length > i && config.getMixerIds()[i] != null) {
                mixerName = config.getMixerIds()[i];
            }
            builder.appendRow("top:pref");
            builder.addLabel("mixer" + (i + 1) + ": ", cellConstraints.xy(1, y));
            JTextField textField = new JTextField(mixerName, 15);
            textField.getDocument().addDocumentListener(new TextFieldChangeListener(textField));
            builder.add(textField, cellConstraints.xy(2, y));
        }

        //Talliers
        for (int i = 0; i < config.getTalliersNumber(); i++) {
            y++;
            String tallierName = "tallier" + (i + 1);
            if (config.getTallierIds().length > i && config.getTallierIds()[i] != null) {
                    tallierName = config.getTallierIds()[i];
            }
            builder.appendRow("top:pref");
            builder.addLabel("tallier" + (i + 1) + ": ", cellConstraints.xy(1, y));
            JTextField textField = new JTextField(tallierName, 15);
            textField.getDocument().addDocumentListener(new TextFieldChangeListener(textField));
            builder.add(textField, cellConstraints.xy(2, y));
        }

        panel.add(builder.getPanel());
    }
}
