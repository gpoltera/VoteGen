/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.BlindedGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class BlindedGeneratorTask extends ElectionBoard {

    public static void run() throws Exception {
        BigInteger previousGenerator;
        for (int i = 0; i < mixers.length; i++) {
            BlindedGenerator blindedGenerator = new BlindedGenerator();
            blindedGenerator.setElectionId(ConfigHelper.getElectionId());
            blindedGenerator.setGenerator(mixersGenerator[i]);
            if (i == 0) {
                previousGenerator = ElectionBoard.signatureParameters.getGenerator();
            } else {
                previousGenerator = mixersGenerator[i - 1];
            }
            blindedGenerator.setProof(NIZKP.getProof(
                    mixers[i],
                    mixersSignatureKey[i],
                    mixersVerificationKey[i],
                    ElectionBoard.signatureParameters.getPrime(),
                    ElectionBoard.signatureParameters.getGroupOrder(),
                    previousGenerator));
            blindedGenerator.setSignature(SignatureGenerator.createSignature(mixers[i], blindedGenerator, mixersPrivateKey[i]));
            blindedGeneratorsList[i] = blindedGenerator;
        }
    }
}
