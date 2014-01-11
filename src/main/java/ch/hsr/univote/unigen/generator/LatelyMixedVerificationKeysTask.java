/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.Proof;
import ch.bfh.univote.common.Signature;
import ch.hsr.univote.unigen.generator.prov.TimestampGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.latelyMixedVerificationKeysList;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mixers;
import static ch.hsr.univote.unigen.generator.prov.WahlGenerator.mvk;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSA;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysTask {

    public static void run() throws Exception {

        for (int i = 0; i < mixers.length; i++) {
            MixedVerificationKey mixedVerificationKey = new MixedVerificationKey();
            mixedVerificationKey.setElectionId(ConfigHelper.getElectionId());
            mixedVerificationKey.setKey(BigInteger.TEN);
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.TEN);
            proof.getCommitment().add(BigInteger.TEN);
            proof.getResponse().add(BigInteger.TEN);
            mixedVerificationKey.setProof(proof);

            PrivateKey privateKey = RSA.getRSAKeyPair("sdsd").getPrivate();
            Signature signature = SignatureGenerator.createSignature(mixedVerificationKey, privateKey);
            signature.setSignerId(mixers[i]);
            signature.setTimestamp(TimestampGenerator.generateTimestamp());
            mixedVerificationKey.setSignature(signature);
            WahlGenerator.lmvk.add(mixedVerificationKey);
        }
        
    }
}
