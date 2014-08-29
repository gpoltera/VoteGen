/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysByTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public LatelyMixedVerificationKeysByTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();
        
        /*load the verification keys*/
        //List<SchnorrVerificationKey> verificationKeys = keyStore.getVotersVerificationKey();

        /*for each mixer*/
        //for (int i = 0; i < electionBoard.mixers.length; i++) {
            /*create MixedVerificationKeys*/
            //MixedVerificationKeys mixedVerificationKeys = createMixedVerificationKeys(verificationKeys);

            // NOT YET IMPLEMENTED
            /*set proof*/
            //Proof proof = new Proof();
            //mixedVerificationKeys.setProof(proof);

            /*sign by mixer*/
            //mixedVerificationKeys.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[i], mixedVerificationKeys, keyStore.getMixerSignatureKey(i)));

            /*add to list*/
            //listMixedVerificationKeys.add(mixedVerificationKeys);
        //}
        /*submit to ElectionBoard*/
        electionBoard.setVerificationKeysLatelyMixedBy(listMixedVerificationKeys);
    }
}
