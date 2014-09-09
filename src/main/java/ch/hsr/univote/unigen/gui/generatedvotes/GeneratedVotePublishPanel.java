/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.generatedvotes;

import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.Publisher;
import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.FileHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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
public class GeneratedVotePublishPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private FileHandler fileHandler;
    private ElectionBoard electionBoard;
    private Publisher publisher;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextPane textPane;
    private JButton startButton, stopButton, voteVerifierButton;

    public GeneratedVotePublishPanel(ElectionBoard electionBoard) {
        this.bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        this.electionBoard = electionBoard;
        this.fileHandler = new FileHandler();

        panel = new JPanel();
        createGeneratedVotePublishPanel();

        MiddlePanel.title.setText(bundle.getString("publishingelection"));
        
        this.add(panel);
        this.setName(bundle.getString("publishingelection"));
    }

    private void createGeneratedVotePublishPanel() {
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("publishingelection"));

        scrollPane = new JScrollPane();
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setPreferredSize(new Dimension(450, 450));
        textPane.setText("ElectionId: " + electionBoard.getElectionSystemInfo().getElectionId());
        scrollPane.setViewportView(textPane);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createButtonsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();

        //Start WebService Button
        startButton = new JButton();
        startButton.setText(bundle.getString("startwebservice"));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                appendText(bundle.getString("webservicestart"));
                startButton.setEnabled(false);
                initialPublisher();
                publisher.startWebSrv();
            }
        });

        //Start VoteVerifier Button
        voteVerifierButton = new JButton();
        voteVerifierButton.setText(bundle.getString("startvoteverifier"));
        voteVerifierButton.setEnabled(false);
        voteVerifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                voteVerifierButton.setEnabled(false);
                startVoteVerifier();
            }
        });

        //Stop WebService Button
        stopButton = new JButton();
        stopButton.setText(bundle.getString("stopwebservice"));
        stopButton.setEnabled(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                appendText(bundle.getString("webservicestop"));
                stopButton.setEnabled(false);
                voteVerifierButton.setEnabled(false);
                publisher.stopWebSrv();
            }
        });

        buttonsPanel.add(startButton);
        buttonsPanel.add(voteVerifierButton);
        buttonsPanel.add(stopButton);

        return buttonsPanel;
    }

    private void startVoteVerifier() {    
        try {            
            String exec = "java -jar \"" + fileHandler.voteVerifierJARPath + "\"";
            Process pc = Runtime.getRuntime().exec(exec);
        } catch (IOException ex) {
            appendFailure(ex.toString());
        }
    }

    private void initialPublisher() {
        publisher = new Publisher(electionBoard, this);
    }

    public void webserviceIsStarted() {
        stopButton.setEnabled(true);
        voteVerifierButton.setEnabled(true);
    }

    public void webserviceIsStopped() {
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        voteVerifierButton.setEnabled(false);
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
