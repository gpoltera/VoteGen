/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixedEncryptedVotesList;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class MixedEncryptedVotesByTask extends WahlGenerator {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
            mixedEncryptedVotes.setElectionId(ConfigHelper.getElectionId());
            for (int j = 0; j < ConfigHelper.getVotersNumber(); j++) {
                EncryptedVote encryptedVote = new EncryptedVote();
                encryptedVote.setFirstValue(BigInteger.TEN);
                encryptedVote.setSecondValue(BigInteger.TEN);
                mixedEncryptedVotes.getVote().add(encryptedVote);
            }
            
            mixedEncryptedVotes.setSignature(SignatureGenerator.createSignature(mixers[i], mixedEncryptedVotes, mixersPrivateKey[i]));
            mixedEncryptedVotesList[i] = mixedEncryptedVotes;
        }
    }
}
