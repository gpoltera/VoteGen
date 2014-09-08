/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.crypto.NIZKP;
import ch.hsr.univote.unigen.crypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysByTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;
    private List<DSAPublicKey> verificationKeys;
    private List<BigInteger> previous_VerificationKeys;

    public LatelyMixedVerificationKeysByTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        Map<String, List> listMixedVerificationKeys = new HashMap<>();

        /*load the verification keys*/
        verificationKeys = keyStore.getLatelyVotersVerificationKey();
        previous_VerificationKeys = new ArrayList<>();

        /*for each mixer*/
        for (int k = 0; k < electionBoard.mixers.length; k++) {
            /*create MixedVerificationKeys*/
            List<MixedVerificationKey> mixedVerificationKey = createMixedVerificationKeys(k);

            /*add to list*/
            listMixedVerificationKeys.put(electionBoard.mixers[k], mixedVerificationKey);
        }
        /*submit to ElectionBoard*/
        electionBoard.setVerificationKeysLatelyMixedBy(listMixedVerificationKeys);
    }

    private List<MixedVerificationKey> createMixedVerificationKeys(int k) {
        List<MixedVerificationKey> mixedVerificationKeys = new ArrayList<>();
        List<BigInteger> new_VerificationKeys = new ArrayList<>();

        if (previous_VerificationKeys.isEmpty()) {
            for (DSAPublicKey verificationKey : verificationKeys) {
                previous_VerificationKeys.add(verificationKey.getY());
            }
        }

        for (BigInteger verificationKey : previous_VerificationKeys) {
            MixedVerificationKey mixedVerificationKey = createMixedVerificationKey(k, verificationKey);
            new_VerificationKeys.add(mixedVerificationKey.getKey());
            mixedVerificationKeys.add(mixedVerificationKey);
        }
        
        previous_VerificationKeys = new_VerificationKeys;

        return mixedVerificationKeys;
    }

    private MixedVerificationKey createMixedVerificationKey(int k, BigInteger verificationKey) {
        MixedVerificationKey mixedVerificationKey = new MixedVerificationKey();
        mixedVerificationKey.setElectionId(config.getElectionId());
        BigInteger vk_ik = verificationKey.modPow(keyStore.getBlindedGeneratorKey(k).getX(), keyStore.getBlindedGeneratorKey(k).getParams().getP());
        mixedVerificationKey.setKey(vk_ik);
        BigInteger previous_g;
        if (k == 0) {
            previous_g = electionBoard.getSignatureParameters().getGenerator();
        } else {
            previous_g = electionBoard.getBlindedGenerator(electionBoard.mixers[k - 1]).getGenerator();
        }
        mixedVerificationKey.setProof(new NIZKP().getLatelyMixedVerificationKeysProof(
                electionBoard.mixers[k],
                verificationKey,
                vk_ik,
                previous_g,
                keyStore.getBlindedGeneratorKey(k),
                electionBoard.getSignatureParameters()));
        mixedVerificationKey.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[k], mixedVerificationKey, keyStore.getMixerSignatureKey(k)));

        return mixedVerificationKey;
    }
}
