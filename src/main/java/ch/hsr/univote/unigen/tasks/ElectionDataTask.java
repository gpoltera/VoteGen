/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.ElectionData;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
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
        electionData.setSignature(SignatureGenerator.createSignature(electionData, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.electionData = electionData;
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), electionData);
    }

    private ElectionData createElectionData() {
        ElectionData electionData = new ElectionData();
        electionData.setElectionGenerator(electionBoard.electionGenerator.getGenerator());
        electionData.setElectionId(ConfigHelper.getElectionId());
        electionData.setEncryptionKey(electionBoard.encryptionKey.getKey());
        electionData.setGenerator(electionBoard.encryptionParameters.getGenerator());
        electionData.setGroupOrder(electionBoard.encryptionParameters.getGroupOrder());
        electionData.setPrime(electionBoard.encryptionParameters.getPrime());
        electionData.setTitle(ConfigHelper.getElectionId());
        electionData.getChoice().addAll(electionBoard.electionOptions.getChoice());
        electionData.getRule().addAll(electionBoard.electionOptions.getRule());

        return electionData;
    }
}
