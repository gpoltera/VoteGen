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

    private static String ip = "localhost";
    private static int port = 8080;
    static Endpoint ep = Endpoint.create(new ElectionBoardWebService());

    public static void startWebSrv() throws IOException {

        if (checkPort()) {
            appendFailure("Port " + port + " wird bereits verwendet");
        } else {
            ep.publish("http://" + ip + ":" + port + "/ElectionBoardService/ElectionBoardServiceImpl");
            updateProgress();
            System.out.println("Webservice start");
            appendText("Webservice gestartet");
        }
    }

    public static void stopWebSrv() {
        appendText("Webservice wird beendet");
        ep.stop();
        ep = Endpoint.create(new ElectionBoardWebService());
        System.out.println("Webservice stop");
    }

    public static boolean checkPort() throws IOException {
        Boolean result = false;

        try {
            InetAddress ia = InetAddress.getByName(ip);
            Socket s = new Socket(ia, port);
            result = true;
            s.close();
        } catch (IOException ex) {
            result = false;
        }
        
        return result;
    }
}
