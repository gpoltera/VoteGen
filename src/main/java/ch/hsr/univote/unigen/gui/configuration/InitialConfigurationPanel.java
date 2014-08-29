/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.configuration;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.gui.listener.SpinnerChangeListener;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class InitialConfigurationPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private JPanel panel;
    private List<String> labels;

    public InitialConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();
        createInitialConfigurationPanel();
        this.add(panel);
        this.setName(bundle.getString("initialconfiguration"));
    }

    private void createInitialConfigurationPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("initialconfiguration"));

        labels = new ArrayList<>();
        labels.add("electionId");
        labels.add("electionTitle");
        labels.add("Voters");
        labels.add("Mixers");
        labels.add("Talliers");
        labels.add("candidates");
        labels.add("lists");
        labels.add("maxCandidates");
        labels.add("maxCumulation");

        PanelBuilder builder = new PanelBuilder(new FormLayout(""));
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("left:pref");
        builder.appendColumn("fill:max(pref; 100px)");

        CellConstraints cellConstraints = new CellConstraints();

        int y = 0;

        //Title
        y++;
        builder.appendRow("top:pref");
        builder.addTitle("TEST", cellConstraints.xyw(1, y, 2));
        y++;
        builder.appendRow("top:pref");
        builder.addSeparator("", cellConstraints.xyw(1, y, 2));

        //Text
        for (int i = 0; i < 2; i++) {
            y++;
            String key = labels.get(i);
            String value = labels.get(i);
            if (config.existProperty(key)) {
                value = config.getProperty(key);
            }
                      
            builder.appendRow("top:pref");
            builder.addLabel(bundle.getString(key) + ": ", cellConstraints.xy(1, y));
            JTextField textField = new JTextField(value, 15);
            textField.setName(key);
            textField.getDocument().addDocumentListener(new TextFieldChangeListener(textField));
            builder.add(textField, cellConstraints.xy(2, y));
        }

        //Spinner
        for (int i = 2; i < labels.size(); i++) {
            y++;
            String key = labels.get(i);
            int value = 5;
            if (config.existProperty(key)) {
                value = Integer.parseInt(config.getProperty(key));
            }
            
            builder.appendRow("top:pref");
            builder.addLabel(bundle.getString(key) + ": ", cellConstraints.xy(1, y));
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, 1, 1000, 1));
            spinner.setName(key);
            spinner.addChangeListener(new SpinnerChangeListener(spinner));
            builder.add(spinner, cellConstraints.xy(2, y));
        }

        panel.add(builder.getPanel());
    }
}
