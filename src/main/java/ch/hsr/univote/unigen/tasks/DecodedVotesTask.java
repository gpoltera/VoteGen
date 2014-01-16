/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.dov;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask extends ElectionBoard {

    public static void run() throws Exception {
        // election id
        dov.setElectionId(ConfigHelper.getElectionId());

       for (int i = 0; i < bts.getBallot().size(); i++) {
            DecodedVote decodedeVote = new DecodedVote();

            DecodedVoteEntry decodededVoteEntry = new DecodedVoteEntry();
            decodededVoteEntry.setChoiceId(1);
            decodededVoteEntry.setCount(3);
            
            decodedeVote.getEntry().add(decodededVoteEntry);

            dov.getDecodedVote().add(decodedeVote);
        }
        
        dov.setSignature(SignatureGenerator.createSignature(dov, electionManagerPrivateKey));
    }
}
