/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 *
 * @author Gian
 */
public class MainGUI extends JFrame {

    // Variables declaration   
    private JPanel masterPanel;
    //private TopPanel topPanel;
    private MiddlePanel middlePanel;
    //private ConsolePanel consolePanel;
    //private ResultTabbedPane resultTabbedPane;
    //private VerificationListener verificationListener;
    private Preferences prefs;
    private static final Logger LOGGER = Logger.getLogger(MainGUI.class.getName());
    private ResourceBundle resourceBundle;
    //private ResultProcessor resultProccessor;
    //private ThreadManager threadManager;
    //private MessengerManager messengerManager;
    private JLabel splash;

    /**
     * Construct the window and frame of the GUI
     */
    public MainGUI() {
        //threadManager  = new ThreadManager();
        initResources();
        setLookAndFeel();

        URL imgURL = getClass().getClassLoader().getResource("iconVoteGenerator.jpg");
        ImageIcon img = new ImageIcon(imgURL);
        this.setIconImage(img.getImage()); //GUI Icon
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));
        this.setLocationRelativeTo(null); //Mittig zentrieren

        //intro/loader
        JPanel splashPanel = new JPanel();
        splashPanel.setLayout(new BorderLayout());
        splash = getSplashImage();
        splashPanel.add(splash, BorderLayout.CENTER);
        splashPanel.setBackground(Color.WHITE);
        JProgressBar jProgressBar = new JProgressBar();
        jProgressBar.setIndeterminate(true);
        splashPanel.add(jProgressBar, BorderLayout.SOUTH);
        this.setContentPane(splashPanel);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //load the gui
        createContentPanel();

        this.setJMenuBar(new MenuBar(this));
        this.setTitle("VoteGenerator");
        this.pack();
    }

    /**
     * Toggle the visibility of the console-like panel which contains a
     * JTextArea.
     *
     * @param show Boolean of true corresponds to showing the panel.
     */
    public void showConsole(boolean show) {
        if (show) {
            //this.getContentPane().add(consolePanel);
        } else {
            //this.getContentPane().remove(consolePanel);
        }
        this.validate();
        this.repaint();
    }

    /**
     * create the main content panel for this Frame Class.
     */
    private void createContentPanel() {
        resetContentPanel();

    }

    /**
     * Create or recreate the main content panel for this Frame Class. This
     * method is called when the program starts as well as if a change of locale
     * is needed. The entire GUI will is recreated.
     */
    public void resetContentPanel() {
        //threadManager.killAllThreads();

        masterPanel = createUI();
        masterPanel.setOpaque(true); //content panes must be opaque
        initResources();

        this.setContentPane(masterPanel);
        this.setJMenuBar(new MenuBar(this));
        this.validate();
        this.repaint();
    }

    /**
     * Create the panel, which contains the title image.
     *
     * @return a JPanel title image
     */
    private JLabel getSplashImage() {
        JLabel imgLabel = new JLabel();
        java.net.URL img = getClass().getClassLoader().getResource("iconVoteGenerator.jpg");
        if (img != null) {
            ImageIcon logo = new ImageIcon(img);
            imgLabel = new JLabel(logo);
//            imgLabel.setMaximumSize(new Dimension(300, 114));
            imgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        } else {
            LOGGER.log(Level.INFO, "IMAGE NOT FOUND");
        }
        return imgLabel;
    }

    /**
     * Instantiates some basic resources needed in this class.
     */
    private void initResources() {
        //resourceBundle = ResourceBundle.getBundle("Bundle", GUIConfiguration.getLocale());
        //verificationListener = new StatusUpdate();
        //messengerManager = new MessengerManager(verificationListener);
    }

    /**
     * Creates the main components of the main window. The main window is
     * divided into three parts: topPanel, middlePanel, and optionally a
     * consolePanel can be added at the bottom.
     *
     * @return a JPanel which will be set as the main content panel of the frame
     */
    public JPanel createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //consolePanel = new ConsolePanel();

        //resultTabbedPane = new ResultTabbedPane(threadManager, consolePanel);
        //resultProccessor = new ResultProcessor(consolePanel, resultTabbedPane);

        middlePanel = new MiddlePanel();
        //topPanel = new TopPanel();

        //boolean networkUp = true;
        //String[] eidList = getElectionIDList();
        //if (eidList == null) {
        //    eidList = new String[3];
        //    eidList[0] = "vsbfh-2013";
        //    networkUp = false;
        //}

        //middlePanel = new MiddlePanel(resultTabbedPane, eidList, mm, tm, networkUp);
        //middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        //middlePanel.setBackground(GUIconstants.GREY);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.X_AXIS));
        //middlePanel.setBackground(GUIconstants.GREY);
        
        panel.add(middlePanel);
        //panel.add(middlePanel);

        return panel;
    }

    /**
     * This inner class represents the implementation of the observer pattern
     * for the status messages for the console and verification parts of the
     * GUI.
     *
     * @author Justin Springer
     */
    //class StatusUpdate implements VerificationListener {

        //@Override
        //public void updateStatus(VerificationEvent ve) {
           // if (ve.getVm() == VerificationMessage.ELECTION_SPECIFIC_ERROR) {
           //     resultTabbedPane.showElectionSpecError(ve.getMsg(), ve.getProcessID());
           //     consolePanel.appendToStatusText("\n" + ve.getMsg(), ve.getProcessID());
          //  } else if (ve.getVm() == VerificationMessage.SETUP_ERROR) {
            //    String text = "\n" + ve.getMsg();
            //    middlePanel.setupErrorMsg(text);
            // else if (ve.getVm() == VerificationMessage.FILE_SELECTED) {
           //     middlePanel.showFileSelected(ve.getMsg());
           // } else if (ve.getVm() == VerificationMessage.SHOW_CONSOLE) {
            //    showConsole(ve.getConsoleSelected());
            //} else if (ve.getVm() == VerificationMessage.VRF_FINISHED) {
              //  String processID = ve.getProcessID();
              //  threadManager.killThread(processID);
              //  resultTabbedPane.completeVerification(processID);
           // } else {
                //String processID = ve.getProcessID();
                //if (resultsHaveValidThread(processID)) {
                   // resultProccessor.showResultInGUI(ve);
                //}

            //}
        //}
    //}

    /**
     * Check if the results that are coming in should be displayed. This
     * requires that the process that they belong to is active, and no command
     * to cancel it has been issued.
     * @param processID
     * @return 
     */
   // public boolean resultsHaveValidThread(String processID) {
    //    return threadManager.hasThreadWithProcessID(processID);
    //}

    /**
     * Set the look and feel of the GUI to the default of the system the program
     * is running on.
     */
    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainGUI.class
                    .getName()).log(Level.SEVERE, ex.getMessage());
        }
    }
}
