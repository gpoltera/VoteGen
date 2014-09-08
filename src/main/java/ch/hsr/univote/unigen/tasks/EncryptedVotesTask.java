/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptedVotesTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public EncryptedVotesTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;
            
        run();
    }

    private void run() {
        /*create EncryptedVotes*/
        EncryptedVotes encryptedVotes = createEncryptedVotes();

        /*sign by ElectionAdministrator*/
        encryptedVotes.setSignature(new RSASignatureGenerator().createSignature(encryptedVotes, keyStore.getEASignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setEncryptedVotes(encryptedVotes);
    }

    private EncryptedVotes createEncryptedVotes() {
        EncryptedVotes encryptedVotes = new EncryptedVotes();
        encryptedVotes.setElectionId(config.getElectionId());
        /*from the last Mixer*/
        MixedEncryptedVotes mixedEncryptedVotes = electionBoard.getEncryptedVotesMixedBy(electionBoard.mixers[electionBoard.mixers.length - 1]);
        
        for (EncryptedVote encryptedVote : mixedEncryptedVotes.getVote()) {
            encryptedVotes.getVote().add(encryptedVote);
        }

        return encryptedVotes;
    }
}
