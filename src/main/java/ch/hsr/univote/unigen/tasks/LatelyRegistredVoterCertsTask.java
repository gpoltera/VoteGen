/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.Certificate;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyRegistredVoterCertsTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public LatelyRegistredVoterCertsTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*read VoterCertificates from ElectionBoard*/
        List<Certificate> voterCertificates = new ArrayList<>();

        /*create certificates*/
        //List<Certificate> listCertificate = createListCertficate();
        /*submit to ElectionBoard*/
        electionBoard.setLatelyRegisteredVoterCertificates(voterCertificates);
    }
}
