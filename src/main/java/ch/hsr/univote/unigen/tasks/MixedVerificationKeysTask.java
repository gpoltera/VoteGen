/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.VerificationKeys;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysTask extends VoteGenerator {

    public void run() throws Exception {
        /*load the VerificationKey from the last mixer from the ElectionBoard*/
        MixedVerificationKeys mixedVerificationKeys = electionBoard.listMixedVerificationKeys.get(electionBoard.mixers.length - 1);
        
        /*create VerificationKeys*/
        VerificationKeys verificationKeys = createVerificationKeys(mixedVerificationKeys);
        
        /*sign by ElectionManager*/
        verificationKeys.setSignature(SignatureGenerator.createSignature(verificationKeys, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.setMixedVerificationKeys(verificationKeys);
        
        /*save in db*/
        DB4O.storeDB(config.getElectionId(), verificationKeys);
    }

    private VerificationKeys createVerificationKeys(MixedVerificationKeys mixedVerificationKeys) {
        VerificationKeys verificationKeys = new VerificationKeys();
        verificationKeys.setElectionId(config.getElectionId());

        for (BigInteger verificationKey : mixedVerificationKeys.getKey()) {
            verificationKeys.getKey().add(verificationKey);
        }

        return verificationKeys;
    }
}
