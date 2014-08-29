/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysByTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;
    private List<DSAPublicKey> verificationKeys;

    public MixedVerificationKeysByTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {

        List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();

        /*load the verification keys*/
        verificationKeys = keyStore.getVotersVerificationKey();

        /*for each mixer*/
        for (int k = 0; k < electionBoard.mixers.length; k++) {
            /*create MixedVerificationKeys*/
            MixedVerificationKeys mixedVerificationKeys = createMixedVerificationKeys(k);

            /*add to list*/
            listMixedVerificationKeys.add(mixedVerificationKeys);
        }
        /*submit to ElectionBoard*/
        electionBoard.setVerificationKeysMixedBy(listMixedVerificationKeys);
    }

    private MixedVerificationKeys createMixedVerificationKeys(int k) {
        MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
        mixedVerificationKeys.setElectionId(config.getElectionId());
        List<DSAPublicKey> previous_verificationKeys = verificationKeys;
        //Shuffle
        Collections.shuffle(verificationKeys, new SecureRandom());
        for (DSAPublicKey verificationKey : verificationKeys) {
            //Compute blinded verification key and add to mixedVerificationKeys
            mixedVerificationKeys.getKey().add(verificationKey.getY().modPow(keyStore.getBlindedGeneratorKey(k).getX(), keyStore.getBlindedGeneratorKey(k).getParams().getP()));
        }
        //Proof is not yet implemented
        mixedVerificationKeys.setProof(new NIZKP().getMixedVerificationKeysProof(electionBoard.mixers[k]));
        mixedVerificationKeys.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[k], mixedVerificationKeys, keyStore.getMixerSignatureKey(k)));

        return mixedVerificationKeys;
    }
}
