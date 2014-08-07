/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.MixedVerificationKey;
import ch.bfh.univote.common.Proof;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class LatelyMixedVerificationKeysByTask extends VoteGenerator {

    public void run() throws Exception {
        List<MixedVerificationKey> listMixedVerificationKey = new ArrayList<>();
        
        /*for each mixer*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {

            MixedVerificationKey mixedVerificationKey = createMixedVerificationKey();
            Proof proof = new Proof();
            proof.getCommitment().add(BigInteger.ZERO);
            proof.getCommitment().add(BigInteger.ZERO);
            proof.getResponse().add(BigInteger.ZERO);

            /*set proof*/
            mixedVerificationKey.setProof(proof);

            /*sign by Mixer*/
            mixedVerificationKey.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[i], mixedVerificationKey, keyStore.getMixerPrivateKey(i)));

            /*add to list*/
            listMixedVerificationKey.add(mixedVerificationKey);
        }
        /*submit to ElectionBoard*/
        electionBoard.setLatelyMixedVerificationKeys(listMixedVerificationKey);
    }

    private MixedVerificationKey createMixedVerificationKey() {
        /*create MixedVerificationKey*/
        MixedVerificationKey mixedVerificationKey = new MixedVerificationKey();
        mixedVerificationKey.setElectionId(config.getElectionId());
        mixedVerificationKey.setKey(BigInteger.ZERO);

        return mixedVerificationKey;
    }
}
