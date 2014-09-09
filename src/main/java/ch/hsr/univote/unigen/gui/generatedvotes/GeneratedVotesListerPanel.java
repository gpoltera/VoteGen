/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.generatedvotes;

import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.db.DBElectionBoardManager;
import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.helper.FileHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private String[][] dbs;

    public GeneratedVotesListerPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        panel = new JPanel();
        dbs = new FileHandler().getElectionDBFileList();

        createGeneratedVotesListerPanel();
        MiddlePanel.title.setText(bundle.getString("loadgenerateelection"));
        
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

        for (int i = 0; i < dbs.length; i++) {
            y++;
            builder.appendRow("center:pref");
            JButton button = createLoadButton(dbs[i][0]);
            builder.addLabel(dbs[i][0], cellConstraints.xy(1, y));
            builder.addLabel(dbs[i][1], cellConstraints.xy(3, y)); //Anpassen
            builder.add(button, cellConstraints.xy(5, y));
        }

        panel.add(builder.getPanel());
    }

    private JButton createLoadButton(final String db) {
        JButton button = new JButton();
        button.setText(bundle.getString("load"));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                loadGeneratedVotePublishPanel(new DBElectionBoardManager().loadElectionBoard(db));
            }
        });

        return button;
    }

    private void loadGeneratedVotePublishPanel(ElectionBoard electionBoard) {
        this.remove(panel);
        this.add(new GeneratedVotePublishPanel(electionBoard));
        validate();
        repaint();
    }
}
