/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.configuration;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.gui.listener.TextFieldChangeListener;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.ArrayList;
import java.util.List;
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
        this.setName(bundle.getString("adminconfiguration"));
    }

    private void createAdminsConfigurationPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("adminconfiguration"));

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
            String mixerId = "Mixer" + (i + 1) + "Id";
            String mixerName = "Mixer" + (i + 1);
            if (config.existProperty(mixerId)) {
                mixerName = config.getProperty(mixerId);
            }
            builder.appendRow("top:pref");
            builder.addLabel("Mixer" + (i + 1) + ": ", cellConstraints.xy(1, y));
            JTextField textField = new JTextField(mixerName, 15);
            textField.setName(mixerId);
            textField.getDocument().addDocumentListener(new TextFieldChangeListener(textField));
            builder.add(textField, cellConstraints.xy(2, y));
        }

        //Talliers
        for (int i = 0; i < config.getTalliersNumber(); i++) {
            y++;
            String tallierId = "Tallier" + (i + 1) + "Id";
            String tallierName = "Tallier" + (i + 1);
            if (config.existProperty(tallierId)) {
                tallierName = config.getProperty(tallierId);
            }
            builder.appendRow("top:pref");
            builder.addLabel("Tallier" + (i + 1) + ": ", cellConstraints.xy(1, y));
            JTextField textField = new JTextField(tallierName, 15);
            textField.setName(tallierId);
            textField.getDocument().addDocumentListener(new TextFieldChangeListener(textField));
            builder.add(textField, cellConstraints.xy(2, y));
        }

        panel.add(builder.getPanel());
    }
}
