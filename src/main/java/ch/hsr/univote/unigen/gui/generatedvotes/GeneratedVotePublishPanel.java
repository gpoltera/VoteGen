/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.generatedvotes;

import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ResourceBundle;
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
    private ElectionBoard electionBoard;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JTextPane textPane;

    public GeneratedVotePublishPanel(ElectionBoard electionBoard) {
        this.bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        this.electionBoard = electionBoard;

        panel = new JPanel();
        createGeneratedVotePublishPanel();

        this.add(panel);
        this.setName(bundle.getString("publishingelection"));
    }

    private void createGeneratedVotePublishPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("publishingelection"));

        scrollPane = new JScrollPane();
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText(bundle.getString("webservicestart"));
        textPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setViewportView(textPane);

        panel.add(scrollPane);
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
