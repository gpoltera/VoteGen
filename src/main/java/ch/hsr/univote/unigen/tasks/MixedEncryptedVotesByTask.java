/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class MixedEncryptedVotesByTask extends VoteGenerator {

    public void run() throws Exception {
        MixedEncryptedVotes[] mixedEncryptedVotesList = new MixedEncryptedVotes[electionBoard.mixers.length];

        /*for each Mixer*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            /*create MixedEncryptedVotes*/
            MixedEncryptedVotes mixedEncryptedVotes = createMixedEncryptedVotes();
            
            /*sign by Mixer*/
            mixedEncryptedVotes.setSignature(SignatureGenerator.createSignature(electionBoard.mixers[i], mixedEncryptedVotes, keyStore.mixersPrivateKey[i]));
            
            /*add to List*/
            mixedEncryptedVotesList[i] = mixedEncryptedVotes;
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptedVotesMixedBy(mixedEncryptedVotesList);
        
        /*save in db*/
        DB4O.storeDB(config.getElectionId(),mixedEncryptedVotesList);
    }

    private MixedEncryptedVotes createMixedEncryptedVotes() {
        MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
        mixedEncryptedVotes.setElectionId(config.getElectionId());
        /*for each Vote*/
        for (int j = 0; j < electionBoard.getEncryptedVotes().getVote().size(); j++) {
            mixedEncryptedVotes.getVote().add(electionBoard.getEncryptedVotes().getVote().get(j));
        }

        return mixedEncryptedVotes;
    }
}
