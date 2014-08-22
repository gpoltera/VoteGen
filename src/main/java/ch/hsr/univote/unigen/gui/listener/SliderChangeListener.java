/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.hsr.univote.unigen.gui.listener;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Gian
 */
public class SliderChangeListener implements ChangeListener {
    private ConfigHelper config;
    private JSlider slider;
    
    public SliderChangeListener(JSlider slider) {
        this.config = MiddlePanel.config;
        this.slider = slider;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        config.setProperty(slider.getName(), Integer.toString(slider.getValue()));
    }    
}
