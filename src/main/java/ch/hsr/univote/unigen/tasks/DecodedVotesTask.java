/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.DecodedVotes;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public DecodedVotesTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*create DecodedVotes*/
        DecodedVotes decodedVotes = createDecodedVotes();

        /*sign by ElectionManager*/
        decodedVotes.setSignature(new RSASignatureGenerator().createSignature(decodedVotes, keyStore.getEMSignatureKey()));

        /*submit to ElectionBoard*/
        electionBoard.setDecodedVotes(decodedVotes);
    }

    private DecodedVotes createDecodedVotes() {
        DecodedVotes decodedVotes = new DecodedVotes();
        decodedVotes.setElectionId(config.getElectionId());
        /*For all decryptedVotes*/
        for (BigInteger decryptedVote : electionBoard.getDecryptedVotes().getVote()) {
            decodedVotes.getDecodedVote().add(createDecodedVote(decryptedVote));
        }
        
        decodedVotes.setSignature(null);

        return decodedVotes;
    }

    private DecodedVote createDecodedVote(BigInteger decryptedVote) {
        DecodedVote decodedVote = new DecodedVote();
        
        
        
        
        decodedVote.getEntry().add(createDecodedVoteEntry());
        
        return decodedVote;
    }

    private DecodedVoteEntry createDecodedVoteEntry() {
        DecodedVoteEntry decodedVoteEntry = new DecodedVoteEntry();
        decodedVoteEntry.setChoiceId(1);
        decodedVoteEntry.setCount(3);

        return decodedVoteEntry;
    }
}
