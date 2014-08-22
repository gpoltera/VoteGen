/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui.listener;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Gian
 */
public class SpinnerChangeListener implements ChangeListener {
    private ConfigHelper config;
    private JSpinner spinner;
    
    public SpinnerChangeListener(JSpinner spinner) {
        this.config = MiddlePanel.config;
        this.spinner = spinner;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        config.setProperty(spinner.getName(), spinner.getValue().toString());
    }
}
