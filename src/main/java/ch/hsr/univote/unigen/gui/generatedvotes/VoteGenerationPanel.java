/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.generatedvotes;

import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DBElectionBoardManager;
import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Gian Poltéra
 */
public class VoteGenerationPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private VoteGenerator voteGenerator;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextPane textPane;
    private JButton startButton, stopButton, webserviceButton;
    private Thread voteGeneratorThread;

    public VoteGenerationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();

        createVoteGenerationPanel();
        MiddlePanel.title.setText(bundle.getString("votegeneration"));
        this.add(panel);
        this.setName(bundle.getString("votegeneration"));
    }

    private void createVoteGenerationPanel() {
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("votegeneration"));

        scrollPane = new JScrollPane();
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setViewportView(textPane);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();

        //Start VoteGeneration Button
        startButton = new JButton();
        startButton.setText(bundle.getString("startgeneration"));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                textPane.setText(bundle.getString("votewillbegenerated"));
                startButton.setEnabled(false);
                startGenerationThread();
                stopButton.setEnabled(true);
            }
        });

        //Webserivce Button
        webserviceButton = new JButton();
        webserviceButton.setText(bundle.getString("startwebservice"));
        webserviceButton.setEnabled(false);
        webserviceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                startButton.setEnabled(false);
                stopButton.setEnabled(false);
                webserviceButton.setEnabled(false);
                loadGeneratedVotePublishPanel();
            }
        });

        //Stop VoteGeneration Button
        stopButton = new JButton();
        stopButton.setText(bundle.getString("stopgeneration"));
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                appendText(bundle.getString("votewillbestopped"));
                stopButton.setEnabled(false);
                webserviceButton.setEnabled(false);
                voteGeneratorThread.stop();
                startButton.setEnabled(true);
            }
        });

        buttonsPanel.add(startButton);
        buttonsPanel.add(webserviceButton);
        buttonsPanel.add(stopButton);

        return buttonsPanel;
    }

    private void loadGeneratedVotePublishPanel() {
        this.remove(panel);
        this.add(new GeneratedVotePublishPanel(voteGenerator.getElectionBoard()));
        validate();
        repaint();        
    }
    
    private void startGenerationThread() {
        voteGenerator = new VoteGenerator(this);
        voteGeneratorThread = new Thread() {
            public void run() {
                voteGenerator.run();
            }
        };
        voteGeneratorThread.start();
    }

    public void generationIsComplite() {
        stopButton.setEnabled(false);
        webserviceButton.setEnabled(true);
        voteGeneratorThread.stop();
    }

    public void appendText(String text) {
        StyledDocument document = textPane.getStyledDocument();
        try {
            document.insertString(document.getLength(), "\n" + text, null);
        } catch (BadLocationException e) {
            System.out.println(e);
        }
    }

    public void appendSuccess() {
        StyledDocument document = textPane.getStyledDocument();

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.GREEN);
        StyleConstants.setBold(keyWord, true);

        try {
            document.insertString(document.getLength(), "\n" + bundle.getString("success"), keyWord);
        } catch (BadLocationException e) {
            System.out.println(e);
        }
    }

    public void appendFailure(String text) {
        StyledDocument document = textPane.getStyledDocument();

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, Color.RED);
        StyleConstants.setBold(keyWord, true);

        try {
            document.insertString(document.getLength(), "\n" + bundle.getString("failure") + ": " + text, keyWord);
        } catch (BadLocationException e) {
            System.out.println(e);
        }
    }
}
