/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.MixedVerificationKeys;
import ch.bfh.univote.common.VerificationKeys;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.board.ElectionBoard;
import ch.hsr.univote.unigen.board.KeyStore;
import ch.hsr.univote.unigen.helper.ConfigHelper;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysTask {

    private ConfigHelper config;
    private ElectionBoard electionBoard;
    private KeyStore keyStore;

    public LatelyMixedVerificationKeysTask() {
        this.config = VoteGenerator.config;
        this.electionBoard = VoteGenerator.electionBoard;
        this.keyStore = VoteGenerator.keyStore;

        run();
    }

    private void run() {
        /*load the VerificationKey from the last mixer from the ElectionBoard*/
        List<MixedVerificationKey> mixedVerificationKeys = new ArrayList<>();

        /*sign by ElectionManager*/
        //VerificationKey verificationKey = new VerificationKey();
        //verificationKey.setElectionId(config.getElectionId());
        //verificationKey.setSignature(new RSASignatureGenerator().createSignature(mixedVerificationKeys, keyStore.getEASignatureKey()));

        
        /*submit to ElectionBoard*/
        electionBoard.setLatelyMixedVerificationKeys(mixedVerificationKeys);
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
