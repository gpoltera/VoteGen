/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.gui.configuration.ConfigurationPanelManager;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.gui.generatedvotes.GeneratedVotesListerPanel;
import ch.hsr.univote.unigen.gui.generatedvotes.VoteGenerationPanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class MiddlePanel extends JPanel {

    private ResourceBundle bundle;
    private JPanel panel, startpage, statusBar, titleBar;
    private VoteGenerationPanel voteGeneration;
    private JButton prevButton, nextButton, saveButton, generateButton;
    public static JLabel title;
    private JProgressBar progressBar;
    private ConfigurationPanelManager panelManager;
    public static ConfigHelper config;

    //private MainGUI mainGUI;
    public MiddlePanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        config = new ConfigHelper();
        panel = new JPanel();
        panelManager = new ConfigurationPanelManager();
        panel.setLayout(new BorderLayout());
        createTitleBarPanel();
        createStartpagePanel();

        this.add(panel);
    }

    private void createTitleBarPanel() {
        titleBar = new JPanel();

        title = new JLabel();
        title.setText(bundle.getString("title"));
        title.setFont(new Font("Tahoma", 1, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titleBar.add(title);

        panel.add(titleBar, BorderLayout.NORTH);
    }

    private void updateTitle(String name) {
        title.setText(name);
    }

    private void createStartpagePanel() {
        startpage = new JPanel();
        startpage.setLayout(new BorderLayout());
        startpage.setBackground(Color.white);

        JLabel imgLabel = new JLabel();
        URL img = getClass().getClassLoader().getResource("images/iconVoteGenerator.jpg");
        if (img != null) {
            ImageIcon logo = new ImageIcon(img);
            imgLabel = new JLabel(logo);
//            imgLabel.setMaximumSize(new Dimension(300, 114));
            imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);          
        }
        startpage.add(imgLabel, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton button1 = new JButton();
        button1.setText(bundle.getString("startconfiguration"));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createConfigurationPanel();
            }
        });
        buttons.add(button1);

        JButton button2 = new JButton();
        button2.setText(bundle.getString("loadgeneratedelection"));
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                createGeneratedVotesPanel();
            }
        });
        buttons.add(button2);
        
        startpage.add(buttons, BorderLayout.SOUTH);

        panel.add(startpage);
    }

    private void createConfigurationPanel() {
        panel.remove(startpage);
        createConfigurationStatusBarPanel();
        nextPanel();
    }

    private void createGeneratedVotesPanel() {
        panel.remove(startpage);
        panel.add(new GeneratedVotesListerPanel());
        title.setText(bundle.getString("loadgenerateelection"));
        validate();
        repaint();
    }

    private void createVoteGenerationPanel() {
        panel.remove(panelManager.getActualPanel());

        voteGeneration = new VoteGenerationPanel();

        voteGeneration.setName(bundle.getString("generatevote"));

        panel.add(voteGeneration);
        validate();
        repaint();
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
                config.saveProperties();
            }
        });
        statusBar.add(saveButton, BorderLayout.CENTER);

        generateButton = new JButton();
        generateButton.setText(bundle.getString("generatevote"));
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                config.saveProperties();
                removeConfigurationStatusBarPanel();
                createVoteGenerationPanel();
            }
        });

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
        progressBar.setMaximum(panelManager.getSize() - 1);
        statusBar.add(progressBar, BorderLayout.SOUTH);

        panel.add(statusBar, BorderLayout.SOUTH);
    }

    private void removeConfigurationStatusBarPanel() {
        statusBar.removeAll();
        validate();
        repaint();
    }

    public void nextPanel() {
        if (panelManager.getActualNumber() < panelManager.getSize()) {
            panel.remove(panelManager.getActualPanel());
            panel.add(panelManager.getNextPanel(), BorderLayout.CENTER);
            updateTitle(panelManager.getActualTitle());
            updateProgressBar(panelManager.getActualNumber() - 1);
            validate();
            repaint();
        }
    }

    public void previousPanel() {
        if (panelManager.getActualNumber() > 1) {
            panel.remove(panelManager.getActualPanel());
            panel.add(panelManager.getPreviousPanel(), BorderLayout.CENTER);
            updateTitle(panelManager.getActualTitle());
            updateProgressBar(panelManager.getActualNumber() - 1);
            validate();
            repaint();
        }
    }

    private void updateProgressBar(int value) {
        progressBar.setValue(value);

        if (value == 0) {
            prevButton.setEnabled(false);
        } else {
            prevButton.setEnabled(true);
        }
        if (value == panelManager.getSize() - 1) {
            nextButton.setEnabled(false);
            statusBar.remove(saveButton);
            statusBar.add(generateButton, BorderLayout.CENTER);
        } else {
            nextButton.setEnabled(true);
        }
    }
}
