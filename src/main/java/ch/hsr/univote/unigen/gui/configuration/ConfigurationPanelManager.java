/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.configuration;

import ch.hsr.univote.unigen.gui.configuration.AdminsConfigurationPanel;
import ch.hsr.univote.unigen.gui.configuration.CandidatesConfigurationPanel;
import ch.hsr.univote.unigen.gui.configuration.InitialConfigurationPanel;
import ch.hsr.univote.unigen.gui.configuration.FailureConfigurationPanel;
import ch.hsr.univote.unigen.gui.configuration.CryptoConfigurationPanel;
import ch.hsr.univote.unigen.gui.configuration.FinishConfigurationPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author Gian
 */
public class ConfigurationPanelManager {

    private List<JPanel> panels;
    private List<Class> panelsclass;
    private int iterator;

    public ConfigurationPanelManager() {
        panels = new ArrayList<>();
        panelsclass = new ArrayList<>();
        iterator = 0;

        panelsclass.add(0, InitialConfigurationPanel.class);
        panelsclass.add(1, AdminsConfigurationPanel.class);
        panelsclass.add(2, CandidatesConfigurationPanel.class);
        panelsclass.add(3, CryptoConfigurationPanel.class);
        panelsclass.add(4, FailureConfigurationPanel.class);
        panelsclass.add(5, FinishConfigurationPanel.class);
    }

    public int getSize() {
        return panelsclass.size();
    }

    public int getActualNumber() {
        return iterator;
    }

    public String getActualTitle() {
        String title = new String();
        if (iterator > 0) {
            title = panels.get(iterator - 1).getName();
        }
        return title;
    }

    public JPanel getActualPanel() {
        JPanel panel = new JPanel();
        if (iterator > 0) {
            panel = panels.get(iterator - 1);
        }

        return panel;
    }

    public JPanel getNextPanel() {
        JPanel panel = new JPanel();
        if (iterator < panelsclass.size()) {
            panel = getPanelFromClass(panelsclass.get(iterator));
            panels.add(panel);
            iterator++;
        }

        return panel;
    }

    public JPanel getPreviousPanel() {
        JPanel panel = new JPanel();
        if (iterator > 1) {
            panel = panels.get(iterator - 2);
            removePanel(panels.get(iterator - 1));
            iterator--;
        }

        return panel;

    }

    private void removePanel(JPanel panel) {
        if (panels.contains(panel)) {
            panels.remove(panel);
        }
    }

    private JPanel getPanelFromClass(Class classname) {
        JPanel panel = new JPanel();
        try {
            panel = (JPanel) classname.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ConfigurationPanelManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return panel;
    }
}
