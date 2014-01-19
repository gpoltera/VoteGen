/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.ev;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptedVotesTask extends ElectionBoard {

    public static void run() throws Exception {
        ev.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 0; i < bts.getBallot().size(); i++) {
            ev.getVote().add(bts.getBallot().get(i).getEncryptedVote());
        }       
        ev.setSignature(SignatureGenerator.createSignature(ev, electionAdministratorPrivateKey));
    }
}
