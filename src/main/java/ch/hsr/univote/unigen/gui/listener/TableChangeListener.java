/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.listener;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author Gian
 */
public class TableChangeListener implements TableModelListener {

    private ConfigHelper config;
    private JTable table;

    public TableChangeListener(JTable table) {
        this.config = MiddlePanel.config;
        this.table = table;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        for (int i = 0; i < table.getRowCount(); i++) {
            String candidate = table.getValueAt(i, 0).toString()
                    + "," + table.getValueAt(i, 1).toString()
                    + "," + table.getValueAt(i, 2).toString()
                    + "," + table.getValueAt(i, 3).toString()
                    + "," + table.getValueAt(i, 4).toString();
            config.setProperty("candidate" + (i + 1), candidate);
        }
    }

}
