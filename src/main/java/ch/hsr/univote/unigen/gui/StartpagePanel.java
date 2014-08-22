/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class StartpagePanel extends JPanel {

    private ResourceBundle bundle;
    private JPanel panel;
    private JButton button;

    public StartpagePanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        panel = new JPanel();
        createStartpagePanel();
        this.add(panel);
    }

    private void createStartpagePanel() {
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EtchedBorder());

        button = new JButton();
        button.setText(bundle.getString("newelection"));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            }
        });
        panel.add(button);
    }
}
