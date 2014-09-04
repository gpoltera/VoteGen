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
    private JButton button1, button2;

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

        button1 = new JButton();
        button1.setText(bundle.getString("newelection"));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            }
        });
        panel.add(button1);
        
        button2 = new JButton();
        button2.setText(bundle.getString("newelection"));
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
            }
        });
        panel.add(button2);
    }
}
