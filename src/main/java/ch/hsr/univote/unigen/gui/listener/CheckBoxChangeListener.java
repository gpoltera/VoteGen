/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui.listener;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Gian
 */
public class CheckBoxChangeListener implements ChangeListener {
    private ConfigHelper config;
    private JCheckBox checkBox;
    
    public CheckBoxChangeListener(JCheckBox checkBox) {
        this.config = MiddlePanel.config;
        this.checkBox = checkBox;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        config.setProperty(checkBox.getName(), String.valueOf(checkBox.isSelected()));
    }   
}
