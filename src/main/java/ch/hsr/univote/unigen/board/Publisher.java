/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

import ch.hsr.univote.unigen.gui.MiddlePanel;
import ch.hsr.univote.unigen.gui.generatedvotes.GeneratedVotePublishPanel;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ResourceBundle;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Gian
 */
public class Publisher {

    private ConfigHelper config;
    private ResourceBundle bundle;
    private ElectionBoard electionBoard;
    private Endpoint endpoint;
    private GeneratedVotePublishPanel generatedVotePublishPanel;
    private String hostname;
    private int port;

    public Publisher(ElectionBoard electionBoard, GeneratedVotePublishPanel generatedVotePublishPanel) {
        this.bundle = ResourceBundle.getBundle("Bundle");
        this.config = MiddlePanel.config;
        this.electionBoard = electionBoard;
        this.generatedVotePublishPanel = generatedVotePublishPanel;
        this.hostname = config.getProperty("hostname");
        this.port = Integer.parseInt(config.getProperty("port"));
    }

    public void startWebSrv() {
        endpoint = Endpoint.create(new ElectionBoardWebService(this.electionBoard));
        if (checkPort()) {
            generatedVotePublishPanel.appendFailure(bundle.getString("port") + " " + port + " " + bundle.getString("alreadyinuse"));
            generatedVotePublishPanel.webserviceIsStopped();
        } else {
            String address = "http://" + hostname + ":" + port + "/ElectionBoardService/ElectionBoardServiceImpl";
            endpoint.publish(address);
            if (endpoint.isPublished()) {
                generatedVotePublishPanel.appendText(bundle.getString("webservicestarted"));
                //generatedVotePublishPanel.appendText(bundle.getString("address") + ": " + address);
                generatedVotePublishPanel.webserviceIsStarted();
            }   
        }
    }

    public void stopWebSrv() {
        if (endpoint.isPublished()) {
            endpoint.stop();
            generatedVotePublishPanel.appendText(bundle.getString("webservicestopped"));
            generatedVotePublishPanel.webserviceIsStopped();
        }     
    }

    private boolean checkPort() {
        Boolean result = false;

        try {
            InetAddress ia = InetAddress.getByName(hostname);
            Socket s = new Socket(ia, port);
            result = true;
            s.close();
        } catch (IOException ex) {
            result = false;
        }

        return result;
    }
}