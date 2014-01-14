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
import static ch.hsr.univote.unigen.krypto.CertificateHelperOrig.fromByteArrayToX509Certificate;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysByTask extends WahlGenerator {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
            mixedVerificationKeys.setElectionId(ConfigHelper.getElectionId());
            
            Proof proof = new Proof();
            
            for (int j = 0; j < ConfigHelper.getVotersNumber(); j++) {
                mixedVerificationKeys.getKey().add(new BigInteger(votersPublicKey[j].getEncoded()));
            }
           
            mixedVerificationKeys.setProof(proof);
            mixedVerificationKeys.setSignature(SignatureGenerator.createSignature(mixers[i], mixedVerificationKeys, mixersPrivateKey[i]));
            mixedVerificationKeysList[i] = mixedVerificationKeys;
        }
    }
}
