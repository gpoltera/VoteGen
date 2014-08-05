/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptedVotesTask extends VoteGenerator {

    public void run() throws Exception {
        /*create EncryptedVotes*/
        EncryptedVotes encryptedVotes = createEncryptedVotes();
        
        /*sign by ElectionAdministrator*/
        encryptedVotes.setSignature(new SignatureGenerator().createSignature(encryptedVotes, keyStore.getElectionAdministratorPrivateKey()));

        /*submit to ElectionBoard*/
        electionBoard.setEncryptedVotes(encryptedVotes);
    }

    private EncryptedVotes createEncryptedVotes() {
        EncryptedVotes encryptedVotes = new EncryptedVotes();
        encryptedVotes.setElectionId(config.getElectionId());
        /*for each Ballot*/
        for (int i = 0; i < electionBoard.getBallots().getBallot().size(); i++) {
            encryptedVotes.getVote().add(electionBoard.getBallots().getBallot().get(i).getEncryptedVote());
        }
        
        return encryptedVotes;
    }
}
