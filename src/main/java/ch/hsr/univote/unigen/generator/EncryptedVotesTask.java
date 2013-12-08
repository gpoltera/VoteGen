/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.EncryptedVotes;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ev;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class EncryptedVotesTask {

    public static void run() throws Exception {
        ev.setElectionId(ConfigHelper.getElectionId());
        
        for (int i = 1; i <= 100; i++) {
            EncryptedVote encryptedVote = new EncryptedVote();
            encryptedVote.setFirstValue(BigInteger.TEN);
            encryptedVote.setSecondValue(BigInteger.TEN);
            ev.getVote().add(encryptedVote);
        }       
        
        signEncryptedVotesTask(ev);
    }
    
    private static void signEncryptedVotesTask(EncryptedVotes encryptedVotes) throws Exception {
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(encryptedVotes, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        ev.setSignature(signature);
    }
}
