/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysByTask extends VoteGenerator {

    public void run() throws Exception {
        MixedVerificationKeys[] mixedVerificationKeysList = new MixedVerificationKeys[electionBoard.mixers.length];
        
        /*for each mixer*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            /*create MixedVerificationKeys*/
            MixedVerificationKeys mixedVerificationKeys = createMixedVerificationKeys(i);

            // NOT YET IMPLEMENTED
            /*set proof*/
            Proof proof = new Proof();
            mixedVerificationKeys.setProof(proof);
            
            /*sign by mixer*/
            mixedVerificationKeys.setSignature(SignatureGenerator.createSignature(electionBoard.mixers[i], mixedVerificationKeys, keyStore.mixersPrivateKey[i]));
            
            /*add to list*/
            mixedVerificationKeysList[i] = mixedVerificationKeys;
        }
        /*submit to ElectionBoard*/
        electionBoard.mixedVerificationKeysList = mixedVerificationKeysList;
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(),electionBoard.mixedVerificationKeysList);
    }

    private MixedVerificationKeys createMixedVerificationKeys(int i) {
        MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
        mixedVerificationKeys.setElectionId(ConfigHelper.getElectionId());

        for (int j = 0; j < keyStore.votersVerificationKey.length; j++) {
            mixedVerificationKeys.getKey().add(keyStore.votersVerificationKey[j]);
        }

        return mixedVerificationKeys;
    }
}
