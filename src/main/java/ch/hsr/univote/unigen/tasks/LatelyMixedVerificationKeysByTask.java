/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.Proof;
import static ch.hsr.univote.unigen.board.ElectionBoard.lmvk;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixers;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixersPrivateKey;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysByTask {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedVerificationKey mixedVerificationKey = new MixedVerificationKey();
            mixedVerificationKey.setElectionId(ConfigHelper.getElectionId());
            mixedVerificationKey.setKey(BigInteger.ZERO);

            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.ZERO);
            proof.getCommitment().add(BigInteger.ZERO);
            proof.getResponse().add(BigInteger.ZERO);
            mixedVerificationKey.setProof(proof);
            mixedVerificationKey.setSignature(SignatureGenerator.createSignature(mixers[i], mixedVerificationKey, mixersPrivateKey[i]));
            lmvk.add(mixedVerificationKey);
        }
    }
}
