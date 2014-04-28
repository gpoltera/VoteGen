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
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask extends VoteGenerator {

    public void run() throws Exception {
        /*create DecodedVotes*/
        DecodedVotes decodedVotes = createDecodedVotes();

        /*sign by ElectionManager*/
        decodedVotes.setSignature(SignatureGenerator.createSignature(decodedVotes, keyStore.electionManagerPrivateKey));
        
        /*submit to ElectionBoard*/
        electionBoard.decodedVotes = decodedVotes;
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), decodedVotes);
    }

    private DecodedVotes createDecodedVotes() {
        DecodedVotes decodedVotes = new DecodedVotes();
        decodedVotes.setElectionId(ConfigHelper.getElectionId());

        /*for each Ballot*/
        for (int i = 0; i < electionBoard.ballots.getBallot().size(); i++) {
            DecodedVote decodedeVote = createDecodedVote();
            DecodedVoteEntry decodededVoteEntry = createDecodedVoteEntry();
            decodedeVote.getEntry().add(decodededVoteEntry);
            decodedVotes.getDecodedVote().add(decodedeVote);
        }

        return decodedVotes;
    }

    private DecodedVote createDecodedVote() {
        DecodedVote decodedeVote = new DecodedVote();

        return decodedeVote;
    }

    private DecodedVoteEntry createDecodedVoteEntry() {
        DecodedVoteEntry decodededVoteEntry = new DecodedVoteEntry();
        decodededVoteEntry.setChoiceId(1);
        decodededVoteEntry.setCount(3);

        return decodededVoteEntry;
    }
}
