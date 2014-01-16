/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.board.ElectionBoard;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixedVerificationKeysList;
import static ch.hsr.univote.unigen.board.ElectionBoard.mixers;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysByTask extends ElectionBoard {

    public static void run() throws Exception {
        for (int i = 0; i < mixers.length; i++) {
            MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
            mixedVerificationKeys.setElectionId(ConfigHelper.getElectionId());

            for (int j = 0; j < votersVerificationKey.length; j++) {
                mixedVerificationKeys.getKey().add(votersVerificationKey[j]);
            }
            
            // NOT YET IMPLEMENTED
            Proof proof = new Proof();
            mixedVerificationKeys.setProof(proof);
            mixedVerificationKeys.setSignature(SignatureGenerator.createSignature(mixers[i], mixedVerificationKeys, mixersPrivateKey[i]));
            mixedVerificationKeysList[i] = mixedVerificationKeys;
        }
    }
}
