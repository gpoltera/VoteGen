/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptedVote;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ev;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptedVotesTask extends WahlGenerator {

    public static void run() throws Exception {
        ev.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 1; i <= ConfigHelper.getVotersNumber(); i++) {
            EncryptedVote encryptedVote = new EncryptedVote();
            encryptedVote.setFirstValue(BigInteger.TEN);
            encryptedVote.setSecondValue(BigInteger.TEN);
            ev.getVote().add(encryptedVote);
        }       
        ev.setSignature(SignatureGenerator.createSignature(ev, electionAdministratorPrivateKey));
    }
}
