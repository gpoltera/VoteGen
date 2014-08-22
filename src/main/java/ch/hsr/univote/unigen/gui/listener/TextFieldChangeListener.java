/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.listener;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Gian
 */
public class TextFieldChangeListener implements DocumentListener {

    private ConfigHelper config;
    private JTextField textField;

    public TextFieldChangeListener(JTextField textField) {
        this.config = MiddlePanel.config;
        this.textField = textField;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        textField.setBackground(Color.white);
        config.setProperty(textField.getName(), textField.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        textField.setBackground(Color.red);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}
