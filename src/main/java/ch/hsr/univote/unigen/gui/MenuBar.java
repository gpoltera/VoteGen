/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.helper.FileHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gian
 */
public class MenuBar extends JMenuBar {

    private ResourceBundle resourceBundle;
    private FileHandler fileHandler;
    private MainGUI mainGUI;

    public MenuBar(MainGUI mainGUI) {
        resourceBundle = ResourceBundle.getBundle("Bundle");
        this.fileHandler = new FileHandler();

        createFileMenu();
        createNavigateMenu();
        createLanguageMenu();
        createHelpMenu();

        this.add(Box.createHorizontalGlue());
        this.mainGUI = mainGUI;
    }

    private void createFileMenu() {
        JMenu menu = new JMenu(resourceBundle.getString("file"));

        //OpenItem
        JMenuItem openItem = new JMenuItem(resourceBundle.getString("open"));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser filechooser = new JFileChooser();
                filechooser.setFileFilter(new FileNameExtensionFilter("VoteGenerator Config", "vgc"));
                int state = filechooser.showOpenDialog(null);
                if (state == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    new FileHandler().loadFromExternalFile(file);
                    MiddlePanel.config = new ConfigHelper();
                }
            }
        });
        menu.add(openItem);

        //SaveItem
        JMenuItem saveItem = new JMenuItem(resourceBundle.getString("save"));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JFileChooser filechooser = new JFileChooser();
                filechooser.setFileFilter(new FileNameExtensionFilter("VoteGenerator Config", "vgc"));
                int state = filechooser.showSaveDialog(null);
                if (state == JFileChooser.APPROVE_OPTION) {
                    File file = filechooser.getSelectedFile();
                    new FileHandler().saveInExternalFile(file);
                }
            }
        });
        menu.add(saveItem);

        //ExitItem
        JMenuItem exitItem = new JMenuItem(resourceBundle.getString("exit"));

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
        exitItem.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt
                    ) {
                        System.exit(0);
                    }
                }
        );
        menu.add(exitItem);

        this.add(menu);
    }

    private void createLanguageMenu() {
        JMenu menu = new JMenu(resourceBundle.getString("language"));

        //GermanItem
        JMenuItem germanItem = new JMenuItem(resourceBundle.getString("german"));
        germanItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
        germanItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //Change to german
                Locale locale = new Locale("de", "DE");
                Locale.setDefault(locale);
                mainGUI.resetContentPanel();
            }
        });
        menu.add(germanItem);

        //EnglishItem
        JMenuItem englishItem = new JMenuItem(resourceBundle.getString("english"));
        englishItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        englishItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //Change to english
                Locale locale = new Locale("en", "EN");
                Locale.setDefault(locale);
                mainGUI.resetContentPanel();
            }
        });
        menu.add(englishItem);

        this.add(menu);
    }

    private void createNavigateMenu() {
        JMenu menu = new JMenu(resourceBundle.getString("navigate"));

        //Back to start item
        JMenuItem backtostart = new JMenuItem(resourceBundle.getString("backtostart"));
        backtostart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                mainGUI.resetContentPanel();
            }
        });
        menu.add(backtostart);

        this.add(menu);
    }

    private void createHelpMenu() {
        JMenu menu = new JMenu(resourceBundle.getString("help"));

        //DocumentationItem
        JMenuItem documentationItem = new JMenuItem(resourceBundle.getString("documentation"));
        documentationItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
        documentationItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    String exec = "rundll32 url.dll,FileProtocolHandler \"" + fileHandler.documentationPDFPath + "\"";
                    Process pc = Runtime.getRuntime().exec(exec);
                } catch (IOException ex) {
                    System.out.println("NO WINDOWS, OR NO PDF READER");
                }
            }
        });
        menu.add(documentationItem);

        //AboutItem
        JMenuItem aboutItem = new JMenuItem(resourceBundle.getString("about"));
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(null,
                        "Copyright:\nGian Poltéra 2013-2014\n\nVersion 1.0",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(aboutItem);

        this.add(menu);
    }
}
