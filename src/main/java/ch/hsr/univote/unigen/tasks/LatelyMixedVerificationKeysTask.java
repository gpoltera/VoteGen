/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKey;
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
        List<MixedVerificationKey> mixedVerificationKeys = electionBoard.getVerificationKeysLatelyMixedBy(config.getMixerIds()[electionBoard.mixers.length - 1]);
        List<MixedVerificationKey> new_MixedVerificationKeys = new ArrayList<>();
        /*sign all VerificationKeys by ElectionManager*/
        for (MixedVerificationKey mixedVerificationKey : mixedVerificationKeys) {
            MixedVerificationKey new_VerificationKey = new MixedVerificationKey();
            new_VerificationKey.setElectionId(mixedVerificationKey.getElectionId());
            new_VerificationKey.setKey(mixedVerificationKey.getKey());
            new_VerificationKey.setProof(mixedVerificationKey.getProof());
            new_VerificationKey.setSignature(new RSASignatureGenerator().createSignature(mixedVerificationKey, keyStore.getEMSignatureKey()));
            new_MixedVerificationKeys.add(new_VerificationKey);
        }
        
        /*submit to ElectionBoard*/
        electionBoard.setLatelyMixedVerificationKeys(new_MixedVerificationKeys);
    }    
}
