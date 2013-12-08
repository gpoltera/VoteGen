/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixedVerificationKeysList;
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
public class MixedVerificationKeysByTask {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
            mixedVerificationKeys.setElectionId(ConfigHelper.getElectionId());
            for (int j = 0; j < 100; j++) {
                mixedVerificationKeys.getKey().add(BigInteger.TEN);
            }
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.TEN);
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.TEN);
            mixedVerificationKeys.setProof(proof);
            Signature signature = new Signature();
            RSAPrivateKey privateKey = RSAGenerator.getPrivateKey();
            signature = SignatureGenerator.createSignature(mixedVerificationKeys, privateKey);
            signature.setSignerId(mixers[i]);
            signature.setTimestamp(TimestampGenerator.generateTimestamp());
            mixedVerificationKeys.setSignature(signature);
            mixedVerificationKeysList[i] = mixedVerificationKeys;
        }
    }
}
