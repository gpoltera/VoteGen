/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.VerificationKeys;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.db.DB4O;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;

/**
 *
 * @author Gian Poltéra
 */
public class MixedVerificationKeysTask extends VoteGenerator {

    public void run() throws Exception {
        /*create VerificationKeys*/
        VerificationKeys verificationKeys = createVerificationKeys();
        
        /*sign by ElectionManager*/
        verificationKeys.setSignature(SignatureGenerator.createSignature(verificationKeys, keyStore.electionManagerPrivateKey));

        /*submit to ElectionBoard*/
        electionBoard.verificationKeys = verificationKeys;
        
        /*save in db*/
        DB4O.storeDB(ConfigHelper.getElectionId(), verificationKeys);
    }

    private VerificationKeys createVerificationKeys() {
        VerificationKeys verificationKeys = new VerificationKeys();
        verificationKeys.setElectionId(ConfigHelper.getElectionId());

        for (int i = 0; i < electionBoard.mixedVerificationKeysList[electionBoard.mixers.length - 1].getKey().size(); i++) {
            verificationKeys.getKey().add(electionBoard.mixedVerificationKeysList[electionBoard.mixers.length - 1].getKey().get(i));
        }

        return verificationKeys;
    }
}
