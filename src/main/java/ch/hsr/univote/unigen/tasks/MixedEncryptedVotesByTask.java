/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class MixedEncryptedVotesByTask extends VoteGenerator {

    public void run() throws Exception {
        List<MixedEncryptedVotes> mixedEncryptedVotesList = new ArrayList<>();

        /*for each Mixer*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            /*create MixedEncryptedVotes*/
            MixedEncryptedVotes mixedEncryptedVotes = createMixedEncryptedVotes();
            
            /*sign by Mixer*/
            mixedEncryptedVotes.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[i], mixedEncryptedVotes, keyStore.getMixerPrivateKey(i)));
            
            /*add to List*/
            mixedEncryptedVotesList.add(i, mixedEncryptedVotes);
        }
        /*submit to ElectionBoard*/
        electionBoard.setEncryptedVotesMixedBy(mixedEncryptedVotesList);
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
