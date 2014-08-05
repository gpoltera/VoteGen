/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysByTask extends VoteGenerator {

    public void run() throws Exception {
        
        List<MixedVerificationKeys> listMixedVerificationKeys = new ArrayList<>();
        
        /*load the verification keys*/
        BigInteger[] verificationKeys = keyStore.getVotersVerificationKey();
        
        /*for each mixer*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            /*create MixedVerificationKeys*/
            MixedVerificationKeys mixedVerificationKeys = createMixedVerificationKeys(verificationKeys);

            // NOT YET IMPLEMENTED
            /*set proof*/
            Proof proof = new Proof();
            mixedVerificationKeys.setProof(proof);
            
            /*sign by mixer*/
            mixedVerificationKeys.setSignature(new SignatureGenerator().createSignature(electionBoard.mixers[i], mixedVerificationKeys, keyStore.getMixerPrivateKey(i)));
            
            /*add to list*/
            listMixedVerificationKeys.add(mixedVerificationKeys);
        }
        /*submit to ElectionBoard*/
        electionBoard.listMixedVerificationKeys = listMixedVerificationKeys;
    }

    private MixedVerificationKeys createMixedVerificationKeys(BigInteger[] verificationKeys) {
        MixedVerificationKeys mixedVerificationKeys = new MixedVerificationKeys();
        mixedVerificationKeys.setElectionId(config.getElectionId());

        for (BigInteger verificationKey : verificationKeys) {
            mixedVerificationKeys.getKey().add(verificationKey);
        }

        return mixedVerificationKeys;
    }
}
