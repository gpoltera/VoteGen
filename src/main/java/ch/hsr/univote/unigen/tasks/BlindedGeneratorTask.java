/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.BlindedGenerator;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.SignatureGenerator;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Gian Poltéra
 */
public class BlindedGeneratorTask extends VoteGenerator {

    BlindedGenerator[] blindedGeneratorsList = new BlindedGenerator[electionBoard.mixers.length];
    BigInteger previousGenerator;

    public void run() throws Exception {
        /*for each mixer*/
        for (int i = 0; i < electionBoard.mixers.length; i++) {
            /*create BlindedGenerator*/
            BlindedGenerator blindedGenerator = createBlindedGenerator(i);

            /*sign by Mixer*/
            blindedGenerator.setSignature(new SignatureGenerator().createSignature(electionBoard.mixers[i], blindedGenerator, keyStore.getMixerPrivateKey(i)));

            /*add to list*/
            blindedGeneratorsList[i] = blindedGenerator;
        }
        /*submit to ElectionBoard*/
        electionBoard.setBlindedGeneratorList(blindedGeneratorsList);
    }

    private BlindedGenerator createBlindedGenerator(int i) {
        try {
            BlindedGenerator blindedGenerator = new BlindedGenerator();
            blindedGenerator.setElectionId(config.getElectionId());
            blindedGenerator.setGenerator(keyStore.getMixerGenerator(i));
            if (i == 0) {
                previousGenerator = electionBoard.getSignatureParameters().getGenerator();
            } else {
                previousGenerator = keyStore.getMixerGenerator(i - 1);
            }
            blindedGenerator.setProof(new NIZKP().getProof(
                    electionBoard.mixers[i],
                    keyStore.getMixerSignatureKey(i),
                    keyStore.getMixerVerificationKey(i),
                    electionBoard.getSignatureParameters().getPrime(),
                    electionBoard.getSignatureParameters().getGroupOrder(),
                    previousGenerator));

            return blindedGenerator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
