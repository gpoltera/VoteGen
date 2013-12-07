/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.DecodedVote;
import ch.bfh.univote.common.DecodedVoteEntry;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.dov;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.ed;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSAGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatur;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian
 */
public class DecodedVotesTask {

    public static void run() throws Exception {
        for (int i = 1; i <= 100; i++) {
            DecodedVote dv = new DecodedVote();
            DecodedVoteEntry dve = new DecodedVoteEntry();
            dve.setChoiceId(1);
            dve.setCount(1);
            dv.getEntry().add(dve);

            dov.getDecodedVote().add(dv);
        }
        dov.setElectionId(ConfigHelper.getElectionId());
        RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
        Signature signature = SignatureGenerator.createSignature(dov, privateKey);
        signature.setSignerId(ConfigHelper.getAdministrationId());
        signature.setTimestamp(TimestampGenerator.generateTimestamp());
        dov.setSignature(signature);
    }
}
