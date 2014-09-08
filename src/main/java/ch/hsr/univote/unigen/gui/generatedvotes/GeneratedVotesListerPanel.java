/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.generatedvotes;

import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.Publisher;
import ch.hsr.univote.unigen.db.DBElectionBoardManager;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class GeneratedVotesListerPanel extends JPanel {

    private ResourceBundle bundle;
    private JPanel panel;
    private File file;
    private List<String> dbs;

    public GeneratedVotesListerPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        panel = new JPanel();
        file = new File("db/");
        dbs = getFiles(file);

        createGeneratedVotesListerPanel();

        this.add(panel);
        this.setName(bundle.getString("loadgenerateelection"));
    }

    private void createGeneratedVotesListerPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName(bundle.getString("loadgenerateelection"));

        PanelBuilder builder = new PanelBuilder(new FormLayout(""));
        builder.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        builder.appendColumn("left:pref");
        builder.appendColumn("20dlu");
        builder.appendColumn("left:pref");
        builder.appendColumn("20dlu");
        builder.appendColumn("fill:max(pref; 100px)");

        CellConstraints cellConstraints = new CellConstraints();

        int y = 0;
        y++;
        builder.appendRow("center:pref");
        builder.addTitle("<html><b>" + bundle.getString("election") + "</b></html>", cellConstraints.xy(1, y));
        builder.addTitle("<html><b>" + bundle.getString("size") + "</b></html>", cellConstraints.xy(3, y));
        builder.addTitle("<html><b>" + bundle.getString("load") + "</b></html>", cellConstraints.xy(5, y));
        y++;
        builder.appendRow("center:pref");
        builder.addSeparator("", cellConstraints.xyw(1, y, 5));
        
        for (String db : dbs) {
            y++;
            builder.appendRow("center:pref");
            JButton button = createLoadButton(db);
            builder.addLabel(db, cellConstraints.xy(1, y));
            builder.addLabel((int) (Math.random() * (200 - 10) + 10) + " MB", cellConstraints.xy(3, y)); //Anpassen
            builder.add(button, cellConstraints.xy(5, y));
        }
        
        panel.add(builder.getPanel());
    }

    private JButton createLoadButton(String db) {
        JButton button = new JButton();
        button.setText(bundle.getString("load"));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ElectionBoard electionBoard = new DBElectionBoardManager().loadElectionBoard(db);
                Publisher publisher = new Publisher(electionBoard);
                publisher.startWebSrv();
            }
        });

        return button;
    }

    private List<String> getFiles(File dir) {
        List<String> filelist = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) { // Permissions are available
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    if (files[i].getName().endsWith("db") && !files[i].getName().startsWith("config")) {
                        filelist.add(files[i].getName());
                    }
                }
            }
        }
        return filelist;
    }
}
