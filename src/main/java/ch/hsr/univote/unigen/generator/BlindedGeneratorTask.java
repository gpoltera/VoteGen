/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.generator;

import ch.bfh.univote.common.BlindedGenerator;
import ch.hsr.univote.unigen.generator.prov.WahlGenerator;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.ProofGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class BlindedGeneratorTask extends WahlGenerator {

    public static void run() throws Exception {
        BigInteger previousGenerator;
        for (int i = 0; i < mixers.length; i++) {
            BlindedGenerator blindedGenerator = new BlindedGenerator();
            blindedGenerator.setElectionId(ConfigHelper.getElectionId());
            blindedGenerator.setGenerator(mixersGenerator[i]);
            if (i == 0) {
                previousGenerator = WahlGenerator.signatureParameters.getGenerator();
            } else {
                previousGenerator = mixersGenerator[i - 1];
            }
            blindedGenerator.setProof(ProofGenerator.getProof(
                    mixers[i],
                    mixersSignatureKey[i],
                    mixersVerificationKey[i],
                    WahlGenerator.signatureParameters.getPrime(),
                    WahlGenerator.signatureParameters.getGroupOrder(),
                    previousGenerator));
            blindedGenerator.setSignature(SignatureGenerator.createSignature(mixers[i], blindedGenerator, mixersPrivateKey[i]));
            blindedGeneratorsList[i] = blindedGenerator;
        }
    }
}
