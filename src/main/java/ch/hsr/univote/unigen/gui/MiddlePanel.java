/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class MiddlePanel extends JPanel {
    private ResourceBundle bundle;
    private JPanel panel, statusBar;
    private JButton prevButton, nextButton, saveButton;
    private JProgressBar progressBar;
    private ConfigurationPanelManager panelManager;
    public static ConfigHelper config;

    //private MainGUI mainGUI;
    public MiddlePanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        config = new ConfigHelper();
        panel = new JPanel();
        panelManager = new ConfigurationPanelManager();
        
        createConfigurationPanel();
        nextPanel();

        this.add(panel);
    }

    public void nextPanel() {
        if (panelManager.getActualNumber() < panelManager.getSize()) {
            panel.remove(panelManager.getActualPanel());
            panel.add(panelManager.getNextPanel(), BorderLayout.CENTER);
            updateProgressBar(panelManager.getActualNumber()-1);
            validate();
            repaint();
        }
    }

    public void previousPanel() {
        if (panelManager.getActualNumber() > 1) {
            panel.remove(panelManager.getActualPanel());
            panel.add(panelManager.getPreviousPanel(), BorderLayout.CENTER);
            updateProgressBar(panelManager.getActualNumber()-1);
            validate();
            repaint();
        }
    }

    private void createConfigurationPanel() {
        panel.setLayout(new BorderLayout());
        createConfigurationStatusBarPanel();
    }

    private void createConfigurationStatusBarPanel() {
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.setBorder(new EtchedBorder());
        //statusBar.setBackground(Color.red);

        prevButton = new JButton();
        prevButton.setText(bundle.getString("previouspage"));
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                previousPanel();
            }
        });
        statusBar.add(prevButton, BorderLayout.WEST);

        saveButton = new JButton();
        saveButton.setText(bundle.getString("save"));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                config.saveProperties(config.CONFIG_FILE);
            }
        });
        statusBar.add(saveButton, BorderLayout.CENTER);
        
        nextButton = new JButton();
        nextButton.setText(bundle.getString("nextpage"));
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                nextPanel();
            }
        });
        statusBar.add(nextButton, BorderLayout.EAST);

        progressBar = new JProgressBar();
        progressBar.setMaximum(panelManager.getSize()-1);
        statusBar.add(progressBar, BorderLayout.SOUTH);

        panel.add(statusBar, BorderLayout.SOUTH);
    }
    
    private void updateProgressBar(int value) {
        progressBar.setValue(value);
        
        if (value == 0) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }
        if (value == panelManager.getSize()-1) {
            nextButton.setEnabled(false);
        } else {
            nextButton.setEnabled(true);
        }
        
    }
}
