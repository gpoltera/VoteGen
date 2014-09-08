/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionData;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;

/**
 *
 * @author Gian
 */
public class ElectionDataTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public ElectionDataTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
        
        run();
    }

    private void run() {
        /*create ElectionData*/
        ElectionData electionData = createElectionData();

        /*sign by ElectionaManager*/
        electionData.setSignature(new RSASignatureGenerator().createSignature(electionData, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setElectionData(electionData);
    }

    private ElectionData createElectionData() {
        ElectionData electionData = new ElectionData();
        electionData.setElectionGenerator(electionBoard.getElectionGenerator().getGenerator());
        electionData.setElectionId(config.getElectionId());
        electionData.setEncryptionKey(electionBoard.getEncryptionKey().getKey());
        electionData.setGenerator(electionBoard.getEncryptionParameters().getGenerator());
        electionData.setGroupOrder(electionBoard.getEncryptionParameters().getGroupOrder());
        electionData.setPrime(electionBoard.getEncryptionParameters().getPrime());
        electionData.setTitle(config.getElectionTitle());
        electionData.getChoice().addAll(electionBoard.getElectionOptions().getChoice());
        electionData.getRule().addAll(electionBoard.getElectionOptions().getRule());

        return electionData;
    }
}
