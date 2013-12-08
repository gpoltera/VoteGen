/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.EncryptedVote;
import ch.bfh.univote.common.MixedEncryptedVotes;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixedEncryptedVotesList;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class MixedEncryptedVotesByTask {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedEncryptedVotes mixedEncryptedVotes = new MixedEncryptedVotes();
            mixedEncryptedVotes.setElectionId(ConfigHelper.getElectionId());
            for (int j = 0; j < 100; j++) {
                EncryptedVote encryptedVote = new EncryptedVote();
                encryptedVote.setFirstValue(BigInteger.TEN);
                encryptedVote.setSecondValue(BigInteger.TEN);
                mixedEncryptedVotes.getVote().add(encryptedVote);
            }
            RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
            Signature signature = SignatureGenerator.createSignature(mixedEncryptedVotes, privateKey);
            signature.setSignerId(mixers[i]);
            signature.setTimestamp(TimestampGenerator.generateTimestamp());
            mixedEncryptedVotes.setSignature(signature);
            mixedEncryptedVotesList[i] = mixedEncryptedVotes;
        }
    }
}
