/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixedVerificationKeysList;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysByTask extends WahlGenerator {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
            mixedVerificationKeys.setElectionId(ConfigHelper.getElectionId());
            for (int j = 0; j < ConfigHelper.getVotersNumber(); j++) {
                mixedVerificationKeys.getKey().add(BigInteger.TEN);
            }
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.TEN);
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.TEN);
            mixedVerificationKeys.setProof(proof);
            mixedVerificationKeys.setSignature(SignatureGenerator.createSignature(mixers[i], mixedVerificationKeys, mixersPrivateKey[i]));
            mixedVerificationKeysList[i] = mixedVerificationKeys;
        }
    }
}
