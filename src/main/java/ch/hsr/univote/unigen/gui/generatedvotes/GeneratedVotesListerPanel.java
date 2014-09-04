/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui.generatedvotes;

import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.Publisher;
import ch.hsr.univote.unigen.db.DBElectionBoardManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Gian
 */
public class GeneratedVotesListerPanel extends JPanel {

    private ResourceBundle bundle;
    private JPanel panel;
    private JTextArea textArea;
    private File file;
    private List<String> dbs;

    public GeneratedVotesListerPanel() {
        bundle = ResourceBundle.getBundle("Bundle");
        panel = new JPanel();
        textArea = new JTextArea();
        file = new File("db/");
        dbs = getFiles(file);
        
        createGeneratedVotesListerPanel();
        
        this.add(panel);
        this.setName("test");
    }

    private void createGeneratedVotesListerPanel() {
        panel.setBorder(new EtchedBorder());
        panel.setName("test");
        
        panel.add(textArea);
        
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ElectionBoard electionBoard = new DBElectionBoardManager().loadElectionBoard(dbs.get(0));
                Publisher publisher = new Publisher(electionBoard);
                publisher.startWebSrv();
            }
        });
        panel.add(button);
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
