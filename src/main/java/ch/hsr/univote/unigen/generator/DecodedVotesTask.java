/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.dov;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask extends WahlGenerator {

    public static void run() throws Exception {
        // election id
        dov.setElectionId(ConfigHelper.getElectionId());

       for (int i = 0; i < ConfigHelper.getVotersNumber(); i++) {
            DecodedVote dv = new DecodedVote();
            for (int j = 0; j < 2; j++) {

            int x = (int) (Math.random() * 3 + 1);
            int y = (int) (Math.random() * 3 + 1);
            DecodedVoteEntry dve = new DecodedVoteEntry();
            dve.setChoiceId(1);
            dve.setCount(3);
            dv.getEntry().add(dve);

            }
            
            dov.getDecodedVote().add(dv);
        }
        
        dov.setSignature(SignatureGenerator.createSignature(dov, electionManagerPrivateKey));
    }
}
