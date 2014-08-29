/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui.configuration;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import static ch.hsr.univote.unigen.gui.MiddlePanel.config;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian Poltéra
 */
public class FinishConfigurationPanel extends JPanel {
    
    private ConfigHelper config;
    private ResourceBundle bundle;
    private JPanel panel;
    
    public FinishConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();
        
        createFinishConfigurationPanel();
        
        this.add(panel);
        this.setName(bundle.getString("finishconfiguration"));
    }
    
    private void createFinishConfigurationPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("finishconfiguration"));        
    }
}
