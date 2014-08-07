/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.univote.unigen.tasks;

import ch.bfh.univote.common.BlindedGenerator;
import ch.hsr.univote.unigen.VoteGenerator;
import ch.hsr.univote.unigen.krypto.NIZKP;
import ch.hsr.univote.unigen.krypto.RSASignatureGenerator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian Poltéra
 */
public class BlindedGeneratorTask extends VoteGenerator {

    /*1.3.4 e) Constructing the Election Generator*/
    List<BlindedGenerator> blindedGeneratorsList = new ArrayList<>();

    public void run() {
        /*for each mixer*/
        for (int k = 0; k < electionBoard.mixers.length; k++) {
            /*create BlindedGenerator*/
            BlindedGenerator blindedGenerator = createBlindedGenerator(k);

            /*sign by Mixer*/
            blindedGenerator.setSignature(new RSASignatureGenerator().createSignature(electionBoard.mixers[k], blindedGenerator, keyStore.getMixerPrivateKey(k)));

            /*add to list*/
            blindedGeneratorsList.add(k, blindedGenerator);
        }
        /*submit to ElectionBoard*/
        electionBoard.setBlindedGeneratorList(blindedGeneratorsList);
    }

    BigInteger previousGenerator = electionBoard.getSignatureParameters().getGenerator();
    BigInteger p = electionBoard.getSignatureParameters().getPrime();
    BigInteger g = electionBoard.getSignatureParameters().getGenerator();
    
    private BlindedGenerator createBlindedGenerator(int k) {
        BigInteger signatureKey = keyStore.getMixerSignatureKey(k);
        
        BlindedGenerator blindedGenerator = new BlindedGenerator();
        blindedGenerator.setElectionId(config.getElectionId());
        g = g.modPow(signatureKey, p);
        keyStore.setMixerGenerator(k, g);
        
        blindedGenerator.setGenerator(keyStore.getMixerGenerator(k));
        
        if (k == 0) {
            previousGenerator = electionBoard.getSignatureParameters().getGenerator();
        } else {
            previousGenerator = keyStore.getMixerGenerator(k - 1);
        }
        blindedGenerator.setProof(new NIZKP().getProof(
                electionBoard.mixers[k],
                keyStore.getMixerSignatureKey(k),
                keyStore.getMixerVerificationKey(k),
                electionBoard.getEncryptionParameters()));

        return blindedGenerator;
    }
}

            