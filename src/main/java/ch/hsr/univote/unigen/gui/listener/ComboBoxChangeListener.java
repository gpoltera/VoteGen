/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui.listener;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;

/**
 *
 * @author Gian
 */
public class ComboBoxChangeListener implements ItemListener {
    private ConfigHelper config;
    private JComboBox comboBox;
    
    public ComboBoxChangeListener(JComboBox comboBox) {
        this.config = MiddlePanel.config;
        this.comboBox = comboBox;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        config.setProperty(comboBox.getName(), comboBox.getSelectedItem().toString());
    } 
}
