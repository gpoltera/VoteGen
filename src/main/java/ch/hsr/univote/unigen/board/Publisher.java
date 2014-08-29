/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.board;

import static ch.hsr.univote.unigen.gui.VoteGeneration.appendFailure;
import static ch.hsr.univote.unigen.gui.VoteGeneration.appendText;
import static ch.hsr.univote.unigen.gui.VoteGeneration.updateProgress;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Gian
 */
public class Publisher {

    private ElectionBoard electionBoard;
    private Endpoint endpoint;

    private final static String IP = "localhost";
    private final static int PORT = 8080;

    public Publisher(ElectionBoard electionBoard) {
        this.electionBoard = electionBoard;
    }

    public void startWebSrv() {
        endpoint = Endpoint.create(new ElectionBoardWebService(this.electionBoard));

        if (checkPort()) {
            appendFailure("Port " + PORT + " wird bereits verwendet");
        } else {
            endpoint.publish("http://" + IP + ":" + PORT + "/ElectionBoardService/ElectionBoardServiceImpl");
            //updateProgress();
            System.out.println("Webservice start");
            //appendText("Webservice gestartet");
        }
    }

    public void stopWebSrv() {
        //appendText("Webservice wird beendet");
        endpoint.stop();
        System.out.println("Webservice stop");
    }

    private boolean checkPort() {
        Boolean result = false;

        try {
            InetAddress ia = InetAddress.getByName(IP);
            Socket s = new Socket(ia, PORT);
            result = true;
            s.close();
        } catch (IOException ex) {
            result = false;
        }

        return result;
    }
}
