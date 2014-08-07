/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.VerificationKeys;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysTask extends VoteGenerator {

    public void run() throws Exception {
        /*load the VerificationKey from the last mixer from the ElectionBoard*/
        MixedVerificationKeys mixedVerificationKeys = electionBoard.getVerificationKeysMixedBy(config.getMixerIds()[electionBoard.mixers.length - 1]);
        
        /*create VerificationKeys*/
        VerificationKeys verificationKeys = createVerificationKeys(mixedVerificationKeys);
        
        /*sign by ElectionManager*/
        verificationKeys.setSignature(new RSASignatureGenerator().createSignature(verificationKeys, keyStore.getElectionManagerPrivateKey()));

        /*submit to ElectionBoard*/
        electionBoard.setMixedVerificationKeys(verificationKeys);
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
