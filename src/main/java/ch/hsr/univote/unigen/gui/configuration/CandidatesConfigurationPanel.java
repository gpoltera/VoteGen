/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.configuration;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.gui.listener.TableChangeListener;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ResourceBundle;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gian
 */
public class CandidatesConfigurationPanel extends JPanel {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private JPanel panel;
    private JTable table;

    public CandidatesConfigurationPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        panel = new JPanel();

        createCandidatesConfigurationPanel();

        this.add(panel);
        this.setName(bundle.getString("candidatesconfiguration"));
    }

    private void createCandidatesConfigurationPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("candidatesconfiguration"));

        JComboBox genderComboBox = new JComboBox();
        genderComboBox.addItem(bundle.getString("male"));
        genderComboBox.addItem(bundle.getString("female"));

        JComboBox borninComboBox = new JComboBox();
        for (int i = 1900; i <= 2014; i++) {
            borninComboBox.addItem(i);
        }
        
        JComboBox listComboBox = new JComboBox();
        for (int i = 0; i < config.getListsNumber(); i++) {
            listComboBox.addItem((i+1));
        }

        String[] headers = {bundle.getString("number"), bundle.getString("name"), bundle.getString("firstname"), bundle.getString("gender"), bundle.getString("bornin"), bundle.getString("list")};
        final Class[] types = new Class[]{String.class, String.class, String.class, Object.class, Object.class, Object.class};
        final Boolean[] editable = {false, true, true, true, true, true};

        Object[][] data = new Object[config.getCandidatesNumber()][headers.length];
        for (int i = 0; i < config.getCandidatesNumber(); i++) {
            String name = "Name" + (i + 1);
            String firstname = "Firstname" + (i + 1);
            Object gender = genderComboBox.getItemAt(0);
            Object bornin = borninComboBox.getItemAt(87);
            Object list = listComboBox.getItemAt(0);
            
            if (config.existProperty("candidate" + (i + 1))) {
                String[] candidate = config.getCandidate(i + 1);
                name = candidate[1]; 
                firstname = candidate[2];
                if(candidate[3].equals(genderComboBox.getItemAt(0).toString())) {
                    gender = genderComboBox.getItemAt(0);
                } else {
                    gender = genderComboBox.getItemAt(1);
                }
                bornin = borninComboBox.getItemAt(Integer.parseInt(candidate[4]) - 1900);
                if(Integer.parseInt(candidate[5]) <= listComboBox.getItemCount()) {
                    list = listComboBox.getItemAt(Integer.parseInt(candidate[5]) - 1);
                }
            }
     
            data[i][0] = i + 1;
            data[i][1] = name;
            data[i][2] = firstname;
            data[i][3] = gender;
            data[i][4] = bornin;
            data[i][5] = list;
        }

        Container container = new Container();
        DefaultTableModel model = new DefaultTableModel(data, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable[column];
            }

            @Override
            public Class getColumnClass(int column) {
                return types[column];
            }
        };

        table = new JTable(model);

        //Gender selector
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(genderComboBox));

        //Vintage selector
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(borninComboBox));
        
        //List selector
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(listComboBox));

        table.setFillsViewportHeight(true);
        table.getModel().addTableModelListener(new TableChangeListener(table));

        container.setLayout(new BorderLayout());
        container.add(table.getTableHeader(), BorderLayout.PAGE_START);
        container.add(table, BorderLayout.CENTER);

        panel.add(container);
    }
}
