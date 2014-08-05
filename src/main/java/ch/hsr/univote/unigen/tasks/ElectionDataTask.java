/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionData;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian
 */
public class ElectionDataTask extends VoteGenerator {

    public void run() throws Exception {
        /*create ElectionData*/
        ElectionData electionData = createElectionData();

        /*sign by ElectionaManager*/
        electionData.setSignature(new SignatureGenerator().createSignature(electionData, keyStore.getElectionManagerPrivateKey()));

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
        electionData.setTitle(config.getElectionId());
        electionData.getChoice().addAll(electionBoard.getElectionOptions().getChoice());
        electionData.getRule().addAll(electionBoard.getElectionOptions().getRule());

        return electionData;
    }
}
