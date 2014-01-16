/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixedEncryptedVotesList;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class MixedEncryptedVotesByTask extends ElectionBoard {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
            mixedEncryptedVotes.setElectionId(ConfigHelper.getElectionId());
            for (int j = 0; j < ev.getVote().size(); j++) {
                mixedEncryptedVotes.getVote().add(ev.getVote().get(j));
            }
            
            mixedEncryptedVotes.setSignature(SignatureGenerator.createSignature(mixers[i], mixedEncryptedVotes, mixersPrivateKey[i]));
            mixedEncryptedVotesList[i] = mixedEncryptedVotes;
        }
    }
}
